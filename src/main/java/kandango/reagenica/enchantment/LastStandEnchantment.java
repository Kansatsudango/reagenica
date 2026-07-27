package kandango.reagenica.enchantment;

import net.minecraft.world.entity.LivingEntity;

public class LastStandEnchantment{
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
