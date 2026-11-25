package kandango.reagenica.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.recipes.StallTradingRecipe;
import kandango.reagenica.screen.TradingStallMenu;
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
import net.minecraft.world.entity.npc.Villager;
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
import net.minecraftforge.items.ItemStackHandler;

public class TradingStallBlockEntity extends BlockEntity implements MenuProvider{
  private final int SLOTCOUNT = 27;
  private final ItemStackHandler itemHandler = new ItemStackHandler(SLOTCOUNT) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
    }
  };
  public TradingStallBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.TRADING_STALL.get(), pos, state);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return LazyOptional.of(() -> itemHandler).cast();
    }
    return LazyOptional.empty();
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
  public void setChanged(){
    super.setChanged();
    final Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      lv.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
  }

  // パケット経由で同期
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
  
  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    CompoundTag tag = pkt.getTag();
    if(tag!=null){
      this.load(tag);
    }
  }
  
  // チャンク読み込み時同期
  @Override
  public CompoundTag getUpdateTag() {
    return this.saveWithoutMetadata();
  }
  
  @Override
  public void handleUpdateTag(CompoundTag tag) {
    this.load(tag);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.trading_stall");
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id,@Nonnull Inventory inv,@Nonnull Player player) {
    return new TradingStallMenu(id, inv, this);
  }

  public long trade(Villager villager, Level lv){
    int account = 0;
    List<RecipeSlot> validmerches = new ArrayList<>();
    Random rand = new Random();
    for(int i=0;i<SLOTCOUNT;i++){
      final int index = i;
      ItemStack item = itemHandler.getStackInSlot(index);
      SimpleContainer container = new SimpleContainer(1);
      container.setItem(0, item);
      Optional<StallTradingRecipe> mayberecipe =  lv.getRecipeManager().getRecipeFor(ModRecipes.STALL_TRADING_TYPE.get(), container, lv);
      mayberecipe.ifPresent(recipe -> validmerches.add(new RecipeSlot(recipe, index)));
    }
    for(RecipeSlot ri : validmerches){
      StallTradingRecipe recipe = ri.recipe();
      int index = ri.index();
      ItemStack merchandise = recipe.getMerchandise();
      ItemStack priceitem = recipe.getPrice();
      int price = priceitem.getCount();
      ItemStack merchstack = itemHandler.getStackInSlot(index);
      if(account+price+rand.nextInt(20) <= 10){
        this.doTrade(index, priceitem, merchandise.getCount(), merchstack.getCount());
        account += price;
        if(lv instanceof ServerLevel slev){
          slev.sendParticles(ParticleTypes.HAPPY_VILLAGER,
          villager.getX() + 0.5,
          villager.getY() + 1.5,
          villager.getZ() + 0.5,
          10,0.5,0.5,0.5,0.1);
        }
      }
    }
    if(account==0){
      return 200L+rand.nextInt(1200);
    }else if(account<=4){
      return 2400L+rand.nextInt(1200);
    }else{
      return 3600L+rand.nextInt(1200);
    }
  }
  private void doTrade(int slot, ItemStack price, int unit, int count){
    int multiplier = count/unit; // rounddown its price 
    if(multiplier<=0)throw new IllegalArgumentException("merchandise count is less than its unit");
    int perunit = price.getCount();
    ItemStack resultItemstack = price.copy();
    resultItemstack.setCount(perunit*multiplier);
    itemHandler.setStackInSlot(slot, resultItemstack);
  }

  private record RecipeSlot(StallTradingRecipe recipe, int index) {
  }
}
