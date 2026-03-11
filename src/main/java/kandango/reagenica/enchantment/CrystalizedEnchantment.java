package kandango.reagenica.enchantment;

import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.family.ChemiToolTiers;
import kandango.reagenica.family.CrystalFamily;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

public class CrystalizedEnchantment extends Enchantment{
  public CrystalizedEnchantment() {
    super(Rarity.RARE, ChemiEnchantments.IRIDIUM_WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
  }
  
  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    if(stack.getItem() instanceof SwordItem sword){
      Tier tier = sword.getTier();
      return tier == ChemiToolTiers.IRIDIUM;
    }else{
      return false;
    }
  }

  @Override
  public int getMaxLevel() {
    return 5;
  }

  public static Optional<ItemEntity> loot(LivingEntity entity, int enchLevel){
    RandomSource random = entity.getRandom();
    if(random.nextInt(100) < enchLevel+1){
      Item crystal = CrystalFamily.Crystals.get(random.nextInt(CrystalFamily.Crystals.size())).SHARD_ITEM.get();
      ItemStack drop = new ItemStack(crystal);
      ItemEntity item = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), drop);
      return Optional.of(item);
    }else{
      return Optional.empty();
    }
  }
}
