package kandango.reagenica.generator;

import java.util.List;
import java.util.Set;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.family.CrystalFamily;
import kandango.reagenica.family.WoodFamily;
import kandango.reagenica.generator.BlockLootType.BlockType;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ChemiLootTableProvider extends LootTableProvider{
  public ChemiLootTableProvider(PackOutput output) {
    super(output, Set.of(), List.of(
            new SubProviderEntry(ChemiBlockLootTables::new, LootContextParamSets.BLOCK)
    ));
  }
  private static class ChemiBlockLootTables extends BlockLootSubProvider{
    protected ChemiBlockLootTables(){
      super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate(){
      WoodFamily.Woods.forEach(this::processWood);
      CrystalFamily.Crystals.forEach(this::processCrystal);
      for(BlockLootType block : ChemiBlocks.listBlocks){
        if(block.type()==BlockType.NORMAL){
          dropSelf(block.blockreg().get());
        }else if(block.type()==BlockType.ORES){
          add(block.blockreg().get(), ore -> createOreDrop(ore, block.item().get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, block.count()))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)));
        }else if(block.type()==BlockType.MACHINE){
          add(block.blockreg().get(), machine -> createNameableBlockEntityTable(machine));
        }else if(block.type()==BlockType.MACHINE_SAVEENERGY){
          add(block.blockreg().get(), machine -> createNameableBlockEntityTable(machine).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Electric", "BlockEntityTag.Electric")));
        }else if(block.type()==BlockType.SILKTOUCH){
          add(block.blockreg().get(), glass -> createSilkTouchOnlyTable(glass));
        }else if(block.type()==BlockType.PLANTS){
          add(block.blockreg().get(), plant -> createCropDrops(plant, block.item().get(), block.item().get(), LootItemBlockStatePropertyCondition.hasBlockStateProperties(plant).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))));
        }else if(block.type()==BlockType.NONE){
          add(block.blockreg().get(), noDrop());
        }else if(block.type()==BlockType.MANUAL){
          add(block.blockreg().get(), noDrop());
        }
      }
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
      return ChemiBlocks.BLOCKS.getEntries().stream().map(e -> e.get()).toList();
    }
    private void processWood(WoodFamily family){
      dropSelf(family.LOG.get());
      dropSelf(family.WOOD.get());
      dropSelf(family.LEAVES.get());//dummy
      dropSelf(family.SAPLING.get());
      dropSelf(family.PLANKS.get());
      dropSelf(family.STAIRS.get());
      add(family.SLAB.get(),createSlabItemTable(family.SLAB.get()));
      dropSelf(family.STRIPPED_LOG.get());
      dropSelf(family.STRIPPED_WOOD.get());
      dropSelf(family.STANDING_SIGN.get());
      dropSelf(family.WALL_SIGN.get());
      dropSelf(family.CEILING_HANGING_SIGN.get());
      dropSelf(family.WALL_HANGING_SIGN.get());
      dropSelf(family.FENCE.get());
      dropSelf(family.FENCE_GATE.get());
      dropSelf(family.TRAPDOOR.get());
      add(family.DOOR.get(),createDoorTable(family.DOOR.get()));
      dropSelf(family.BUTTON.get());
      dropSelf(family.PRESSURE_PLATE.get());
    }
    private void processCrystal(CrystalFamily family){
      dropSelf(family.BLOCK.get());
      add(family.BUDDING_BLOCK.get(), noDrop());
      dropOtherWithFortuneUnlessSilktouch(family.CRYSTAL.get(), family.SHARD_ITEM.get());
      dropWhenSilkTouch(family.CRYSTAL_BUD.get());
    }

    protected void dropOtherWithFortuneUnlessSilktouch(Block block, Item drop){
      add(block,
        createSilkTouchDispatchTable(
            block,
            applyExplosionDecay(
                block,
                LootItem.lootTableItem(drop)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                  .apply(ApplyBonusCount.addOreBonusCount(
                        Enchantments.BLOCK_FORTUNE
                ))
            )
        )
    );
    }
  }
}
