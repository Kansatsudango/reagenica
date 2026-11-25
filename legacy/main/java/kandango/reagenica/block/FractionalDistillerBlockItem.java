package kandango.reagenica.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class FractionalDistillerBlockItem extends BlockItem{
  public FractionalDistillerBlockItem(Block block, Properties prop){
    super(block,prop);
  }

  @Override
  public InteractionResult place(@Nonnull BlockPlaceContext context) {
    Level level = context.getLevel();
    BlockPos basePos = context.getClickedPos();

    // 上2ブロックの位置を確認
    BlockPos topPos = basePos.above(1);

    // 空間チェック
    if (!level.getBlockState(topPos).canBeReplaced(context)) {
        return InteractionResult.FAIL;
    }

    // ベースブロックの設置
    InteractionResult result = super.place(context);
    if (!result.consumesAction()) return result;

    return result;
  } 
}
