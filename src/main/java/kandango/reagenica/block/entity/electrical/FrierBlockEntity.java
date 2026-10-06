package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidsyncer.FluidSyncHelper;
import kandango.reagenica.block.entity.fluidsyncer.FluidSyncHelper.SyncType;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.packet.ISingleTankBlock;
import kandango.reagenica.recipes.FrierRecipe;
import kandango.reagenica.screen.FrierMenu;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class FrierBlockEntity extends ElectricConsumerAbstract implements MenuProvider,ISingleTankBlock,ILampController{
  private final ItemStackHandler itemHandler = new ItemStackHandler(13) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot<=3) return true;
        if(slot==4) return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)>0;
        else if(slot<=10) return false;
        else if(slot==11) return true;
        else return false;
      }
    };

  private final FluidTank oilTank = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
  };
  private final FluidSyncHelper fluidSyncHelper = new FluidSyncHelper(worldPosition, SyncType.ALWAYS, oilTank);
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private int fuel = 0;
  public int getFuel(){return fuel;}
  public void setFuel(int p){this.fuel=p;}
  private int fuelmax = 1600;
  public int getFuelMax(){return fuelmax;}
  public void setFuelMax(int p){this.fuelmax=p;}
  private int temperature = 0;
  public int getTemperature(){return temperature;}
  public void setTemperature(int p){this.temperature=p;}
  private boolean dirty=true;//Always dirty when loaded newly
  private FrierRecipe cachedRecipe = null;
  private boolean isUsingEnergy = false;
  public boolean isUsingEnergy(){return this.isUsingEnergy;}
  public void setUsingEnergy(boolean p){this.isUsingEnergy=p;}
  private final LampControllerHelper<FrierBlockEntity> lamphelper = new LampControllerHelper<FrierBlockEntity>(this);

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).deniedSlots(1,2,3).fuelslot(4).outputslot(5,6,7,8,9,10).anyfluidInputslot(11).build());
  private final LazyOptional<IFluidHandler> fluidTanksLazyOptional = LazyOptional.of(() -> oilTank);

  public FrierBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.FRIER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    oilTank.readFromNBT(tag.getCompound("OilTank"));
    this.progress = tag.getInt("Progress");
    this.fuel = tag.getInt("Fuel");
    this.fuelmax = tag.getInt("FuelMax");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "OilTank", oilTank);
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

  public FluidTank getOilTank(){
    return oilTank;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new FrierMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.frier");
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
    }
  }
  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(3000, 200,200);
  }
  @Override
  public void receivePacket(FluidStack fluid) {
    this.oilTank.setFluid(fluid);
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
