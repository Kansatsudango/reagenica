package kandango.reagenica.block;

import javax.annotation.Nullable;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ChemiLogBlock extends RotatedPillarBlock{
  private final Block stripped;

  public ChemiLogBlock(Block stripped, Properties p) {
    super(p);
    this.stripped = stripped;
  }
  
  @Nullable
  public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate){
    if (toolAction == ToolActions.AXE_STRIP){
      return stripped.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
    }
    return null;
  }
}
