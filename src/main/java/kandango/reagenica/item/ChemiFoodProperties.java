package kandango.reagenica.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ChemiFoodProperties {
  public static final FoodProperties GOHAN = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(1.2f)
            .fast()
            .build();
  public static final FoodProperties ONION_SOUP = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .build();
  public static final FoodProperties BEANS_SOUP = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .build();
  public static final FoodProperties TOMATO_SOUP = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .build();
  public static final FoodProperties CORN_SOUP = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .build();
  public static final FoodProperties CORN_BREAD = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0f)
            .build();
  public static final FoodProperties RAISIN_BREAD = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0f)
            .build();
  public static final FoodProperties MINESTRONE = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties POT_AU_FEU = new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(1.5f)
            .build();
  public static final FoodProperties SEASONED_GOHAN = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 2400, 0), 1.0f)
            .alwaysEat()
            .fast()
            .build();
  public static final FoodProperties NIKUJAGA = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties CHIKUZENNI = new FoodProperties.Builder()
            .nutrition(9)
            .saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties MISO_SOUP = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(1.0f)
            .fast()
            .build();
  public static final FoodProperties OYAKODON = new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(1.5f)
            .build();
  public static final FoodProperties CREAM_STEW = new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(1.5f)
            .build();
  public static final FoodProperties PORK_ONION = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.6f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 0), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties CHICKEN_SAUTE = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties CHICKEN_SAUTE_POTATO = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(2.0f)
            .build();
  public static final FoodProperties ROASTED_SOYBEANS = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5f)
            .fast()
            .build();
  public static final FoodProperties COOKED_ONION = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(1.2f)
            .build();
  public static final FoodProperties POPCORN = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(1.2f)
            .build();
  public static final FoodProperties BEEF_STEW = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties MEAT_WINE = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.8f)
            .build();
  public static final FoodProperties MEATSAUCE_PASTA = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.6f)
            .alwaysEat()
            .fast()
            .build();
  public static final FoodProperties SUMESHI = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(1.2f)
            .fast()
            .build();
  public static final FoodProperties SUSHI = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(1.2f)
            .fast()
            .build();
  public static final FoodProperties SUSHI_SALMON = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(1.2f)
            .fast()
            .build();
  public static final FoodProperties SUSHI_SEABREAM = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.2f)
            .fast()
            .build();
  public static final FoodProperties NANBANZUKE = new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(1.4f)
            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 0), 1.0f)
            .alwaysEat()
            .build();
  public static final FoodProperties SALMON_MARINADE = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.4f)
            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 0), 1.0f)
            .alwaysEat()
            .build();
}
