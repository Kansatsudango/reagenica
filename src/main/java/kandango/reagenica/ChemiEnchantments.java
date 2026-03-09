package kandango.reagenica;

import kandango.reagenica.enchantment.BigMinerEnchantment;
import kandango.reagenica.enchantment.VeinMinerEnchantment;
import kandango.reagenica.family.ChemiToolTiers;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChemiEnchantments {
  public static final DeferredRegister<Enchantment> ENCHANTMENTS = 
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChemistryMod.MODID);
  
  public static final EnchantmentCategory IRIDIUM_DIGGER = 
    EnchantmentCategory.create("iridium_digger",
      item -> item instanceof DiggerItem digger &&
              digger.getTier() == ChemiToolTiers.IRIDIUM);
  
  public static final RegistryObject<Enchantment> BIG_MINING = 
      ENCHANTMENTS.register("big_mining",BigMinerEnchantment::new);
  public static final RegistryObject<Enchantment> CHAIN_MINING = 
      ENCHANTMENTS.register("chain_mining",VeinMinerEnchantment::new);
}
