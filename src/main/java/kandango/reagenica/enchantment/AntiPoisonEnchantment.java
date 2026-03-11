package kandango.reagenica.enchantment;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.family.ChemiArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class AntiPoisonEnchantment extends Enchantment{
  public AntiPoisonEnchantment() {
    super(Rarity.RARE, ChemiEnchantments.IRIDIUM_ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
  }
  
  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    if(stack.getItem() instanceof ArmorItem armor){
      ArmorMaterial material = armor.getMaterial();
      return material == ChemiArmorMaterials.IRIDIUM;
    }else{
      return false;
    }
  }

  @Override
  public int getMaxLevel() {
    return 3;
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
