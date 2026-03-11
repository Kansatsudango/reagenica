package kandango.reagenica.enchantment;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.family.ChemiToolTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;

public class AntiPoisonEnchantment extends Enchantment{
  public AntiPoisonEnchantment() {
    super(Rarity.RARE, ChemiEnchantments.IRIDIUM_ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
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

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @Override
  protected boolean checkCompatibility(@Nonnull Enchantment other) {
    return super.checkCompatibility(other)
            && other != ChemiEnchantments.CHAIN_MINING.get();
  }

  public static MobEffectInstance run(LivingEntity entity, MobEffectInstance effect, int enchLevel){
    if(enchLevel<=0){
      return effect;
    }else{
      int oldDuration = effect.getDuration();
      return new MobEffectInstance(effect.getEffect(), oldDuration/(enchLevel+1), effect.getAmplifier(),
                  effect.isAmbient(), effect.isVisible(), effect.showIcon());
    }
  }


}
