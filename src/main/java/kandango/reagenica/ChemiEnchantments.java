package kandango.reagenica;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ChemiEnchantments {
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
