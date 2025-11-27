package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.screen.HeatGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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

public class HeatGeneratorBlockEntity extends ElectricGeneratorAbstract implements MenuProvider,IDualTankBlock{
  private boolean dirty = true;
  private final int GENERATE_UNIT = 10;

  private final FluidTank inputFluid = new FluidTank(4000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private final FluidTank outputFluid = new FluidTank(4000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };

  public FluidTank getInputTank(){
    return inputFluid;
  }
  public FluidTank getOutputTank(){
    return outputFluid;
  }

  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(inputFluid, outputFluid));

  public HeatGeneratorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.HEAT_GENERATOR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    inputFluid.readFromNBT(tag.getCompound("InputTank"));
    outputFluid.readFromNBT(tag.getCompound("OutputTank"));
    energyStorage.setfromtag(tag.getCompound("Electric"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    CompoundTag fluidTag = new CompoundTag();
    inputFluid.writeToNBT(fluidTag);
    tag.put("InputTank",fluidTag);
    fluidTag = new CompoundTag();
    outputFluid.writeToNBT(fluidTag);
    tag.put("OutputTank",fluidTag);
    tag.put("Electric", energyStorage.serializetotag());
  }
  @Override
  public CompoundTag getUpdateTag() {
      return saveWithoutMetadata();
  }
  
  @Override
  public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
  }

  private void syncFluidToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new SyncDualFluidTanksPacket(worldPosition, inputFluid.getFluid().copy(), outputFluid.getFluid().copy())
      );
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if(cap == ForgeCapabilities.FLUID_HANDLER){
      return fluidHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    boolean flag=false;
    if(dirty){
    }
    if(!this.inputFluid.isEmpty() && this.inputFluid.getFluidAmount()>GENERATE_UNIT){
      FluidStack distilled = new FluidStack(ChemiFluids.DISTILLED_WATER.getFluid(), GENERATE_UNIT);
      if(FluidStackUtil.canFullyInsertToTank(distilled, outputFluid)){
        this.generateEnergy();
        this.inputFluid.drain(GENERATE_UNIT, FluidAction.EXECUTE);
        this.outputFluid.fill(distilled, FluidAction.EXECUTE);
      }
    }
    for(Direction dir : Direction.values()){
      BlockPos nei = worldPosition.relative(dir);
      BlockEntity neighbor = lv.getBlockEntity(nei);
      if(neighbor!=null){
        LazyOptional<IFluidHandler> mayhandler = neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, dir.getOpposite());
        mayhandler.ifPresent(handler -> {
          FluidStack drawing = handler.drain(20, FluidAction.SIMULATE);
          if(drawing.getFluid().isSame(ChemiFluids.HEATED_WATER.getFluid())){
            this.inputFluid.fill(handler.drain(20, FluidAction.EXECUTE), FluidAction.EXECUTE);
          }
        });
      }
    }
    dirty=flag;
    this.provideEnergy();
  }

  private void generateEnergy(){
    if (energyStorage.receiveEnergy(48, true) > 0) {
      energyStorage.receiveEnergy(48, false);
    }
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new HeatGeneratorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.heat_generator");
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 1000,600);
  }

  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.inputFluid.setFluid(fluid1);
    this.outputFluid.setFluid(fluid2);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    fluidHandlerLazyOptional.invalidate();
  }
}
