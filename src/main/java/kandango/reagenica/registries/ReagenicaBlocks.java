package kandango.reagenica.registries;

import kandango.reagenica.Reagenica;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReagenicaBlocks {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reagenica.MODID);
  public static final DeferredRegister.Items BLOCKITEMS = DeferredRegister.createItems(Reagenica.MODID);

  public static final DeferredBlock<Block> INITIAL_BLOCK = BLOCKS.registerSimpleBlock("initial_block", p -> p.mapColor(MapColor.STONE));
  public static final DeferredItem<BlockItem> INITIAL_BLOCK_ITEM = BLOCKITEMS.registerSimpleBlockItem(INITIAL_BLOCK);
}
