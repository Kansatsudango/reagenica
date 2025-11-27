package kandango.reagenica.block.entity;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.item.bioreagent.BioGrowingPlate;
import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.item.burnable.HeatProvidingItem;
import kandango.reagenica.recipes.ReagentMixingRecipe;
import kandango.reagenica.recipes.items.IngredientWithCount;
import kandango.reagenica.screen.ExperimentMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DraftChamberBlockEntity extends BlockEntity implements MenuProvider {
  private final ItemStackHandler itemHandler= new ItemStackHandler(13) {
      @Override
      protected void onContentsChanged(int slot) {
      setChanged();
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot<6) return true;
        else if(slot<9) return false;
        else if(slot<11) return true;
        else if(slot<=13) return true;
        else return false;
      }
    };
  private boolean cachedFire = false;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> new DraftChamberItemHandler(itemHandler));

  public DraftChamberBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.DRAFT_CHAMBER.get(), pos, state);
  }

  public ItemStackHandler getItemHandler() {
    return itemHandler;
  }
  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory",itemHandler.serializeNBT());
  }
  @Override
  public Component getDisplayName() {
      return Component.translatable("gui.reagenica.draft_chamber");
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id,@Nonnull Inventory inv,@Nonnull Player player) {
      return new ExperimentMenu(id, inv, this);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    if(tag.contains("Inventory")){
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    }
  }

  public void mixButton(boolean shift){
    if(shift){
      for(int i=0;i<64;i++){//safe guard
        if(!tryMixRecipe())break;
      }
    }else{
      tryMixRecipe();
    }
  }

  private boolean tryMixRecipe(){
    Level lv=this.level;
    if(lv == null) return false;
    if(lv.isClientSide)return false;
    SimpleContainer container = new SimpleContainer(4);
    container.setItem(0,itemHandler.getStackInSlot(9));
    container.setItem(1,itemHandler.getStackInSlot(10));
    container.setItem(2, itemHandler.getStackInSlot(11));
    container.setItem(3, itemHandler.getStackInSlot(12));
    Optional<ReagentMixingRecipe> recipeOpt = ReagentMixingRecipe.getRecipe(container, lv);
    if(recipeOpt.map(r -> r.getOutputA().getItem() == ChemiItems.GROWING_PLATE.get()).orElse(false)){
      boolean swapped = false;
      ItemStack stackPlate = itemHandler.getStackInSlot(9).copy();
      ItemStack stackParent = itemHandler.getStackInSlot(10).copy();
      if(stackParent.getItem() == ChemiItems.MEDIUM_PLATE.get()){
        ItemStack cache = stackPlate;
        stackPlate=stackParent;
        stackParent=cache;
        swapped=true;
      }
      ItemStack platestack = BioGrowingPlate.getPlateFromParent(stackPlate, stackParent);
      if(stackParent.getItem() instanceof BioReagent){
        ItemStack after = ItemStackUtil.getDamagedItem(stackParent, 80, () -> ItemStack.EMPTY);
        this.itemHandler.setStackInSlot(swapped?9:10, after);
      }else{
        itemHandler.extractItem(swapped?9:10, 1, false);
      }
      itemHandler.extractItem(swapped?10:9, 1, false);
      ItemStackUtil.insertOrElseThrow(lv, worldPosition, itemHandler, platestack, 6, 9);
    }else if(recipeOpt.isPresent()){
      ReagentMixingRecipe recipe = recipeOpt.orElseThrow();
      ResultInserts ins = getInsertSlot(recipe);
      if(ins.success){
        this.consume(recipe);
        if(itemHandler.getStackInSlot(ins.r1()).isEmpty()){
          itemHandler.setStackInSlot(ins.r1(), recipe.getOutputA().copy());
        }else{
          itemHandler.getStackInSlot(ins.r1()).grow(recipe.getOutputA().getCount());
          itemHandler.setStackInSlot(ins.r1(), itemHandler.getStackInSlot(ins.r1()));
        }
        if(!recipe.getOutputB().isEmpty()){
          if(itemHandler.getStackInSlot(ins.r2()).isEmpty()){
            itemHandler.setStackInSlot(ins.r2(), recipe.getOutputB().copy());
          }else{
            itemHandler.getStackInSlot(ins.r2()).grow(recipe.getOutputB().getCount());
            itemHandler.setStackInSlot(ins.r2(), itemHandler.getStackInSlot(ins.r2()));
          }
        }
        if(recipe.isHeatRequired()){
          ItemStack stack = itemHandler.getStackInSlot(11);
          if(stack.is(ChemiItems.EASY_TORCH.get()))ItemStackUtil.damageItemInSlot(itemHandler, 11, 1, () -> ItemStack.EMPTY);
          else ItemStackUtil.damageItemInSlot(itemHandler, 11, 1, () -> {stack.setDamageValue(stack.getMaxDamage()); return stack;});
        }
        setChanged();
        return true;
      }
    }
    return false;
  }
  private ResultInserts getInsertSlot(ReagentMixingRecipe recipe){
    ItemStack res1 = recipe.getOutputA();
    ItemStack res2 = recipe.getOutputB();
    int r1=-1;
    int r2=-1;
    for(int i=6;i<=8;i++){
      if(r1==-1 && ItemStackUtil.canAddStack(itemHandler.getStackInSlot(i), res1)){
        r1=i;
        continue;
      }
      if(r2==-1 && ItemStackUtil.canAddStack(itemHandler.getStackInSlot(i), res2)){
        r2=i;
      }
    }
    return new ResultInserts(r1,r2,(r1!=-1 && r2!=-1));
  }
  private void consume(ReagentMixingRecipe recipe){
    for(IngredientWithCount item : recipe.getInputs()){
      for(int i=9;i<=10;i++){
        ItemStack stack = itemHandler.getStackInSlot(i);
        if(item.test(stack)){
          itemHandler.extractItem(i, item.getCount(), false);
          break;
        }
      }
    }
    if(!recipe.getCatalyst().isEmpty()){
      ItemStack stack = itemHandler.getStackInSlot(12);
      itemHandler.setStackInSlot(12, ItemStackUtil.getDamagedItem(stack, 1, () -> ItemStack.EMPTY));
    }
  }
  public boolean hasActiveFire(){
    ItemStack stack = itemHandler.getStackInSlot(11);
    return stack.getItem() instanceof HeatProvidingItem && stack.getDamageValue() < stack.getMaxDamage();
  }
  public boolean isFireActiveClient(){
      return cachedFire;
  }
  public ContainerData getContainerData(){
      return new ContainerData() {
          @Override
          public int get(int index) {
              if(index==0) return hasActiveFire()?1:0;
              else throw new IllegalArgumentException("Mod Internal Error.");
          }
          @Override
          public void set(int index, int value) {
              if(index==0){
                  cachedFire = (value!=0);
              }
          }
          @Override
          public int getCount() {
              return 1;
          }
      };
  }
  protected boolean canAddStack(ItemStack insert, ItemStack origin){
      if(insert.isEmpty() || origin.isEmpty())return true;
      return ItemStack.isSameItemSameTags(origin, insert) && origin.getCount()+insert.getCount() <= origin.getMaxStackSize();
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }

  private record ResultInserts(int r1,int r2,boolean success) {
  }
  private class DraftChamberItemHandler implements IItemHandler{
    private final ItemStackHandler handler;

    public DraftChamberItemHandler(ItemStackHandler handler){
      this.handler=handler;
    }

    @Override
    public int getSlots() {
      return handler.getSlots();
    }

    @Override
    public @Nonnull ItemStack getStackInSlot(int slot) {
      return handler.getStackInSlot(slot);
    }

    @Override
    public @Nonnull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      return slot<6 ? handler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) {
      if(slot>=6 && slot<9)return handler.extractItem(slot, amount, simulate);
      else return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
      return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return handler.isItemValid(slot, stack);
    }
  }
}
