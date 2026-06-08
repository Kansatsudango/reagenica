package kandango.reagenica.enchantment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.world.ChemiCapabilities;
import kandango.reagenica.world.task.SeedPlacingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class GardenerEnchantment extends Enchantment{
  public GardenerEnchantment() {
    super(Rarity.RARE, ChemiEnchantments.HOES, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
  }
  
  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return stack.getItem() instanceof HoeItem;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }
  @Override
  public boolean isTreasureOnly() {
    return false;
  }
  @Override
  public boolean isTradeable() {
    return true;
  }

  @Override
  protected boolean checkCompatibility(@Nonnull Enchantment other) {
    return true;
  }

  public static void run(ServerLevel slv, ServerPlayer player, BlockPos pos, ItemStack tool){
    BlockState state = slv.getBlockState(pos);
    BlockState defaultState = state.getBlock().defaultBlockState();
    if(state.getBlock() instanceof CropBlock crop){
      ItemStack seed = crop.getCloneItemStack(slv, pos, state);
      List<ItemStack> invItems = new ArrayList<>();
      Inventory inv = player.getInventory();
      invItems.addAll(inv.items);
      invItems.addAll(inv.offhand);
      invItems.addAll(inv.armor);
      List<ItemStack> bags = invItems.stream().filter(stack -> stack.is(ChemiItems.FARMING_BAG.get())).toList();
      for(ItemStack bag : bags){
        LazyOptional<IItemHandler> mayhandler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if(mayhandler.isPresent()){
          IItemHandler handler = mayhandler.orElseThrow(() -> new IllegalStateException());
          for(int i=0;i<handler.getSlots();i++){
            ItemStack stack = handler.getStackInSlot(i);
            if(stack.is(seed.getItem())){
              handler.extractItem(i, 1, false);
              putTask(slv, new SeedPlacingTask(player, pos, defaultState));
              return;
            }
          }
        }else{
          ChemistryMod.LOGGER.warn("Farming bag itemhandler capability not found");
        }
      }
      for(ItemStack stack : invItems){
        if(stack.is(seed.getItem())){
          stack.shrink(1);
          putTask(slv, new SeedPlacingTask(player, pos, defaultState));
          return;
        }
      }
    }
  }
  private static void putTask(ServerLevel slv, SeedPlacingTask task){
    @Nullable final ServerLevel overworld = slv.getServer().getLevel(Level.OVERWORLD);
    if(overworld!=null){
      overworld.getCapability(ChemiCapabilities.SEED_PLACING_DATA).ifPresent(data -> data.add(slv, task));
    }
  }
}
