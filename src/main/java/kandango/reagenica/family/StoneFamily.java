package kandango.reagenica.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import kandango.reagenica.ChemiBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class StoneFamily {
  public static final List<StoneFamily> Stones = Collections.synchronizedList(new ArrayList<>());
  public final DeferredBlock<Block> STONE;
  public final DeferredBlock<StairBlock> STAIRS;
  public final DeferredBlock<SlabBlock> SLAB;
  public final DeferredBlock<WallBlock> WALL;
  public final DeferredBlock<Block> P_STONE;
  public final DeferredBlock<StairBlock> P_STAIRS;
  public final DeferredBlock<SlabBlock> P_SLAB;
  public final DeferredItem<BlockItem> STONE_ITEM;
  public final DeferredItem<BlockItem> STAIRS_ITEM;
  public final DeferredItem<BlockItem> SLAB_ITEM;
  public final DeferredItem<BlockItem> WALL_ITEM;
  public final DeferredItem<BlockItem> P_STONE_ITEM;
  public final DeferredItem<BlockItem> P_STAIRS_ITEM;
  public final DeferredItem<BlockItem> P_SLAB_ITEM;
  public final String name;
  public StoneFamily(String name){
    final BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE);
    this.name = name;
    this.STONE = ChemiBlocks.BLOCKS.register(name, () -> new Block(props));
    this.STONE_ITEM = ChemiBlocks.ITEMS.register(name, () -> new BlockItem(STONE.get(), new Item.Properties()));
    this.STAIRS = ChemiBlocks.BLOCKS.register(name+"_stairs", () -> new StairBlock(STONE.get().defaultBlockState(), props));
    this.STAIRS_ITEM = ChemiBlocks.ITEMS.register(name+"_stairs", () -> new BlockItem(STAIRS.get(), new Item.Properties()));
    this.SLAB = ChemiBlocks.BLOCKS.register(name+"_slab", () -> new SlabBlock(props));
    this.SLAB_ITEM = ChemiBlocks.ITEMS.register(name+"_slab", () -> new BlockItem(SLAB.get(), new Item.Properties()));
    this.WALL = ChemiBlocks.BLOCKS.register(name+"_wall", () -> new WallBlock(props));
    this.WALL_ITEM = ChemiBlocks.ITEMS.register(name+"_wall", () -> new BlockItem(WALL.get(), new Item.Properties()));
    this.P_STONE = ChemiBlocks.BLOCKS.register("polished_"+name, () -> new Block(props));
    this.P_STONE_ITEM = ChemiBlocks.ITEMS.register("polished_"+name, () -> new BlockItem(P_STONE.get(), new Item.Properties()));
    this.P_STAIRS = ChemiBlocks.BLOCKS.register("polished_"+name+"_stairs", () -> new StairBlock(P_STONE.get().defaultBlockState(), props));
    this.P_STAIRS_ITEM = ChemiBlocks.ITEMS.register("polished_"+name+"_stairs", () -> new BlockItem(P_STAIRS.get(), new Item.Properties()));
    this.P_SLAB = ChemiBlocks.BLOCKS.register("polished_"+name+"_slab", () -> new SlabBlock(props));
    this.P_SLAB_ITEM = ChemiBlocks.ITEMS.register("polished_"+name+"_slab", () -> new BlockItem(P_SLAB.get(), new Item.Properties()));
    Stones.add(this);
  }
  public Stream<DeferredItem<? extends BlockItem>> blockItems(){
    return Stream.of(STONE_ITEM, STAIRS_ITEM, SLAB_ITEM, WALL_ITEM, P_STONE_ITEM, P_STAIRS_ITEM, P_SLAB_ITEM);
  }
  public Stream<DeferredBlock<? extends Block>> blocks(){
    return Stream.of(STONE, STAIRS, SLAB, WALL, P_STONE, P_STAIRS, P_SLAB);
  }
}
