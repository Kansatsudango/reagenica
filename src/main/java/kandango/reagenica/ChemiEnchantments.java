package kandango.reagenica;

import kandango.reagenica.enchantment.BigMinerEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChemiEnchantments {
  public static final DeferredRegister<Enchantment> ENCHANTMENTS = 
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChemistryMod.MODID);
  
  public static final RegistryObject<Enchantment> BIG_MINING = 
      ENCHANTMENTS.register("vein_mining",BigMinerEnchantment::new);
}
