package kandango.reagenica.generator;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class BlockLootType {
  private final DeferredBlock<? extends Block> block;
  private final BlockType type;
  private final int count;
  private final Supplier<DeferredItem<? extends Item>> item;
  private MineableType mine;
  private int minelevel;
  public BlockLootType(DeferredBlock<? extends Block> block,BlockType type,int count,Supplier<DeferredItem<? extends Item>> item,MineableType mtype){
    this.block=block;
    this.type=type;
    this.count=count;
    this.item=item;
    this.mine=mtype;
    this.minelevel=0;
  }
  public DeferredBlock<? extends Block> blockreg(){
    return block;
  }
  public BlockType type(){
    return type;
  }
  public int count(){
    if(this.item==null)throw new UnsupportedOperationException();
    return count;
  }
  public DeferredItem<? extends Item> item(){
    if(this.item==null)throw new UnsupportedOperationException();
    return item.get();
  }
  public MineableType mine(){
    return this.mine;
  }
  public int minelevel(){
    return this.minelevel;
  }
  public void changeMineable(MineableType type){
    this.mine = type;
  }
  public static BlockLootType normal(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.NONE);
  }
  public static BlockLootType stone(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType wood(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.AXE);
  }
  public static BlockLootType hoe(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.HOE);
  }
  public static BlockLootType ore(DeferredBlock<? extends Block> block,int drop,Supplier<DeferredItem<? extends Item>> item,int level){
    BlockLootType type = new BlockLootType(block,BlockType.ORES, drop, item,MineableType.PICKAXE);
    type.minelevel=level;
    return type;
  }
  public static BlockLootType silktouch(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.SILKTOUCH, 0, null,MineableType.NONE);
  }
  public static BlockLootType machine(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.MACHINE, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType battery(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.MACHINE_SAVEENERGY, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType plants(DeferredBlock<? extends Block> block,int drop,Supplier<DeferredItem<? extends Item>> item){
    return new BlockLootType(block,BlockType.PLANTS, drop, item,MineableType.NONE);
  }
  public static BlockLootType none(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.NONE, 0, null,MineableType.NONE);
  }
  public static BlockLootType silkhoes(DeferredBlock<? extends Block> block){
    return new BlockLootType(block,BlockType.NONE, 0, null,MineableType.HOE);
  }
  public enum BlockType{
    NORMAL,
    ORES,
    SILKTOUCH,
    MACHINE,
    MACHINE_SAVEENERGY,
    PLANTS,
    NONE,
    MANUAL
  }
  public enum MineableType{
    PICKAXE,
    AXE,
    SHOVEL,
    HOE,
    NONE
  }
}
