package kandango.reagenica.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class AntiPoisonEnchantment{
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
