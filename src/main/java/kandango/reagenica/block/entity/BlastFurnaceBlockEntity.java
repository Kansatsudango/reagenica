package kandango.reagenica.block.entity;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.block.entity.util.LitUtil;
import kandango.reagenica.recipes.BlastFurnaceRecipe;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.screen.BlastFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class BlastFurnaceBlockEntity extends BlockEntity implements MenuProvider,IBlockEntityWithSlider,ITickableBlockEntity{
  private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null)return false;
        else if(slot==0)return true;
        else if(slot==1)return stack.is(Items.COAL) || stack.is(Items.CHARCOAL);
        else return false;
      }
    };
 
  private boolean dirty=true;//Always dirty when loaded newly
  private int temperature = 200;//actually div10
  public void setTemp(int t){this.temperature = t;}
  public int getTemp(){return temperature;}
  private Optional<BlastFurnaceRecipe> cachedRecipe = Optional.empty();
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private int fuel = 0;
  public int getFuel(){return fuel;}
  public void setFuel(int p){this.fuel=p;}
  private int fuelmax = 1600;//炭ならどうせ1600
  public int getFuelMax(){return fuelmax;}
  public void setFuelMax(int p){this.fuelmax=p;}
  private int maxtemp=16000;
  public int getmaxTemp(){return maxtemp;}
  public void setmaxTemp(int p){this.maxtemp=p;}
  private float expSum = 0.0f;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).fuelslot(1).outputslot(2,3).build());
  
  public BlastFurnaceBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.BLAST_FURNACE.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    this.fuel=tag.getInt("Fuel");
    this.fuelmax=tag.getInt("MaxFuel");
    this.progress=tag.getInt("Progress");
    this.temperature=tag.getInt("Temperature");
    this.maxtemp=tag.getInt("MaxTemp");
    this.expSum=tag.getFloat("Exp");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    super.saveAdditional(tag);
    tag.putInt("Fuel", fuel);
    tag.putInt("MaxFuel", fuelmax);
    tag.putInt("Progress", progress);
    tag.putInt("Temperature", temperature);
    tag.putInt("MaxTemp", maxtemp);
    tag.putFloat("Exp", expSum);
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
    return new BlastFurnaceMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.blast_furnace");
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
    if(dirty){
      SimpleContainer container = new SimpleContainer(5);
      container.setItem(0, itemHandler.getStackInSlot(0));
      this.cachedRecipe = lv.getRecipeManager().getRecipeFor(ModRecipes.BLAST_FURNACE_TYPE.get(), container, lv);
      if(this.fuel==0){
        ItemStack fuelstack = this.itemHandler.getStackInSlot(1);
        if(fuelstack.is(ItemTags.create(new ResourceLocation("minecraft", "coals")))){
          this.fuel = ForgeHooks.getBurnTime(fuelstack, RecipeType.SMELTING);
          this.fuelmax = this.fuel;
          itemHandler.getStackInSlot(1).shrink(1);
          LitUtil.setLit(true, lv, worldPosition);
        }else{
          LitUtil.setLit(false, lv, worldPosition);
        }
      }
      dirty=false;
    }
    if(this.fuel>=1){
      this.fuel--;
      this.temperature = heatup(this.temperature);
      if(fuel==0)dirty=true;
    }else{
      this.temperature = cooldown(this.temperature);
    }
    if(this.cachedRecipe.isPresent()){
      BlastFurnaceRecipe recipe = this.cachedRecipe.orElseThrow();
      int minTemp = recipe.getMinTemp();
      int maxTemp = recipe.getMaxTemp();
      if(minTemp <= this.temperature && this.temperature <= maxTemp){
        int speed = (this.temperature - minTemp)*6000/(maxTemp - minTemp) + 4000;
        ItemStack output = recipe.getOutput();
        ItemStack byproduct = recipe.getByproduct();
        if(canInsert(output, byproduct)){
          this.progress+=speed;
          if(this.progress>=2000000){//200tick=10s at Max Speed
            this.progress=0;
            itemHandler.setStackInSlot(2, ItemStackUtil.addStack(this.itemHandler.getStackInSlot(2).copy(), output.copy()));
            itemHandler.setStackInSlot(3, ItemStackUtil.addStack(this.itemHandler.getStackInSlot(3).copy(), byproduct.copy()));
            itemHandler.extractItem(0, 1, false);
            this.expSum += recipe.getExp();
          }
        }
      }
    }
  }
  private int heatup(int current){
    int tm=0;
    if(current<6000){
      tm = current+10;
    }else if(current<10000){
      tm = current+5;
    }else if(current<12000){
      tm = current+2;
    }else if(current<16000){
      tm = current+1;
    }else{
      tm = 16000;
    }
    if(tm > this.maxtemp){
      return Math.max(current-5,maxtemp);
    }
    return tm;
  }
  private int cooldown(int current){
    return Math.max(current-1,200);
  }
  private boolean canInsert(ItemStack main, ItemStack sub){
    ItemStack resultslot = itemHandler.getStackInSlot(2);
    ItemStack byproductslot = itemHandler.getStackInSlot(3);
    return (resultslot.isEmpty() || ItemStackUtil.canAddStack(resultslot, main))
            && (byproductslot.isEmpty() || ItemStackUtil.canAddStack(byproductslot, sub));
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }
  @Override
  public void setSliderValue(double value) {
    maxtemp = (int)(value*16000);
    this.setChanged();
  }
  @Override
  public double getSliderValue() {
    return (double)this.maxtemp/16000.0;
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
  }

  public void awardExp(@Nonnull Player player){
    if(this.level instanceof ServerLevel slv){
      int award = (int)expSum;
      ExperienceOrb.award(slv, player.position(), award);
      expSum -= award;
    }
  }
}
