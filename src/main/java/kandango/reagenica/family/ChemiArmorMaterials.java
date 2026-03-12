package kandango.reagenica.family;

import java.util.EnumMap;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum ChemiArmorMaterials implements ArmorMaterial{
  PLATINUM("reagenica:platinum", 40, defences(4, 6, 8, 4),
    12, SoundEvents.ARMOR_EQUIP_GOLD, 2.0f, 0.1f, 
    () -> Ingredient.of(ChemiItems.PLATINUM_INGOT.get()) 
  ),
  IRIDIUM("reagenica:iridium", 70, defences(4, 6, 8, 4),
    15, SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0f, 0.15f, 
    () -> Ingredient.of(ChemiItems.IRIDIUM_INGOT.get())
  ),
  PINK_GOLD("reagenica:pink_gold", 14, defences(3, 5, 7, 3),
    25, SoundEvents.ARMOR_EQUIP_GOLD, 0.75f, 0.0f, 
    () -> Ingredient.of(ChemiItems.PINK_GOLD_INGOT.get())
  );
	private final String name;
	private final int durability;
	private final EnumMap<ArmorItem.Type, Integer> defence;
	private final int enchantability;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final Supplier<Ingredient> repairIngredient;
  

  //EnumMap from net.minecraft.world.item.ArmorMaterials
  private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266653_) -> {
      p_266653_.put(ArmorItem.Type.BOOTS, 13);
      p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
      p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
      p_266653_.put(ArmorItem.Type.HELMET, 11);
  });
  private ChemiArmorMaterials(String name, int durability, EnumMap<Type,Integer> defences, int enchantability,
     SoundEvent sound, float toughness, float kbres, Supplier<Ingredient> ingredient) {
    this.name = name;
    this.durability = durability;
    this.defence = defences;
    this.enchantability = enchantability;
    this.sound = sound;
    this.toughness = toughness;
    this.knockbackResistance = kbres;
    this.repairIngredient = ingredient;
  }
  private static EnumMap<ArmorItem.Type, Integer> defences(int boots, int leggings, int chestplate, int helmet){
    return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
      map.put(ArmorItem.Type.BOOTS, boots);
      map.put(ArmorItem.Type.LEGGINGS, leggings);
      map.put(ArmorItem.Type.CHESTPLATE, chestplate);
      map.put(ArmorItem.Type.HELMET, helmet);
    });
  }
  @Override
  public int getDurabilityForType(@Nonnull Type type) {
    return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durability;
  }

  @Override
  public int getDefenseForType(@Nonnull Type type) {
    return defence.get(type);
  }

  @Override
  public int getEnchantmentValue() {
    return enchantability;
  }

  @Override
  public SoundEvent getEquipSound() {
    return sound;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return repairIngredient.get();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public float getToughness() {
    return toughness;
  }

  @Override
  public float getKnockbackResistance() {
    return knockbackResistance;
  }
  
}
