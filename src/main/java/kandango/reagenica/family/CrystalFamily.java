package kandango.reagenica.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.BuddingCrystalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

public class CrystalFamily {
  public static final List<CrystalFamily> Crystals = Collections.synchronizedList(new ArrayList<>());
  public final RegistryObject<AmethystBlock> BLOCK;
  public final RegistryObject<Block> BUDDING_BLOCK;
  public final RegistryObject<AmethystClusterBlock> CRYSTAL;
  public final RegistryObject<AmethystClusterBlock> CRYSTAL_BUD;
  public final RegistryObject<BlockItem> BLOCK_ITEM;
  public final RegistryObject<BlockItem> BUDDING_BLOCK_ITEM;
  public final RegistryObject<BlockItem> CRYSTAL_ITEM;
  public final RegistryObject<BlockItem> CRYSTAL_BUD_ITEM;
  public final RegistryObject<Item> SHARD_ITEM;
  public final String name;

  public CrystalFamily(String name, MapColor color){
    this.name = name;
    this.CRYSTAL = ChemiBlocks.BLOCKS.register(name+"_crystal", () -> new AmethystClusterBlock(7,3,
      BlockBehaviour.Properties.of().mapColor(color).forceSolidOn().noOcclusion().randomTicks()
      .sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel(x -> 9)
      .pushReaction(PushReaction.DESTROY)));
    this.CRYSTAL_ITEM = ChemiBlocks.ITEMS.register(name+"_crystal",
      () -> new BlockItem(this.CRYSTAL.get(), new Item.Properties()));
    this.CRYSTAL_BUD = ChemiBlocks.BLOCKS.register(name+"_bud", () -> new AmethystClusterBlock(5,3,
      BlockBehaviour.Properties.of().mapColor(color).forceSolidOn().noOcclusion().randomTicks()
      .sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel(x -> 6).pushReaction(PushReaction.DESTROY)));
    this.CRYSTAL_BUD_ITEM = ChemiBlocks.ITEMS.register(name+"_bud",
      () -> new BlockItem(this.CRYSTAL_BUD.get(), new Item.Properties()));
    this.BLOCK = ChemiBlocks.BLOCKS.register(name+"_block", () -> new AmethystBlock(
      BlockBehaviour.Properties.of().mapColor(color).strength(1.5F)
      .sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    this.BLOCK_ITEM = ChemiBlocks.ITEMS.register(name+"_block",
      () -> new BlockItem(this.BLOCK.get(), new Item.Properties()));
    this.BUDDING_BLOCK = ChemiBlocks.BLOCKS.register("budding_"+name+"_block", () -> new BuddingCrystalBlock(
      BlockBehaviour.Properties.of().mapColor(color).strength(1.5F)
      .sound(SoundType.AMETHYST).requiresCorrectToolForDrops()
      ,this.CRYSTAL_BUD::get,this.CRYSTAL::get));
    this.BUDDING_BLOCK_ITEM = ChemiBlocks.ITEMS.register("budding_"+name+"_block",
      () -> new BlockItem(this.BUDDING_BLOCK.get(), new Item.Properties()));
    this.SHARD_ITEM = ChemiBlocks.ITEMS.register(name+"_shard", () -> new Item(new Item.Properties()));
    Crystals.add(this);
  }

  public Stream<RegistryObject<? extends Item>> crystalItems(){
    return Stream.of(BLOCK_ITEM, BUDDING_BLOCK_ITEM, CRYSTAL_ITEM, CRYSTAL_BUD_ITEM, SHARD_ITEM);
  }
  public Stream<RegistryObject<? extends Block>> crystalBlocks(){
    return Stream.of(BLOCK, BUDDING_BLOCK, CRYSTAL, CRYSTAL_BUD);
  }
}
