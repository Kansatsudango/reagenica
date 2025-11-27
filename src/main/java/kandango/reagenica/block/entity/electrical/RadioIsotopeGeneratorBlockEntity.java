package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.screen.RadioIsotopeGeneratorMenu;
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

public class RadioIsotopeGeneratorBlockEntity extends ElectricGeneratorAbstract implements MenuProvider{
  private boolean dirty = true;
  private int cache = 0;
  private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
      dirty=true;
    }

    @Override
    public boolean isItemValid(int slot, @Nullable ItemStack stack) {
      if(stack==null)return false;
      return stack.is(ChemiItems.PLUTONIUM_PELLET.get());
    }
  };

  private final LazyOptional<ItemStackHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);

  public RadioIsotopeGeneratorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.GENERATOR_RADIOISOTOPE.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
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
    if(dirty){
      refleshOutput();
    }
    generateEnergy();
    this.provideEnergy();
  }

  private void refleshOutput(){
    int pellets=0;
    for(int i=0;i<6;i++){
      if(itemHandler.getStackInSlot(i).is(ChemiItems.PLUTONIUM_PELLET.get())){
        pellets++;
      }
    }
    cache = switch(pellets){
      case 0 -> 0;
      case 1 -> 1;
      case 2 -> 2;
      case 3 -> 4;
      case 4 -> 7;
      case 5 -> 10;
      case 6 -> 14;
      default -> 0;
    };
  }
  private void generateEnergy(){
    energyStorage.receiveEnergy(cache, false);
  }
  
  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new RadioIsotopeGeneratorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.radioisotope_generator");
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
