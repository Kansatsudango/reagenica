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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
  
  public static final ResourceKey<Enchantment> BIG_MINING = 
      create("big_mining");
  public static final ResourceKey<Enchantment> CHAIN_MINING = 
      create("chain_mining");
  public static final ResourceKey<Enchantment> ANTI_POISON = 
      create("anti_poison");
  public static final ResourceKey<Enchantment> LAST_STAND = 
      create("last_stand");
  public static final ResourceKey<Enchantment> CRYSTALIZED = 
      create("crystalized");
  public static final ResourceKey<Enchantment> GARDENER = 
      create("gardener");

  private static ResourceKey<Enchantment> create(String id) {
    return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID,id));
  }
}
