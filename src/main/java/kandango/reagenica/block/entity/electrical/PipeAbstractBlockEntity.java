package kandango.reagenica.block.entity.electrical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ITickableBlockEntity;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;

public abstract class PipeAbstractBlockEntity extends BlockEntity implements IDualTankBlock,ITickableBlockEntity{
  private final Set<Direction> inputDirs = new HashSet<>();
  private final FluidTank internalFluid = new FluidTank(100){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  private final FluidTank internalFluidBuf = new FluidTank(100){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> internalFluid);
  private Status beat = Status.SLEEP;
  private int waittick = 0;
  private boolean dirty = true;//it only dirts when load

  public FluidStack getFluid(){
    FluidStack stack1 = internalFluid.getFluid();
    FluidStack stack2 = internalFluidBuf.getFluid();
    return new FluidStack(stack1.getFluid(), stack1.getAmount()+stack2.getAmount());
  }

  public PipeAbstractBlockEntity(BlockEntityType<? extends PipeAbstractBlockEntity> type,BlockPos pos, BlockState state){
    super(type,pos,state);
  }

  private void syncFluidToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new SyncDualFluidTanksPacket(worldPosition, internalFluid.getFluid().copy(), internalFluidBuf.getFluid().copy())
      );
    }
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    internalFluid.readFromNBT(tag.getCompound("Internal"));
    internalFluidBuf.readFromNBT(tag.getCompound("InternalBuf"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    CompoundTag fluidTag = new CompoundTag();
    internalFluid.writeToNBT(fluidTag);
    tag.put("Internal",fluidTag);
    fluidTag = new CompoundTag();
    internalFluidBuf.writeToNBT(fluidTag);
    tag.put("InternalBuf",fluidTag);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return fluidHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2){
    this.internalFluid.setFluid(fluid1);
    this.internalFluidBuf.setFluid(fluid2);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      this.handleUpdateTag(pkt.getTag());
  }
  
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
  }
  
  @Override
  public CompoundTag getUpdateTag() {
      return saveWithoutMetadata();
  }
  
  @Override
  public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
  }

  public void setInSide(Direction dir){
    this.inputDirs.add(dir);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    if(this.dirty){
      this.dirty=false;
    }
    if(beat==Status.SLEEP){
      if(!internalFluid.isEmpty()){
        beat = Status.IDLE;
        waittick=0;
      }
    }else if(beat==Status.ABSORB){
      int filled = internalFluid.fill(internalFluidBuf.getFluid(), FluidAction.EXECUTE);
      if(filled != internalFluidBuf.getFluidAmount()) throw new IllegalStateException("Fluid merge failed at "+this.getBlockPos().toString());
      internalFluidBuf.setFluid(FluidStack.EMPTY);
      beat = Status.ACT;
      if(internalFluid.isEmpty()){
        beat = Status.SLEEP;
        inputDirs.clear();
      }
    }else if(beat==Status.ACT){
      if(internalFluid.isEmpty()){
        beat = Status.SLEEP;
        return;
      }
      Map<Direction, IFluidHandler> handlers = new HashMap<>();
      Map<Direction, Integer> demands = new HashMap<>();
      for(Direction dir : Direction.values()){
        if(inputDirs.contains(dir))continue;
        isValidConnection(dir).ifPresent(handler -> {handlers.put(dir, handler);
          demands.put(dir, getInputtableamount(handler, internalFluid.getFluid()));});
      }
      int demand = demands.values().stream().mapToInt(x -> x).sum();
      if(demand==0)inputDirs.clear();
      float distribute_rate = (float)(Math.min(internalFluid.getFluidAmount(),this.maxTransfer())) / (float)demand;
      if(distribute_rate>1.0f)distribute_rate=1.0f;
      for(Direction dir : Direction.values()){
        IFluidHandler handler = handlers.get(dir);
        if(handler != null){
          int amount = (int)Math.ceil((float)demands.get(dir) * distribute_rate);
          int finalamount = Math.min(amount,this.internalFluid.getFluidAmount());
          handler.fill(new FluidStack(this.internalFluid.getFluid().getFluid(), finalamount),FluidAction.EXECUTE);
          this.internalFluid.drain(finalamount, FluidAction.EXECUTE);
          BlockEntity be = lv.getBlockEntity(this.getBlockPos().relative(dir));
          if(be instanceof PipeAbstractBlockEntity pipe){
            pipe.setInSide(dir.getOpposite());
          }
        }
      }
      beat = Status.IDLE;
    }else if(beat==Status.IDLE){
      waittick++;
      if(waittick>=8){
        beat = Status.ABSORB;
        waittick = 0;
      }
    }
  }
  private LazyOptional<IFluidHandler> isValidConnection(Direction dir){
    Level lv = this.level;
    if(lv==null)return LazyOptional.empty();
    BlockPos here = this.getBlockPos();
    BlockEntity relative = lv.getBlockEntity(here.relative(dir));
    if(relative==null) return LazyOptional.empty();
    else if(relative instanceof PipeAbstractBlockEntity){
      if(relative.getClass() != this.getClass()){
        return LazyOptional.empty();
      }
    }
    return relative.getCapability(ForgeCapabilities.FLUID_HANDLER, dir.getOpposite());
  }
  private int getInputtableamount(IFluidHandler handler, FluidStack fluid){
    return handler.fill(fluid, FluidAction.SIMULATE);
  }

  protected abstract int maxTransfer();

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    fluidHandlerLazyOptional.invalidate();
  }

  public enum Status{
    SLEEP,
    ABSORB,
    ACT,
    IDLE
  }
}
