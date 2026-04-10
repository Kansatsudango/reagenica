package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.PEMDevice;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.DrainOnlyFluidHandler;
import kandango.reagenica.block.entity.fluidhandlers.FillOnlyFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.screen.PEMDeviceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PEMDeviceBlockEntity extends ElectricConsumerAbstract implements MenuProvider{
  public static final int ENERGY_UNIT = 10;
  public static final int WATER_UNIT = 100;
  public static final int CAPACITY_UNIT = 12000;
  private boolean dirty = true;
  private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
      dirty=true;
    }

    @Override
    public boolean isItemValid(int slot, @Nullable ItemStack stack) {
      return slot%2==0;
    }
  };
  private final FluidTank oxygenTank = new FluidTank(CAPACITY_UNIT) {
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.OXYGEN.getFluid());
    }
  };
  public FluidStack getOxygen(){
    return oxygenTank.getFluid();
  }
  private final FluidTank hydrogenTank = new FluidTank(CAPACITY_UNIT) {
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.HYDROGEN.getFluid());
    }
  };
  public FluidStack getHydrogen(){
    return hydrogenTank.getFluid();
  }
  private final FluidTank waterTank = new FluidTank(CAPACITY_UNIT) {
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.DISTILLED_WATER.getFluid());
    }
  };
  public FluidStack getWater(){
    return waterTank.getFluid();
  }
  private int progress=0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).specificFluidInputSlot(ChemiFluids.DISTILLED_WATER.getFluid(), 0).build());
  private final LazyOptional<IFluidHandler> oxygenHandlerLazyOptional = LazyOptional.of(() -> new DrainOnlyFluidHandler(oxygenTank));
  private final LazyOptional<IFluidHandler> hydrogenHandlerLazyOptional = LazyOptional.of(() -> new DrainOnlyFluidHandler(hydrogenTank));
  private final LazyOptional<IFluidHandler> waterHandlerLazyOptional = LazyOptional.of(() -> new FillOnlyFluidHandler(waterTank));

  public PEMDeviceBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.PEM_DEVICE.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    hydrogenTank.readFromNBT(tag.getCompound("HydrogenTank"));
    oxygenTank.readFromNBT(tag.getCompound("OxygenTank"));
    waterTank.readFromNBT(tag.getCompound("WaterTank"));
    progress = tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "HydrogenTank", hydrogenTank);
    FluidStackUtil.saveFluid(tag, "OxygenTank", oxygenTank);
    FluidStackUtil.saveFluid(tag, "WaterTank", waterTank);
    tag.putInt("Progress", progress);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandlerLazyOptional.cast();
    }else if(cap == ForgeCapabilities.FLUID_HANDLER){
      Direction facing = getBlockState().getOptionalValue(PEMDevice.FACING).orElse(Direction.NORTH);
      if(side == Direction.UP){
        return waterHandlerLazyOptional.cast();
      }else if(side == facing.getClockWise()){
        return hydrogenHandlerLazyOptional.cast();
      }else if(side == facing.getCounterClockWise()){
        return oxygenHandlerLazyOptional.cast();
      }else{
        return LazyOptional.empty();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    boolean flag = false;
    if(dirty){
      if(!this.itemHandler.getStackInSlot(0).isEmpty()){
        flag |= FluidItemConverter.drainfromItem(itemHandler, 0, 1, waterTank);
        flag |= FluidItemConverter.draintoItem(itemHandler, 0, 1, waterTank);
      }
      if(!this.itemHandler.getStackInSlot(2).isEmpty()){
        flag |= FluidItemConverter.draintoItem(itemHandler, 2, 3, hydrogenTank);
      }
      if(!this.itemHandler.getStackInSlot(4).isEmpty()){
        flag |= FluidItemConverter.draintoItem(itemHandler, 4, 5, oxygenTank);
      }
      this.dirty=flag;
    }
    if(eitherHasSpace() && this.waterTank.getFluidAmount() >= 100 && this.energyStorage.getEnergyStored() >= ENERGY_UNIT){
      this.progress++;
      this.energyStorage.extractEnergy(ENERGY_UNIT, false);
      if(this.progress>=200){
        this.progress=0;
        this.waterTank.drain(100, FluidAction.EXECUTE);
        this.hydrogenTank.fill(new FluidStack(ChemiFluids.HYDROGEN.getFluid(), 200), FluidAction.EXECUTE);
        this.oxygenTank.fill(new FluidStack(ChemiFluids.OXYGEN.getFluid(), 100), FluidAction.EXECUTE);
      }
    }
  }
  private boolean eitherHasSpace(){
    return this.hydrogenTank.getFluidAmount() < this.hydrogenTank.getCapacity()
          && this.oxygenTank.getFluidAmount() < this.oxygenTank.getCapacity();
  }
  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    ChemistryMod.LOGGER.info("creating Menu");
    return new PEMDeviceMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.pem_device");
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
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 1000,400);
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    oxygenHandlerLazyOptional.invalidate();
    hydrogenHandlerLazyOptional.invalidate();
    waterHandlerLazyOptional.invalidate();
  }
}
