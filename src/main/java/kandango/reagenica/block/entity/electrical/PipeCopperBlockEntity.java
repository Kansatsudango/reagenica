package kandango.reagenica.block.entity.electrical;

import kandango.reagenica.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PipeCopperBlockEntity extends PipeAbstractBlockEntity{
  public PipeCopperBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.PIPE_COPPER.get(), pos, state);
  }

  @Override
  protected int maxTransfer() {
    return 50;
  }
  
}
