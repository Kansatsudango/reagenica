package kandango.reagenica.block.entity;

import java.util.function.BooleanSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import kandango.reagenica.screen.FlatMapperMenu;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class FlatMapperBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity{
  private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        else if(slot==0) return true;
        else return false;
      }
    };
  private boolean keepEmptyBag = true;
  public void setKeepEmptyBagFlag(boolean state){
    this.keepEmptyBag=state;
  }
  public boolean isKeepEmptyBagFlagOn(){
    return keepEmptyBag;
  }
  public void toggleFlag(){
    this.keepEmptyBag = !this.keepEmptyBag;
  }

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> new FlatMapperItemHandler(
    this.itemHandler,
    this::isKeepEmptyBagFlagOn
  ));

  public FlatMapperBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.FLATMAPPER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    this.keepEmptyBag = tag.getBoolean("KeepEmptyBag");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    tag.putBoolean("KeepEmptyyBag", this.keepEmptyBag);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new FlatMapperMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("block.reagenica.flatmapper");
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
    if(itemHandler.getStackInSlot(2).isEmpty() && itemHandler.getStackInSlot(1).isEmpty()){
      ItemStack bag = itemHandler.getStackInSlot(0);
      bag.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().ifPresentOrElse(handler -> {
        boolean wasEmpty = extractItem(handler);
        if(wasEmpty){
          itemHandler.setStackInSlot(1, bag);
          itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        }
      }, this::passInput);
    }
  }

  private void passInput(){
    itemHandler.setStackInSlot(1, itemHandler.getStackInSlot(0));
    itemHandler.setStackInSlot(0, ItemStack.EMPTY);
  }

  //return: there was nothing more to extract
  private boolean extractItem(IItemHandler handler){
    if(!itemHandler.getStackInSlot(2).isEmpty())return false;
    int slots = handler.getSlots();
    for(int i=0; i<slots; i++){
      ItemStack extractedItem = handler.extractItem(i, 64, false);
      if(!extractedItem.isEmpty()){
        itemHandler.setStackInSlot(2, extractedItem);
        return false;
      }
    }
    return true;
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }

  private static class FlatMapperItemHandler implements IItemHandler{
    final ItemStackHandler handler;
    final BooleanSupplier lock;
    public FlatMapperItemHandler(ItemStackHandler handler, BooleanSupplier slotlock){
      this.handler = handler;
      this.lock = slotlock;
    }
    @Override
    public int getSlots() {
      return handler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
      return handler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
      if(slot==0){
        return handler.insertItem(slot, stack, simulate);
      }else{
        return stack;
      }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
      if(slot==0){
        return ItemStack.EMPTY;
      }else if(slot==1){
        return lock.getAsBoolean() ? ItemStack.EMPTY : handler.extractItem(slot, amount, simulate);
      }else{
        return handler.extractItem(slot, amount, simulate);
      }
    }

    @Override
    public int getSlotLimit(int slot) {
      return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      return handler.isItemValid(slot, stack);
    }

  }
}
