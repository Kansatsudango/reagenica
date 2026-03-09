package kandango.reagenica.enchantment;

import javax.annotation.Nonnull;

import kandango.reagenica.family.ChemiToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.state.BlockState;

public class BigMinerEnchantment extends Enchantment{
  public BigMinerEnchantment() {
    super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
  }
  
  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    if(stack.getItem() instanceof DiggerItem digger){
      Tier tier = digger.getTier();
      return tier == ChemiToolTiers.IRIDIUM;
    }else{
      return false;
    }
  }

  public int getMaxLevel() {
    return 2;
  }

  public static void run(ServerLevel slv, Player player, BlockPos origin, ItemStack tool, int enchLevel){
    for(int x=-enchLevel;x<=enchLevel;x++){
      for(int y=-enchLevel;y<=enchLevel;y++){
        for(int z=-enchLevel;z<=enchLevel;z++){
          BlockPos pos = origin.offset(x, y, z);
          if(!pos.equals(origin)){
            BlockState state = slv.getBlockState(pos);
            if(state.getDestroySpeed(slv, pos)<0)continue;
            if(!player.hasCorrectToolForDrops(state))continue;
            if(player instanceof ServerPlayer sp){
              sp.gameMode.destroyBlock(pos);
            }
          }
        }
      }
    }
  }
}
