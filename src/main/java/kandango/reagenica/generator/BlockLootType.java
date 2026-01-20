package kandango.reagenica.generator;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockLootType {
  private final RegistryObject<? extends Block> block;
  private final BlockType type;
  private final int count;
  private final Supplier<RegistryObject<? extends Item>> item;
  private MineableType mine;
  private int minelevel;
  public BlockLootType(RegistryObject<? extends Block> block,BlockType type,int count,Supplier<RegistryObject<? extends Item>> item,MineableType mtype){
    this.block=block;
    this.type=type;
    this.count=count;
    this.item=item;
    this.mine=mtype;
    this.minelevel=0;
  }
  public RegistryObject<? extends Block> blockreg(){
    return block;
  }
  public BlockType type(){
    return type;
  }
  public int count(){
    if(this.item==null)throw new UnsupportedOperationException();
    return count;
  }
  public RegistryObject<? extends Item> item(){
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
  public static BlockLootType normal(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.NONE);
  }
  public static BlockLootType stone(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType wood(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.AXE);
  }
  public static BlockLootType hoe(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.NORMAL, 0, null,MineableType.HOE);
  }
  public static BlockLootType ore(RegistryObject<? extends Block> block,int drop,Supplier<RegistryObject<? extends Item>> item,int level){
    BlockLootType type = new BlockLootType(block,BlockType.ORES, drop, item,MineableType.PICKAXE);
    type.minelevel=level;
    return type;
  }
  public static BlockLootType silktouch(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.SILKTOUCH, 0, null,MineableType.NONE);
  }
  public static BlockLootType machine(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.MACHINE, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType battery(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.MACHINE_SAVEENERGY, 0, null,MineableType.PICKAXE);
  }
  public static BlockLootType plants(RegistryObject<? extends Block> block,int drop,Supplier<RegistryObject<? extends Item>> item){
    return new BlockLootType(block,BlockType.PLANTS, drop, item,MineableType.NONE);
  }
  public static BlockLootType none(RegistryObject<? extends Block> block){
    return new BlockLootType(block,BlockType.NONE, 0, null,MineableType.NONE);
  }
  public static BlockLootType silkhoes(RegistryObject<? extends Block> block){
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
