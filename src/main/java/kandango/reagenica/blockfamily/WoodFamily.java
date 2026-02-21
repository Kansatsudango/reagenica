package kandango.reagenica.blockfamily;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.generator.BlockLootType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.PressurePlateBlock.Sensitivity;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.RegistryObject;

public class WoodFamily {
  public final RegistryObject<RotatedPillarBlock> LOG;
  public final RegistryObject<LeavesBlock> LEAVES;
  public final RegistryObject<SaplingBlock> SAPLING;
  public final RegistryObject<Block> PLANKS;
  public final RegistryObject<StairBlock> STAIRS;
  public final RegistryObject<SlabBlock> SLAB;
  public final RegistryObject<RotatedPillarBlock> STRIPPED_LOG;
  public final RegistryObject<FenceBlock> FENCE;
  public final RegistryObject<FenceGateBlock> FENCE_GATE;
  public final RegistryObject<TrapDoorBlock> TRAPDOOR;
  public final RegistryObject<DoorBlock> DOOR;
  public final RegistryObject<ButtonBlock> BUTTON;
  public final RegistryObject<PressurePlateBlock> PRESSURE_PLATE;
  public final RegistryObject<BlockItem> LOG_ITEM;
  public final RegistryObject<BlockItem> LEAVES_ITEM;
  public final RegistryObject<BlockItem> SAPLING_ITEM;
  public final RegistryObject<BlockItem> PLANKS_ITEM;
  public final RegistryObject<BlockItem> STAIRS_ITEM;
  public final RegistryObject<BlockItem> SLAB_ITEM;
  public final RegistryObject<BlockItem> STRIPPED_LOG_ITEM;
  public final RegistryObject<BlockItem> FENCE_ITEM;
  public final RegistryObject<BlockItem> FENCE_GATE_ITEM;
  public final RegistryObject<BlockItem> TRAPDOOR_ITEM;
  public final RegistryObject<BlockItem> DOOR_ITEM;
  public final RegistryObject<BlockItem> BUTTON_ITEM;
  public final RegistryObject<BlockItem> PRESSURE_PLATE_ITEM;
  public final String name;
  public final BlockSetType setType;
  public WoodFamily(String name, Supplier<AbstractTreeGrower> grower){
    this.name = name;
    this.setType = BlockSetType.register(new BlockSetType(name));
    this.LOG = ChemiBlocks.BLOCKS.register(name+"_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    this.LOG_ITEM = ChemiBlocks.ITEMS.register(name+"_log", () -> new BlockItem(LOG.get(), new Item.Properties()));
    this.LEAVES = ChemiBlocks.BLOCKS.register(name+"_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    this.LEAVES_ITEM = ChemiBlocks.ITEMS.register(name+"_leaves", () -> new BlockItem(LEAVES.get(), new Item.Properties()));
    this.SAPLING = ChemiBlocks.BLOCKS.register(name+"_sapling", () -> new SaplingBlock(grower.get(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    this.SAPLING_ITEM = ChemiBlocks.ITEMS.register(name+"_sapling", () -> new BlockItem(SAPLING.get(), new Item.Properties()));
    this.PLANKS = ChemiBlocks.BLOCKS.register(name+"_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    this.PLANKS_ITEM = ChemiBlocks.ITEMS.register(name+"_planks", () -> new BlockItem(PLANKS.get(), new Item.Properties()));
    this.STAIRS = ChemiBlocks.BLOCKS.register(name+"_stairs", () -> new StairBlock(() -> this.PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    this.STAIRS_ITEM = ChemiBlocks.ITEMS.register(name+"_stairs", () -> new BlockItem(STAIRS.get(), new Item.Properties()));
    this.SLAB = ChemiBlocks.BLOCKS.register(name+"_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    this.SLAB_ITEM = ChemiBlocks.ITEMS.register(name+"_slab", () -> new BlockItem(SLAB.get(), new Item.Properties()));
    this.STRIPPED_LOG = ChemiBlocks.BLOCKS.register("stripped_"+name+"_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    this.STRIPPED_LOG_ITEM = ChemiBlocks.ITEMS.register("stripped_"+name+"_log", () -> new BlockItem(STRIPPED_LOG.get(), new Item.Properties()));
    this.FENCE = ChemiBlocks.BLOCKS.register(name+"_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    this.FENCE_ITEM = ChemiBlocks.ITEMS.register(name+"_fence", () -> new BlockItem(FENCE.get(), new Item.Properties()));
    this.FENCE_GATE = ChemiBlocks.BLOCKS.register(name+"_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),SoundEvents.FENCE_GATE_OPEN,SoundEvents.FENCE_GATE_CLOSE));
    this.FENCE_GATE_ITEM = ChemiBlocks.ITEMS.register(name+"_fence_gate", () -> new BlockItem(FENCE_GATE.get(), new Item.Properties()));
    this.TRAPDOOR = ChemiBlocks.BLOCKS.register(name+"_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), setType));
    this.TRAPDOOR_ITEM = ChemiBlocks.ITEMS.register(name+"_trapdoor", () -> new BlockItem(TRAPDOOR.get(), new Item.Properties()));
    this.DOOR = ChemiBlocks.BLOCKS.register(name+"_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), setType));
    this.DOOR_ITEM = ChemiBlocks.ITEMS.register(name+"_door", () -> new BlockItem(DOOR.get(), new Item.Properties()));
    this.BUTTON = ChemiBlocks.BLOCKS.register(name+"_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), setType, 30, true));
    this.BUTTON_ITEM = ChemiBlocks.ITEMS.register(name+"_button", () -> new BlockItem(BUTTON.get(), new Item.Properties()));
    this.PRESSURE_PLATE = ChemiBlocks.BLOCKS.register(name+"_pressure_plate", () -> new PressurePlateBlock(Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), setType));
    this.PRESSURE_PLATE_ITEM = ChemiBlocks.ITEMS.register(name+"_pressure_plate", () -> new BlockItem(PRESSURE_PLATE.get(), new Item.Properties()));
  }
  public void addLootTable(List<? super BlockLootType> list){
    list.add(BlockLootType.wood(LOG));
    list.add(BlockLootType.silkhoes(LEAVES));
    list.add(BlockLootType.normal(SAPLING));
    list.add(BlockLootType.wood(PLANKS));
    list.add(BlockLootType.wood(STAIRS));
    list.add(BlockLootType.wood(SLAB));
    list.add(BlockLootType.wood(STRIPPED_LOG));
    list.add(BlockLootType.wood(FENCE));
    list.add(BlockLootType.wood(FENCE_GATE));
    list.add(BlockLootType.wood(TRAPDOOR));
    list.add(BlockLootType.wood(DOOR));
    list.add(BlockLootType.wood(BUTTON));
    list.add(BlockLootType.wood(PRESSURE_PLATE));
  }
  public Stream<RegistryObject<BlockItem>> blockItems(){
    return Stream.of(LOG_ITEM, LEAVES_ITEM, SAPLING_ITEM, PLANKS_ITEM, STAIRS_ITEM, SLAB_ITEM, STRIPPED_LOG_ITEM,
       FENCE_ITEM, FENCE_GATE_ITEM, TRAPDOOR_ITEM, DOOR_ITEM, BUTTON_ITEM, PRESSURE_PLATE_ITEM);
  }
}
