package kandango.reagenica.item;

import kandango.reagenica.enchantment.EnchantmentUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class EnchantmentBookAmulet extends Amulet{
  public EnchantmentBookAmulet(ResourceKey<Enchantment> ench, NumberProvider lvl) {
    super(rg -> enchLoot(rg, ench, lvl));
  }
  private static LootTable enchLoot(RegistryAccess rg, ResourceKey<Enchantment> ench, NumberProvider lvl){
    LootItemConditionalFunction.Builder<?> enchFunc = new SetEnchantmentsFunction.Builder().withEnchantment(EnchantmentUtil.getHolder(rg, ench), lvl);
    LootPool.Builder pool = LootPool.lootPool().add(LootItem.lootTableItem(Items.BOOK).apply(enchFunc).setWeight(1)).setRolls(ConstantValue.exactly(1));
    return LootTable.lootTable().withPool(pool).build();
  }
}
