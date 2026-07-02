package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampState;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.recipes.ReagentMixingRecipe;
import kandango.reagenica.recipes.items.IngredientWithCount;
import kandango.reagenica.screen.AutoExperimenterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AutoExperimenterBlockEntity extends ElectricConsumerAbstract implements MenuProvider,ILampController{
  private final ItemStackHandler itemHandler = new ItemStackHandler(19) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        else return true;
      }
    };

  private int progress = 0;
  private boolean isHeating = false;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  public int getHeatStatus(){
    ReagentMixingRecipe recipe = this.internalRecipe;
    if(recipe!=null){
      boolean recipeRequireHeat = recipe.isHeatRequired();
      return (recipeRequireHeat?1:0) + (isHeating?2:0);
    }
    return 0;
  }
  private boolean dirty=true;
  @Nullable private ReagentMixingRecipe internalRecipe = null;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).outputslot(8,9).deniedSlots(0,1,2,3,4,5,6,7).build());
  private final LampControllerHelper<AutoExperimenterBlockEntity> lamphelper = new LampControllerHelper<>(this);

  public AutoExperimenterBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.AUTO_EXPERIMENTER.get(),pos,state);
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
    return new AutoExperimenterMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.auto_experimenter");
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
    if(dirty){
      SimpleContainer container = new SimpleContainer(4);
      container.setItem(0,itemHandler.getStackInSlot(0));
      container.setItem(1,itemHandler.getStackInSlot(1));
      container.setItem(2, new ItemStack(ChemiItems.ALCHOHOL_LAMP.get()));
      container.setItem(3, itemHandler.getStackInSlot(2));
      this.internalRecipe = ReagentMixingRecipe.getRecipe(container, level).orElse(null);
      if(this.internalRecipe != null){
        itemHandler.setStackInSlot(3, this.internalRecipe.getOutputA());
        itemHandler.setStackInSlot(4, this.internalRecipe.getOutputB());
      }else{
        itemHandler.setStackInSlot(3, ItemStack.EMPTY);
        itemHandler.setStackInSlot(4, ItemStack.EMPTY);
      }
    }
    ReagentMixingRecipe recipe = this.internalRecipe;
    boolean isOperating=false;
    this.isHeating=false;
    if(recipe!=null){
      bringItems(recipe);
      if(hasEnoughIngredients(recipe) && canInsert(recipe)){
        boolean recipeRequireHeat = recipe.isHeatRequired();
        final int energyConsume = recipeRequireHeat?12:6;
        if(energyStorage.getEnergyStored() >= energyConsume){
          energyStorage.extractEnergy(energyConsume, false);
          this.progress++;
          isOperating=true;
          if(recipeRequireHeat)this.isHeating=true;
        }
        if(this.progress>=200){
          consume(recipe);
          itemHandler.insertItem(8, recipe.getOutputA(), false);
          itemHandler.insertItem(9, recipe.getOutputB(), false);
          this.progress=0;
        }
      }
    }
    if(recipe==null){
      lamphelper.changeLampState(LampStates.YELLOW);
    }else{
      if(isOperating){
        if(energyStorage.getEnergyStored()>=8000)lamphelper.changeLampState(LampStates.GREEN);
        else lamphelper.changeLampState(new LampStates(LampState.OFF, LampState.BLINK, LampState.ON));
      }else if(!canInsert(recipe)){
        lamphelper.changeLampState(new LampStates(LampState.ON, LampState.BLINK, LampState.OFF));
      }else if(!hasEnoughIngredients(recipe)){
        lamphelper.changeLampState(LampStates.SLOW);
      }else{
        lamphelper.changeLampState(new LampStates(LampState.ON, LampState.ON, LampState.OFF));
      }
    }
    lamphelper.lampSyncer();
  }
  private void bringItems(@Nonnull ReagentMixingRecipe recipe){
    ItemStack inputA = itemHandler.getStackInSlot(5);
    ItemStack inputB = itemHandler.getStackInSlot(6);
    boolean inserted=false;
    for(IngredientWithCount ic : recipe.getInputs()){
      if(inserted)break;
      if(ic.test(inputA) || ic.test(inputB)){
        continue; 
      }else{
        Ingredient singleIngredient = ic.getIngredient();
        int targetSlot = singleIngredient.test(inputB) ? 6 : 5;
        boolean inputted = findAndBring(singleIngredient, targetSlot);
        if(!inputted){
          inserted=findAndBring(singleIngredient, targetSlot==5?6:5);
        }else{
          inserted=true;
        }
      }
    }
    if(!inserted){
      Ingredient catalyst = this.internalRecipe.getCatalyst();
      ItemStack stackInSlot = itemHandler.getStackInSlot(7);
      if(!catalyst.test(stackInSlot)){
        if(stackInSlot.isEmpty()){
          inserted = findAndBring(catalyst, 7);
        }else{
          for(int i=10;i<19;i++){
            if(itemHandler.getStackInSlot(i).isEmpty()){
              itemHandler.setStackInSlot(i, stackInSlot);
              itemHandler.setStackInSlot(7, ItemStack.EMPTY);
            }
          }
        }
      }
    }
  }
  private boolean findAndBring(Ingredient ingredient, int slotTo){
    ItemStack stackInReceiveSlot = itemHandler.getStackInSlot(slotTo); 
    for(int i=10;i<19;i++){
      ItemStack stackInSlot = itemHandler.getStackInSlot(i);
      if(ingredient.test(stackInSlot)){
        if(ItemStackUtil.canAddStackPartially(stackInReceiveSlot, stackInSlot) >= 1){
          if(stackInReceiveSlot.isEmpty()){
            ItemStack newStack = stackInSlot.copy();
            newStack.setCount(1);
            itemHandler.setStackInSlot(slotTo, newStack);
          }else{
            stackInReceiveSlot.grow(1);
          }
          stackInSlot.shrink(1);
          return true;
        }
      }
    }
    return false;
  }
  private boolean hasEnoughIngredients(ReagentMixingRecipe recipe){
    outer: for(IngredientWithCount ic : recipe.getInputs()){
      for(int i=5;i<7;i++){
        if(ic.test(itemHandler.getStackInSlot(i)))continue outer;
      }
      return false;
    }
    {
      Ingredient catalyst = recipe.getCatalyst();
      if(!catalyst.test(itemHandler.getStackInSlot(7))){
        return false;
      }
    }
    return true;
  }
  private boolean canInsert(ReagentMixingRecipe recipe){
    return ItemStackUtil.canAddStack(itemHandler.getStackInSlot(8), recipe.getOutputA())
        && ItemStackUtil.canAddStack(itemHandler.getStackInSlot(9), recipe.getOutputB());
  }
  private void consume(ReagentMixingRecipe recipe){
    for(IngredientWithCount ic : recipe.getInputs()){
      for(int i=5;i<=7;i++){
        ItemStack stack = itemHandler.getStackInSlot(i);
        if(ic.test(stack)){
          itemHandler.extractItem(i, ic.getCount(), false);
          break;
        }
      }
    }
    if(!recipe.getCatalyst().isEmpty()){
      ItemStack stack = itemHandler.getStackInSlot(7);
      itemHandler.setStackInSlot(7, ItemStackUtil.getDamagedItem(stack, 1, () -> ItemStack.EMPTY));
    }
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
  @Override
  public LampStates getLampStates() {
    return lamphelper.getLampStates();
  }
  @Override
  public void receivePacket(LampStates states) {
    lamphelper.receivePacket(states);
  }
  
}
