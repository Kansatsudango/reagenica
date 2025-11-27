package kandango.reagenica.generator;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockLootType {
  private final RegistryObject<Block> block;
  private final BlockType type;
  private final int count;
  private final Supplier<RegistryObject<Item>> item;
  private MineableType mine;
  private int minelevel;
  public BlockLootType(RegistryObject<Block> block,BlockType type,int count,Supplier<RegistryObject<Item>> item,MineableType mtype){
    this.block=block;
    this.type=type;
    this.count=count;
    this.item=item;
    this.mine=mtype;
    this.minelevel=0;
  }
  public RegistryObject<Block> blockreg(){
    return block;
  }
  public BlockType type(){
    return type;
  }
  public int count(){
    if(this.item==null)throw new UnsupportedOperationException();
    return count;
  }
  public RegistryObject<Item> item(){
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
  public static BlockLootType normal(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.NONE);
  }
  public static BlockLootType stone(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType wood(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.AXE);
  }
  public static BlockLootType hoe(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.HOE);
  }
  public static BlockLootType ore(RegistryObject<Block> block,int drop,Supplier<RegistryObject<Item>> item,int level){
    BlockLootType type = new BlockLootType(block,BlockType.ORES, drop, item,MineableType.PICKAXE);
    type.minelevel=level;
    return type;
  }
  public static BlockLootType silktouch(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.SILKTOUCH, 0, null,MineableType.NONE);
  }
  public static BlockLootType machine(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.MACHINE, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType battery(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.MACHINE_SAVEENERGY, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType plants(RegistryObject<Block> block,int drop,Supplier<RegistryObject<Item>> item){
    return new BlockLootType(block,BlockType.PLANTS, drop, item,MineableType.NONE);
  }
  public static BlockLootType none(RegistryObject<Block> block){
    return new BlockLootType(block,BlockType.NONE, 0, null,MineableType.NONE);
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
