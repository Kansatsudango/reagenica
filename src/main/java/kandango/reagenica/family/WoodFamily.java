package kandango.reagenica.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.ChemiLogBlock;
import kandango.reagenica.block.sign.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class WoodFamily {
  public static final List<WoodFamily> Woods = Collections.synchronizedList(new ArrayList<>());
  public final DeferredBlock<ChemiLogBlock> LOG;
  public final DeferredBlock<ChemiLogBlock> WOOD;
  public final DeferredBlock<LeavesBlock> LEAVES;
  public final DeferredBlock<SaplingBlock> SAPLING;
  public final DeferredBlock<FlowerPotBlock> POTTED_SAPLING;
  public final DeferredBlock<Block> PLANKS;
  public final DeferredBlock<StairBlock> STAIRS;
  public final DeferredBlock<SlabBlock> SLAB;
  public final DeferredBlock<RotatedPillarBlock> STRIPPED_LOG;
  public final DeferredBlock<RotatedPillarBlock> STRIPPED_WOOD;
  public final DeferredBlock<ChemiStandingSignBlock> STANDING_SIGN;
  public final DeferredBlock<ChemiWallSignBlock> WALL_SIGN;
  public final DeferredBlock<ChemiCeilingHangingSignBlock> CEILING_HANGING_SIGN;
  public final DeferredBlock<ChemiWallHangingSignBlock> WALL_HANGING_SIGN;
  public final DeferredBlock<FenceBlock> FENCE;
  public final DeferredBlock<FenceGateBlock> FENCE_GATE;
  public final DeferredBlock<TrapDoorBlock> TRAPDOOR;
  public final DeferredBlock<DoorBlock> DOOR;
  public final DeferredBlock<ButtonBlock> BUTTON;
  public final DeferredBlock<PressurePlateBlock> PRESSURE_PLATE;
  public final DeferredItem<BlockItem> LOG_ITEM;
  public final DeferredItem<BlockItem> WOOD_ITEM;
  public final DeferredItem<BlockItem> LEAVES_ITEM;
  public final DeferredItem<BlockItem> SAPLING_ITEM;
  public final DeferredItem<BlockItem> PLANKS_ITEM;
  public final DeferredItem<BlockItem> STAIRS_ITEM;
  public final DeferredItem<BlockItem> SLAB_ITEM;
  public final DeferredItem<BlockItem> STRIPPED_LOG_ITEM;
  public final DeferredItem<BlockItem> STRIPPED_WOOD_ITEM;
  public final DeferredItem<SignItem> SIGN_ITEM;
  public final DeferredItem<HangingSignItem> HANGING_SIGN_ITEM;
  public final DeferredItem<BlockItem> FENCE_ITEM;
  public final DeferredItem<BlockItem> FENCE_GATE_ITEM;
  public final DeferredItem<BlockItem> TRAPDOOR_ITEM;
  public final DeferredItem<BlockItem> DOOR_ITEM;
  public final DeferredItem<BlockItem> BUTTON_ITEM;
  public final DeferredItem<BlockItem> PRESSURE_PLATE_ITEM;
  public final String name;
  public final BlockSetType setType;
  public final WoodType woodType;
  public WoodFamily(String name, TreeGrower grower){
    this.name = name;
    this.setType = BlockSetType.register(new BlockSetType(name));
    this.woodType = WoodType.register(new WoodType("reagenica:"+name, setType));
    this.STRIPPED_LOG = ChemiBlocks.BLOCKS.register("stripped_"+name+"_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    this.STRIPPED_LOG_ITEM = ChemiBlocks.ITEMS.register("stripped_"+name+"_log", () -> new BlockItem(STRIPPED_LOG.get(), new Item.Properties()));
    this.STRIPPED_WOOD = ChemiBlocks.BLOCKS.register("stripped_"+name+"_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    this.STRIPPED_WOOD_ITEM = ChemiBlocks.ITEMS.register("stripped_"+name+"_wood", () -> new BlockItem(STRIPPED_WOOD.get(), new Item.Properties()));
    this.LOG = ChemiBlocks.BLOCKS.register(name+"_log", () -> new ChemiLogBlock(this.STRIPPED_LOG.get(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    this.LOG_ITEM = ChemiBlocks.ITEMS.register(name+"_log", () -> new BlockItem(LOG.get(), new Item.Properties()));
    this.WOOD = ChemiBlocks.BLOCKS.register(name+"_wood", () -> new ChemiLogBlock(this.STRIPPED_WOOD.get(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    this.WOOD_ITEM = ChemiBlocks.ITEMS.register(name+"_wood", () -> new BlockItem(WOOD.get(), new Item.Properties()));
    this.LEAVES = ChemiBlocks.BLOCKS.register(name+"_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    this.LEAVES_ITEM = ChemiBlocks.ITEMS.register(name+"_leaves", () -> new BlockItem(LEAVES.get(), new Item.Properties()));
    this.SAPLING = ChemiBlocks.BLOCKS.register(name+"_sapling", () -> new SaplingBlock(grower, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    this.SAPLING_ITEM = ChemiBlocks.ITEMS.register(name+"_sapling", () -> new BlockItem(SAPLING.get(), new Item.Properties()));
    this.POTTED_SAPLING = ChemiBlocks.BLOCKS.register("potted_"+name+"_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, SAPLING::get, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_DANDELION)));
    this.PLANKS = ChemiBlocks.BLOCKS.register(name+"_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    this.PLANKS_ITEM = ChemiBlocks.ITEMS.register(name+"_planks", () -> new BlockItem(PLANKS.get(), new Item.Properties()));
    this.STAIRS = ChemiBlocks.BLOCKS.register(name+"_stairs", () -> new StairBlock(this.PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    this.STAIRS_ITEM = ChemiBlocks.ITEMS.register(name+"_stairs", () -> new BlockItem(STAIRS.get(), new Item.Properties()));
    this.SLAB = ChemiBlocks.BLOCKS.register(name+"_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    this.SLAB_ITEM = ChemiBlocks.ITEMS.register(name+"_slab", () -> new BlockItem(SLAB.get(), new Item.Properties()));
    this.STANDING_SIGN = ChemiBlocks.BLOCKS.register(name+"_sign", () -> new ChemiStandingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN), woodType));
    this.WALL_SIGN = ChemiBlocks.BLOCKS.register(name+"_wall_sign", () -> new ChemiWallSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN), woodType));
    this.SIGN_ITEM = ChemiBlocks.ITEMS.register(name+"_sign", () -> new SignItem(new Item.Properties(), STANDING_SIGN.get(), WALL_SIGN.get()));
    this.CEILING_HANGING_SIGN = ChemiBlocks.BLOCKS.register(name+"_hanging_sign", () -> new ChemiCeilingHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN), woodType));
    this.WALL_HANGING_SIGN = ChemiBlocks.BLOCKS.register(name+"_wall_hanging_sign", () -> new ChemiWallHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN), woodType));
    this.HANGING_SIGN_ITEM = ChemiBlocks.ITEMS.register(name+"_hanging_sign", () -> new HangingSignItem(CEILING_HANGING_SIGN.get(), WALL_HANGING_SIGN.get(), new Item.Properties()));
    this.FENCE = ChemiBlocks.BLOCKS.register(name+"_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    this.FENCE_ITEM = ChemiBlocks.ITEMS.register(name+"_fence", () -> new BlockItem(FENCE.get(), new Item.Properties()));
    this.FENCE_GATE = ChemiBlocks.BLOCKS.register(name+"_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE),SoundEvents.FENCE_GATE_OPEN,SoundEvents.FENCE_GATE_CLOSE));
    this.FENCE_GATE_ITEM = ChemiBlocks.ITEMS.register(name+"_fence_gate", () -> new BlockItem(FENCE_GATE.get(), new Item.Properties()));
    this.TRAPDOOR = ChemiBlocks.BLOCKS.register(name+"_trapdoor", () -> new TrapDoorBlock(setType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    this.TRAPDOOR_ITEM = ChemiBlocks.ITEMS.register(name+"_trapdoor", () -> new BlockItem(TRAPDOOR.get(), new Item.Properties()));
    this.DOOR = ChemiBlocks.BLOCKS.register(name+"_door", () -> new DoorBlock(setType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    this.DOOR_ITEM = ChemiBlocks.ITEMS.register(name+"_door", () -> new BlockItem(DOOR.get(), new Item.Properties()));
    this.BUTTON = ChemiBlocks.BLOCKS.register(name+"_button", () -> new ButtonBlock(setType, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    this.BUTTON_ITEM = ChemiBlocks.ITEMS.register(name+"_button", () -> new BlockItem(BUTTON.get(), new Item.Properties()));
    this.PRESSURE_PLATE = ChemiBlocks.BLOCKS.register(name+"_pressure_plate", () -> new PressurePlateBlock(setType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    this.PRESSURE_PLATE_ITEM = ChemiBlocks.ITEMS.register(name+"_pressure_plate", () -> new BlockItem(PRESSURE_PLATE.get(), new Item.Properties()));
    Woods.add(this);
  }
  public Stream<DeferredItem<? extends BlockItem>> blockItems(){
    return Stream.of(LOG_ITEM, WOOD_ITEM, LEAVES_ITEM, SAPLING_ITEM, PLANKS_ITEM, STAIRS_ITEM, SLAB_ITEM,
       STRIPPED_LOG_ITEM, STRIPPED_WOOD_ITEM, SIGN_ITEM, HANGING_SIGN_ITEM,
       FENCE_ITEM, FENCE_GATE_ITEM, TRAPDOOR_ITEM, DOOR_ITEM, BUTTON_ITEM, PRESSURE_PLATE_ITEM);
  }
  public Stream<DeferredBlock<? extends Block>> blocks(){
    return Stream.of(LOG, WOOD, LEAVES, SAPLING, PLANKS, STAIRS, SLAB,
       STRIPPED_LOG, STRIPPED_WOOD, WALL_SIGN, STANDING_SIGN, WALL_HANGING_SIGN, CEILING_HANGING_SIGN,
       FENCE, FENCE_GATE, TRAPDOOR, DOOR, BUTTON, PRESSURE_PLATE);
  }
}
