package kandango.reagenica.block.entity;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.recipes.AnalyzerRecipe;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.recipes.items.ItemStackWithChance;
import kandango.reagenica.screen.AnalyzerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
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

public class AnalyzerBlockEntity extends BlockEntity implements MenuProvider{
  private final Random rand = new Random();
  private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        return slot==0;
      }
    };
  
  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).outputslot(1,2,3,4,5,6,7).build());

  public AnalyzerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.ANALYZER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id,@Nonnull Inventory inv,@Nonnull Player player) {
    return new AnalyzerMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.analyzer");
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

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  public void analyze(){
    Level lv = this.level;
    if(lv==null)return;
    ItemStack item = this.itemHandler.getStackInSlot(0);
    SimpleContainer container = new SimpleContainer(1);
    container.setItem(0, item);
    Optional<AnalyzerRecipe> mayrecipe = lv.getRecipeManager().getRecipeFor(ModRecipes.ANALYZER_TYPE.get(), container, lv);
    if(mayrecipe.isPresent()){
      AnalyzerRecipe recipe = mayrecipe.orElseThrow();
      ItemStack returnitem = recipe.getReturnItem();
      List<ItemStackWithChance> results = recipe.getResults();
      this.itemHandler.getStackInSlot(0).shrink(1);
      boolean flag = ItemStackUtil.addStackToSlotifPossible(itemHandler, 1, returnitem);
      if(!flag)drop(lv,returnitem);
      for(ItemStackWithChance stack : results){
        this.insert(lv, stack.roll(rand));
      }
    }
  }
  private void insert(@Nonnull Level lv,ItemStack stack){
    for(int i=2;i<=7;i++){
      boolean insert = ItemStackUtil.addStackToSlotifPossible(itemHandler, i, stack);
      if(insert)return;
    }
    drop(lv,stack);
  }
  private void drop(@Nonnull Level lv,ItemStack item){
    if(lv.isClientSide)return;
    if(!item.isEmpty()){
      double x = this.getBlockPos().getX() + 0.5;
      double y = this.getBlockPos().getY() + 1.1;
      double z = this.getBlockPos().getZ() + 0.5;
      ItemEntity itemEntity = new ItemEntity(lv, x, y, z, item);
      lv.addFreshEntity(itemEntity);
    }
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }
}
