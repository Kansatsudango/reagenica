package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.fluidsyncer.FluidSyncHelper;
import kandango.reagenica.block.entity.fluidsyncer.FluidSyncHelper.SyncType;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.block.entity.util.LitUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.recipes.HeatFurnaceRecipe;
import kandango.reagenica.screen.HeatFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class HeatFurnaceBlockEntity extends ElectricConsumerAbstract implements MenuProvider,IDualTankBlock,ILampController{
  private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot==0) return true;
        if(slot==1) return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)>0;
        else if(slot<4) return false;
        else if(slot==4 || slot==6) return true;
        else return false;
      }
    };

  private final FluidTank inputFluid = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
  };
  private final FluidTank outputFluid = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
  };
  private final FluidSyncHelper fluidSyncHelper = new FluidSyncHelper(worldPosition, SyncType.ALWAYS, inputFluid, outputFluid);
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private int fuel = 0;
  public int getFuel(){return fuel;}
  public void setFuel(int p){this.fuel=p;}
  private int fuelmax = 1600;
  public int getFuelMax(){return fuelmax;}
  public void setFuelMax(int p){this.fuelmax=p;}
  private boolean dirty=true;//Always dirty when loaded newly
  private HeatFurnaceRecipe cachedRecipe = null;
  private boolean isUsingEnergy = false;
  public boolean isUsingEnergy(){return this.isUsingEnergy;}
  public void setUsingEnergy(boolean p){this.isUsingEnergy=p;}
  private final LampControllerHelper<HeatFurnaceBlockEntity> lamphelper = new LampControllerHelper<HeatFurnaceBlockEntity>(this);

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).fuelslot(1).outputslot(2,3).build());
  private final LazyOptional<IFluidHandler> fluidTanksLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(inputFluid,outputFluid));

  public HeatFurnaceBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.HEAT_FURNACE.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    inputFluid.readFromNBT(tag.getCompound("InputTank"));
    outputFluid.readFromNBT(tag.getCompound("OutputTank"));
    this.progress = tag.getInt("Progress");
    this.fuel = tag.getInt("Fuel");
    this.fuelmax = tag.getInt("FuelMax");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    CompoundTag fluidTag = new CompoundTag();
    inputFluid.writeToNBT(fluidTag);
    tag.put("InputTank",fluidTag);
    fluidTag = new CompoundTag();
    outputFluid.writeToNBT(fluidTag);
    tag.put("OutputTank",fluidTag);
    tag.putInt("Progress", progress);
    tag.putInt("Fuel", fuel);
    tag.putInt("FuelMax", fuelmax);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return fluidTanksLazyOptional.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  public FluidTank getInputTank(){
    return inputFluid;
  }
  public FluidTank getOutputTank(){
    return outputFluid;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new HeatFurnaceMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.heat_furnace");
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
    boolean dirtyflag=false;
    if(lv==null){
      return;
    }
    if(dirty){
      SimpleContainer container = new SimpleContainer(5);
      container.setItem(0, itemHandler.getStackInSlot(0));
      this.cachedRecipe = HeatFurnaceRecipe.getRecipe(this.inputFluid.getFluid(), this.itemHandler.getStackInSlot(0), lv)
                                            .filter(this::canInsert).orElse(null);
      if(this.fuel==0 && this.cachedRecipe!=null){
        ItemStack fuelstack = this.itemHandler.getStackInSlot(1);
        int burn = ForgeHooks.getBurnTime(fuelstack, RecipeType.SMELTING);
        if(burn>0){
          this.fuel = burn;
          this.fuelmax = burn;
          itemHandler.getStackInSlot(1).shrink(1);
        }
      }
      if(!this.itemHandler.getStackInSlot(4).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 4, 5, inputFluid);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 4, 5, inputFluid);
        dirtyflag = in || out;
      }
      if(!this.itemHandler.getStackInSlot(6).isEmpty()){
        dirtyflag = FluidItemConverter.draintoItem(itemHandler, 6, 7, outputFluid);
      }
    }
    dirty=dirtyflag;
    isUsingEnergy=false;
    boolean isLit=false;
    if(this.fuel > 0 || this.getEnergy() > 0){
      if(this.cachedRecipe!=null){
        lamphelper.changeLampState(LampStates.GREEN);
        isLit=true;
        ItemStack output = cachedRecipe.getOutput();
        ItemStack byproduct = cachedRecipe.getByproduct();
        FluidStack fluidin = cachedRecipe.getFluidIn();
        FluidStack fluidOut = cachedRecipe.getFluidOut();
        if(canInsert(cachedRecipe)){
          this.progress++;
          if(progress>=200){
            progress=0;
            this.progress=0;
            itemHandler.setStackInSlot(2, ItemStackUtil.addStack(this.itemHandler.getStackInSlot(2).copy(), output.copy()));
            itemHandler.setStackInSlot(3, ItemStackUtil.addStack(this.itemHandler.getStackInSlot(3).copy(), byproduct.copy()));
            lv.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            itemHandler.getStackInSlot(0).shrink(1);
            outputFluid.fill(fluidOut, FluidAction.EXECUTE);
            if(!cachedRecipe.getInputFluid().isEmpty()) inputFluid.drain(fluidin.getAmount(), FluidAction.EXECUTE);
            this.dirty = true;
          }
          if(this.fuel <= 0 && this.getEnergy() >= 8){// use Electric Energy if fuel is absent
            this.energyStorage.extractEnergy(8, false);
            this.isUsingEnergy=true;
          }
        }
      }else{
        if(itemHandler.getStackInSlot(0).isEmpty()){
          lamphelper.changeLampState(LampStates.YELLOW);
        }else{
          lamphelper.changeLampState(LampStates.RED);
        }
      }
      if(this.fuel > 0){
        this.fuel--;
        if(this.fuel==0) this.dirty = true;
      }
    }else{
      lamphelper.changeLampState(LampStates.WARN);
    }
    LitUtil.setLit(isLit, lv, worldPosition);
    lamphelper.lampSyncer();
    if(lv instanceof ServerLevel serverLevel){
      fluidSyncHelper.syncifneeded(serverLevel);
    }
  }
  private boolean canInsert(HeatFurnaceRecipe recipe){
    ItemStack resultslot = itemHandler.getStackInSlot(2);
    ItemStack byproductslot = itemHandler.getStackInSlot(3);
    return (resultslot.isEmpty() || ItemStackUtil.canAddStack(resultslot, recipe.getOutput()))
            && (byproductslot.isEmpty() || ItemStackUtil.canAddStack(byproductslot, recipe.getByproduct()))
            && (FluidStackUtil.canFullyInsertToTank(recipe.getFluidOut(), this.outputFluid));
  }
  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(3000, 20,20);
  }
  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.inputFluid.setFluid(fluid1);
    this.outputFluid.setFluid(fluid2);
  }
  @Override
  public LampStates getLampStates() {
    return lamphelper.getLampStates();
  }
  @Override
  public void receivePacket(LampStates states) {
    lamphelper.receivePacket(states);
  }
  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidTanksLazyOptional.invalidate();
  }
}
