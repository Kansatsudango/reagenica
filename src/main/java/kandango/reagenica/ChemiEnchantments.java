package kandango.reagenica;

import kandango.reagenica.enchantment.AntiPoisonEnchantment;
import kandango.reagenica.enchantment.BigMinerEnchantment;
import kandango.reagenica.enchantment.CrystalizedEnchantment;
import kandango.reagenica.enchantment.GardenerEnchantment;
import kandango.reagenica.enchantment.LastStandEnchantment;
import kandango.reagenica.enchantment.VeinMinerEnchantment;
import kandango.reagenica.family.ChemiArmorMaterials;
import kandango.reagenica.family.ChemiToolTiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiEnchantments {
  public static final DeferredRegister<Enchantment> ENCHANTMENTS = 
      DeferredRegister.create(Registries.ENCHANTMENT, ChemistryMod.MODID);
  
  public static final EnchantmentCategory IRIDIUM_DIGGER = 
    EnchantmentCategory.create("iridium_digger_reagenica",
      item -> item instanceof DiggerItem digger &&
              digger.getTier() == ChemiToolTiers.IRIDIUM);
  public static final EnchantmentCategory IRIDIUM_ARMOR = 
    EnchantmentCategory.create("iridium_armor_reagenica",
      item -> item instanceof ArmorItem armor &&
              armor.getMaterial() == ChemiArmorMaterials.IRIDIUM);
  public static final EnchantmentCategory IRIDIUM_WEAPON = 
    EnchantmentCategory.create("iridium_weapon_reagenica",
      item -> item instanceof SwordItem sword &&
              sword.getTier() == ChemiToolTiers.IRIDIUM);
  public static final EnchantmentCategory HOES = 
    EnchantmentCategory.create("hoes_reagenica",
      item -> item instanceof HoeItem);
  
  public static final DeferredHolder<Enchantment> BIG_MINING = 
      ENCHANTMENTS.register("big_mining",BigMinerEnchantment::new);
  public static final DeferredHolder<Enchantment> CHAIN_MINING = 
      ENCHANTMENTS.register("chain_mining",VeinMinerEnchantment::new);
  public static final DeferredHolder<Enchantment> ANTI_POISON = 
      ENCHANTMENTS.register("anti_poison",AntiPoisonEnchantment::new);
  public static final DeferredHolder<Enchantment> LAST_STAND = 
      ENCHANTMENTS.register("last_stand",LastStandEnchantment::new);
  public static final DeferredHolder<Enchantment> CRYSTALIZED = 
      ENCHANTMENTS.register("crystalized",CrystalizedEnchantment::new);
  public static final DeferredHolder<Enchantment> GARDENER = 
      ENCHANTMENTS.register("gardener",GardenerEnchantment::new);
}
