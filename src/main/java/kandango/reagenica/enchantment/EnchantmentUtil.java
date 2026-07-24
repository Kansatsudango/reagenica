package kandango.reagenica.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EnchantmentUtil {
  public static Holder<Enchantment> getHolder(RegistryAccess access, ResourceKey<Enchantment> key) {
    return access.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
  }

  public static int getLevel(ItemStack stack, RegistryAccess access, ResourceKey<Enchantment> key) {
    return EnchantmentHelper.getTagEnchantmentLevel(getHolder(access, key), stack);
  }
}
