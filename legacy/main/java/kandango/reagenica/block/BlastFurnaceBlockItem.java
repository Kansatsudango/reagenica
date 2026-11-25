package kandango.reagenica.block;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceBlockItem extends BlockItem{
  public BlastFurnaceBlockItem(Block block, Properties prop){
    super(block,prop);
  }

  @Override
  public InteractionResult place(@Nonnull BlockPlaceContext context) {
    Level level = context.getLevel();
    BlockPos basePos = context.getClickedPos();

    // 上2ブロックの位置を確認
    BlockPos middlePos = basePos.above(1);
    BlockPos topPos = basePos.above(2);

    // 空間チェック
    if (!level.getBlockState(middlePos).canBeReplaced(context) ||
        !level.getBlockState(topPos).canBeReplaced(context)) {
        return InteractionResult.FAIL;
    }

    // ベースブロックの設置
    InteractionResult result = super.place(context);
    if (!result.consumesAction()) return result;

    // 残り2つのブロックを設置
    BlockState middle = ChemiBlocks.BLASTFURNACE_SUB.get().defaultBlockState()
            .setValue(BlastFurnaceSub.LEVEL, 1); // 中段
    BlockState top = ChemiBlocks.BLASTFURNACE_SUB.get().defaultBlockState()
            .setValue(BlastFurnaceSub.LEVEL, 2); // 上段

    level.setBlock(middlePos, middle, 3);
    level.setBlock(topPos, top, 3);

    return result;
  } 
}
