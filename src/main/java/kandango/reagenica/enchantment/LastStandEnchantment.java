package kandango.reagenica.enchantment;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.family.ChemiToolTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

public class LastStandEnchantment extends Enchantment{
  public LastStandEnchantment() {
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
    return 3;
  }

  public static float calc(LivingEntity attacker, float damage, int enchLevel){
    float hpRatio = attacker.getHealth() / attacker.getMaxHealth();
    if(hpRatio<0.2f){
      return damage*(1+enchLevel*1.5f);
    }else if(hpRatio<0.5f){
      return damage*(1+enchLevel*0.5f);
    }else if(hpRatio<0.75f){
      return damage*(1+enchLevel*0.2f);
    }else{
      return damage;
    }
  }
}
