package kandango.reagenica.block.entity.electrical;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.block.entity.util.LitUtil;
import kandango.reagenica.recipes.CrusherRecipe;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.screen.CrusherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CrusherBlockEntity extends ElectricConsumerAbstract implements MenuProvider{
  private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot==0) return true;
        if(slot==1) return false;
        else return false;
      }
    };

  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private boolean dirty=true;
  private Optional<CrusherRecipe> cachedRecipe;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).outputslot(1).build());

  public CrusherBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.CRUSHER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    this.progress = tag.getInt("Progress");
    energyStorage.setfromtag(tag.getCompound("Electric"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    tag.putInt("Progress", progress);
    tag.put("Electric", energyStorage.serializetotag());
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
    return new CrusherMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.crusher");
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
    boolean isLit = false;
    if(lv==null){
      return;
    }
    if(dirty){
      SimpleContainer container = new SimpleContainer(1);
      container.setItem(0, itemHandler.getStackInSlot(0));
      this.cachedRecipe = lv.getRecipeManager().getRecipeFor(ModRecipes.CRUSHER_TYPE.get(), container, lv);
      if(this.cachedRecipe.isPresent()){
        ItemStack outslot = itemHandler.getStackInSlot(1);
        ItemStack output = this.cachedRecipe.orElseThrow().getOutput();
        if(!ItemStackUtil.canAddStack(outslot, output))this.cachedRecipe = Optional.empty();
      }
    }
    if(cachedRecipe.isPresent() && this.getEnergy() >= 16){
      this.progress++;
      isLit=true;
      this.consumeEnergy(16);
      if(this.progress >= 200){
        CrusherRecipe recipe = cachedRecipe.orElseThrow();
        ItemStack outslot = itemHandler.getStackInSlot(1);
        ItemStack result = recipe.getOutput();
        if(!ItemStackUtil.canAddStack(outslot, result)){
          ChemistryMod.LOGGER.error("Could not merge stack!");
          return;
        }
        itemHandler.setStackInSlot(1, ItemStackUtil.addStack(outslot, result));
        itemHandler.getStackInSlot(0).shrink(1);
        this.progress = 0;
        dirtyflag=true;
      }
    }
    dirty=dirtyflag;
    LitUtil.setLit(isLit, lv, worldPosition);
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 200,200);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }
}
