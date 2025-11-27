package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.block.entity.util.LitUtil;
import kandango.reagenica.screen.FuelGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.items.ItemStackHandler;

public class FuelGeneratorBlockEntity extends ElectricGeneratorAbstract implements MenuProvider{
  private boolean dirty = true;
  private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
      dirty=true;
    }

    @Override
    public boolean isItemValid(int slot, @Nullable ItemStack stack) {
      return true;
    }
  };

  private int burnTimeRemaining = 0;
  private int maxburnTime = 0;

  private final LazyOptional<ItemStackHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);

  public FuelGeneratorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.GENERATOR_FUEL.get(),pos,state);
  }

  public int getBurnTime(){
    return burnTimeRemaining;
  }
  public int getMaxburntime(){
    return maxburnTime;
  }
  public void setBurnTime(int x){
    this.burnTimeRemaining = x;
  }
  public void setMaxburntime(int x){
    this.maxburnTime = x;
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    this.burnTimeRemaining = tag.getInt("BurnTime");
    this.maxburnTime = tag.getInt("MaxBurnTime");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    tag.putInt("BurnTime", burnTimeRemaining);
    tag.putInt("MaxBurnTime", maxburnTime);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    boolean flag=false;
    if (burnTimeRemaining > 0) {
      burnTimeRemaining--;
      generateEnergy();
      if(burnTimeRemaining==0){
        flag=true;
      }
    } else if (this.dirty) {
      ItemStack fuelstack = this.itemHandler.getStackInSlot(0);
      int burn = ItemStackUtil.getFuelExceptforLava(fuelstack);
      if (burn > 0) {
        burnTimeRemaining = burn;
        maxburnTime = burn;
        fuelstack.shrink(1);
        LitUtil.setLit(true, lv, worldPosition);
      }else{
        LitUtil.setLit(false, lv, worldPosition);
      }
    }
    dirty=flag;
    this.provideEnergy();
  }

  private void generateEnergy(){
    if (energyStorage.receiveEnergy(8, true) > 0) {
      energyStorage.receiveEnergy(8, false);
    }
  }
  
  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new FuelGeneratorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.fuel_generator");
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 1000,400);
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }
}
