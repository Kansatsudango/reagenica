package kandango.reagenica.event;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID)
public class LootTableInjector {
  @SubscribeEvent
  public static void onLootTableLoad(LootTableLoadEvent event){
    ResourceLocation name = event.getName();
    if(name.equals(new ResourceLocation("minecraft", "chests/stronghold_corridor"))){
      LootTable table = event.getTable();
      LootPool pool = LootPool.lootPool()
      .setRolls(ConstantValue.exactly(1))
      .add(LootItem.lootTableItem(ChemiBlocks.GRAPE_SAPLING_ITEM.get()).setWeight(2)
        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
      .name("reagenica_grape_sapling").build();
      table.addPool(pool);
    }
    if(name.equals(new ResourceLocation("minecraft", "chests/abandoned_mineshaft"))){
      LootTable table = event.getTable();
      LootPool pool = LootPool.lootPool()
      .setRolls(ConstantValue.exactly(1))
      .add(LootItem.lootTableItem(ChemiBlocks.GRAPE_SAPLING_ITEM.get()).setWeight(1)
        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
      .add(LootItem.lootTableItem(ChemiBlocks.GRAPE.get()).setWeight(3))
      .name("reagenica_grape_sapling").build();
      table.addPool(pool);
    }
    if(name.equals(new ResourceLocation("minecraft", "chests/simple_dungeon"))){
      LootTable table = event.getTable();
      LootPool pool = LootPool.lootPool()
      .setRolls(ConstantValue.exactly(1))
      .add(LootItem.lootTableItem(ChemiBlocks.GRAPE_SAPLING_ITEM.get()).setWeight(1)
        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
      .add(LootItem.lootTableItem(Blocks.AIR).setWeight(2))
      .name("reagenica_grape_sapling").build();
      table.addPool(pool);
    }
  }
}
