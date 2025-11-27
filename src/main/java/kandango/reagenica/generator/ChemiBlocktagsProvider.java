package kandango.reagenica.generator;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.generator.BlockLootType.MineableType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ChemiBlocktagsProvider extends BlockTagsProvider {
  public ChemiBlocktagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, "reagenica", existingFileHelper);
  }

  @Override
  protected void addTags(@Nonnull HolderLookup.Provider provider) {
    for(BlockLootType block : ChemiBlocks.listBlocks){
      if(block.mine()==MineableType.PICKAXE){
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.blockreg().get());
        switch (block.minelevel()) {
          case 0:
            break;
          case 1:
            tag(BlockTags.NEEDS_STONE_TOOL).add(block.blockreg().get());
            break;
          case 2:
            tag(BlockTags.NEEDS_IRON_TOOL).add(block.blockreg().get());
            break;
          case 3:
            tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block.blockreg().get());
            break;
          default:
            break;
        }
      }else if(block.mine()==MineableType.AXE){
        tag(BlockTags.MINEABLE_WITH_AXE).add(block.blockreg().get());
      }else if(block.mine()==MineableType.SHOVEL){
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block.blockreg().get());
      }else if(block.mine()==MineableType.HOE){
        tag(BlockTags.MINEABLE_WITH_HOE).add(block.blockreg().get());
      }
    }
  }
}
