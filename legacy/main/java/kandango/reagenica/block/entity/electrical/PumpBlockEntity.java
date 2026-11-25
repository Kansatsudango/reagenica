package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.PumpChemistry;
import kandango.reagenica.block.entity.ITickableBlockEntity;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.NopFluidHandler;
import kandango.reagenica.packet.ISingleTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncFluidPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;

public class PumpBlockEntity extends BlockEntity implements ISingleTankBlock,ITickableBlockEntity{
  private final FluidTank internalFluid = new FluidTank(100){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };

  public FluidStack getFluid(){
    return internalFluid.getFluid().copy();
  }

  public PumpBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.PUMP.get(),pos,state);
  }

  private void syncFluidToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new SyncFluidPacket(worldPosition, internalFluid.getFluid().copy())
      );
    }
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    internalFluid.readFromNBT(tag.getCompound("Internal"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    CompoundTag fluidTag = new CompoundTag();
    internalFluid.writeToNBT(fluidTag);
    tag.put("Internal",fluidTag);
    super.saveAdditional(tag);
  }

  private final LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> new NopFluidHandler(internalFluid));
  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      Direction facing = getBlockState().getValue(PumpChemistry.FACING);
      if(side == facing || side == facing.getOpposite()){
        return lazyFluidHandler.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void receivePacket(FluidStack fluid1){
    this.internalFluid.setFluid(fluid1);
  }

  @Override
  public void serverTick(){
    Direction pushside = getBlockState().getValue(PumpChemistry.FACING);
    Direction pullside = pushside.getOpposite();
    BlockPos pos = this.getBlockPos();
    Level lv = this.getLevel();
    if(lv == null)return;
    BlockEntity pullingsubj = lv.getBlockEntity(pos.relative(pullside));
    if(pullingsubj != null){
      pullingsubj.getCapability(ForgeCapabilities.FLUID_HANDLER, pullside.getOpposite()).ifPresent(handler -> pull(handler));
    }
    BlockEntity pushingsubj = lv.getBlockEntity(pos.relative(pushside));
    if(pushingsubj != null){
      pushingsubj.getCapability(ForgeCapabilities.FLUID_HANDLER, pushside.getOpposite()).ifPresent(handler -> push(handler));
    }
  }
  private void pull(IFluidHandler handler){
    FluidStack pullingstack = handler.drain(20, FluidAction.SIMULATE);
    int pullable = this.internalFluid.fill(pullingstack, FluidAction.SIMULATE);
    if(pullable!=0){
      FluidStack pulledstack = handler.drain(pullable, FluidAction.EXECUTE);
      this.internalFluid.fill(pulledstack,FluidAction.EXECUTE);
    }
  }
  private void push(IFluidHandler handler){
    int pushable = handler.fill(this.internalFluid.getFluid(), FluidAction.SIMULATE);
    if(pushable!=0){
      FluidStack pushing = this.internalFluid.getFluid().copy();
      pushing.setAmount(Math.min(pushing.getAmount(), 20));
      int pushed = handler.fill(pushing,FluidAction.EXECUTE);
      this.internalFluid.drain(pushed, FluidAction.EXECUTE);
    }
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    lazyFluidHandler.invalidate();
  }
}

