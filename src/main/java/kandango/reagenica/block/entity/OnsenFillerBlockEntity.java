package kandango.reagenica.block.entity;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.BlockUtil;
import kandango.reagenica.block.OnsenFiller;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class OnsenFillerBlockEntity extends BlockEntity implements ITickableBlockEntity{
  private final FluidTank fluidTank = new FluidTank(1000){
    @Override
    protected void onContentsChanged(){
      setChanged();
    }
  };
  private int cooldown = 20;

  public OnsenFillerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.ONSEN_FILLER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    fluidTank.readFromNBT(tag.getCompound("Tank"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    FluidStackUtil.saveFluid(tag, "Tank", fluidTank);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    return super.getCapability(cap, side);
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

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null){
      return;
    }
    BlockEntity below = lv.getBlockEntity(worldPosition.below());
    if(below!=null) below.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(this::drawFluid);
    if(cooldown>0){
      cooldown--;
    }else{
      cooldown=20;
      if(fluidTank.getFluidAmount() >= 1000){
        @Nullable final Direction facing = BlockUtil.getStatus(lv.getBlockState(worldPosition),OnsenFiller.FACING).orElse(null);
        fillWater(lv,facing,fluidTank.getFluid().getFluid());
      }
    }
  }
  private void drawFluid(IFluidHandler another){
    int max = fluidTank.getCapacity();
    int current = fluidTank.getFluidAmount();
    if(current<max){
      int drawing = Math.min(max-current,100);
      FluidStack stack = another.drain(drawing, FluidAction.EXECUTE);
      fluidTank.fill(stack, FluidAction.EXECUTE);
    }
  }
  private void fillWater(@Nonnull Level lv,@Nullable Direction facing,@Nullable Fluid fluid){
    final int MAX_BELOW = 16;
    if(facing==null)return;
    BlockPos startPos = worldPosition.relative(facing);
    @Nullable BlockPos originPos = null;
    for(int i=1;i<MAX_BELOW;i++){
      BlockPos targetPos = startPos.below(i);
      if(isWall(lv.getBlockState(targetPos), fluid)){
        if(i>=2){
          originPos = targetPos.above();
          findLowestFillingPos(lv, originPos, fluid).ifPresent(pos -> placeSource(lv, pos, fluid));
        }
        break;
      }
    }
  }
  private void placeSource(@Nonnull Level lv, BlockPos pos, Fluid fluid){
    fluidTank.drain(1000, FluidAction.EXECUTE);
    BlockState state = fluid.defaultFluidState().createLegacyBlock();
    lv.setBlock(pos, state, Block.UPDATE_ALL);
  }
  private boolean isWall(BlockState state, Fluid fillingFluid){
    FluidState fs = state.getFluidState();
    boolean is_same_fluid = (fs.getType().isSame(fillingFluid));
    boolean is_air = state.isAir();
    return !(is_same_fluid || is_air);
  }
  private Optional<BlockPos> findLowestFillingPos(@Nonnull Level lv, BlockPos origin, Fluid fluid){
    final int MAX_FLUID_VISIT = 1000;
    int watchdog = 0;
    int lowest_Y = 10000;
    Queue<BlockPos> queue = new ArrayDeque<>();
    Set<BlockPos> visited = new HashSet<>();
    @Nullable BlockPos fillCandidatePos = null;
    queue.add(origin);
    visited.add(origin);
    while (!queue.isEmpty() && watchdog < MAX_FLUID_VISIT) {
      watchdog++;
      BlockPos pos = queue.poll();
      if(pos.getY() < lowest_Y){
        if(shouldFill(lv, pos, fluid)){
          lowest_Y = pos.getY();
          fillCandidatePos=pos;
        }
      }
      for(Direction dir : Direction.values()){
        BlockPos next = pos.relative(dir);
        if(visited.contains(next))continue;
        if(!lv.isLoaded(next))continue;
        if((next.getY() < worldPosition.getY()) && (isSameFluid(lv, next, fluid) 
            || (isSameFluid(lv, pos, fluid) && lv.getBlockState(next).isAir()))){
          queue.add(next);
          visited.add(next);
        }
      }
    }
    return Optional.ofNullable(fillCandidatePos);
  }
  private boolean shouldFill(Level lv, BlockPos pos, Fluid fillingFluid){
    BlockState state = lv.getBlockState(pos);
    FluidState fs = state.getFluidState();
    boolean is_air = state.isAir();
    boolean is_source = fs.isSourceOfType(fillingFluid);
    boolean is_same_fluid = isSameFluid(state, fillingFluid);
    boolean is_closed = isClosed(lv, pos, fillingFluid);
    return is_closed && (is_air || (!is_source && is_same_fluid));
  }
  private boolean isSameFluid(Level lv, BlockPos pos, Fluid fluid){
    return isSameFluid(lv.getBlockState(pos), fluid);
  }
  private boolean isSameFluid(BlockState state, Fluid fluid){
    FluidState fs = state.getFluidState();
    boolean answer = fs.getFluidType() == fluid.getFluidType();
    return answer;
  }
  private boolean isClosed(Level lv, BlockPos origin, Fluid fluid){
    final int MAX_SIZE = 201;
    int count = 0;
    Queue<BlockPos> queue = new ArrayDeque<>();
    Set<BlockPos> visited = new HashSet<>();
    queue.add(origin);
    visited.add(origin);
    while (!queue.isEmpty() && count < MAX_SIZE) {
      count++;
      BlockPos pos = queue.poll();
      for(Direction dir : Direction.Plane.HORIZONTAL){
        BlockPos next = pos.relative(dir);
        if(visited.contains(next))continue;
        if(!lv.isLoaded(next))continue;
        if(isSameFluid(lv, next, fluid) || lv.getBlockState(next).isAir()){
          queue.add(next);
          visited.add(next);
        }
      }
    }
    return count < MAX_SIZE;
  }
}
