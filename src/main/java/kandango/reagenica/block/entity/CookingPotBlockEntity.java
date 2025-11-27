package kandango.reagenica.block.entity;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.item.reagent.LiquidKitchen;
import kandango.reagenica.recipes.CookingRecipe;
import kandango.reagenica.screen.CookingPotMenu;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CookingPotBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity{
  private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        changed=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        else if(slot==7) return stack.is(Items.BOWL);
        else if(slot==8) return false;
        else if(slot==9) return false;
        return true;
      }
    };
  private int progress=0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private int fuel=0;
  public int getFuel(){return fuel;}
  public void setFuel(int p){this.fuel=p;}
  private int maxfuel=0;
  public int getMaxFuel(){return maxfuel;}
  public void setMaxFuel(int p){this.maxfuel=p;}
  private boolean changed=true;//Always be changed when loaded newly. also cooking pot should not be 'dirty'.
  private CookingRecipe recipecache;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler)
                                                                          .customInputRule(SlotPriorityRule.single(stack -> stack.is(Items.BOWL), 7))
                                                                          .fuelslot(6).outputslot(8,9).build());

  public CookingPotBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.COOKING_POT.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    this.progress = tag.getInt("Progress");
    this.fuel = tag.getInt("Fuel");
    this.maxfuel = tag.getInt("MaxFuel");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    tag.putInt("Progress", progress);
    tag.putInt("Fuel", fuel);
    tag.putInt("MaxFuel", maxfuel);
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
    return new CookingPotMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.cooking_pot");
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
    if(changed){
      SimpleContainer container = new SimpleContainer(6);
      for(int i=0;i<6;i++){
        container.setItem(i, this.itemHandler.getStackInSlot(i));
      }
      recipecache = CookingRecipe.getRecipe(container, lv).orElse(null);
      if(recipecache!=null && !recipeInsertable(recipecache)){
        recipecache=null;
      }
      changed=false;
    }
    if(fuel>0){
      fuel--;
      if((fuel&0x1)!=0 && lv instanceof ServerLevel slv){
        slv.sendParticles(ParticleTypes.FLAME,
        worldPosition.getX() + 0.5,
        worldPosition.getY() + 0.13,
        worldPosition.getZ() + 0.5,
        1,0.2,0,0.2,0.0);
      }
    }
    if(fuel==0){
      if(recipecache!=null){
        ItemStack fuelslot = itemHandler.getStackInSlot(6);
        int burn = ForgeHooks.getBurnTime(fuelslot, RecipeType.SMELTING);
        if(burn!=0){
          this.fuel=burn;
          this.maxfuel=burn;
          itemHandler.extractItem(6, 1, false);
        }
      }
    }
    if(fuel>0 && recipecache != null){
      this.progress++;
      if(progress>=200){
        this.progress=0;
        List<Ingredient> ingredients = recipecache.getInputs();
        int testtubes = consumeIngredient(ingredients,false);
        ItemStack dinner = recipecache.getOutput();
        ItemStackUtil.addStackToSlot(itemHandler, 9, dinner);
        ItemStackUtil.addStackToSlot(itemHandler, 8, new ItemStack(ChemiItems.TESTTUBE.get(), testtubes));
        itemHandler.extractItem(7, recipecache.getOutputCount(), false);
        this.changed=true;
      }
    }
  }
  private int consumeIngredient(List<Ingredient> ingredients,boolean simulate){
    int testtubes = 0;
    for(Ingredient ingredient : ingredients){
      for(int i=0;i<6;i++){
        ItemStack stack = itemHandler.getStackInSlot(i);
        if(ingredient.test(stack)){
          if(!simulate)itemHandler.extractItem(i, 1, false);
          if(stack.getItem() instanceof LiquidKitchen)testtubes++;
          break;
        }
      }
    }
    return testtubes;
  }
  private boolean recipeInsertable(CookingRecipe recipe){
    ItemStack out = recipe.getOutput();
    int testtubes = consumeIngredient(recipe.getInputs(), true);
    ItemStack bowls = itemHandler.getStackInSlot(7);
    boolean canOutput = ItemStackUtil.canAddStack(itemHandler.getStackInSlot(9), out);
    boolean canReturn = ItemStackUtil.canAddStack(itemHandler.getStackInSlot(8), new ItemStack(ChemiItems.TESTTUBE.get(), testtubes));
    boolean hasBowls = (bowls.getItem() == Items.BOWL) && bowls.getCount()>=recipe.getOutputCount();
    return canOutput && canReturn && hasBowls;
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }
}
