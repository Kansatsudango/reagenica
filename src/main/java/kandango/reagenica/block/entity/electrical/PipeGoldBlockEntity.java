package kandango.reagenica.block.entity.electrical;

import kandango.reagenica.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PipeGoldBlockEntity extends PipeAbstractBlockEntity{
  public PipeGoldBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.PIPE_GOLD.get(), pos, state);
  }

  @Override
  protected int maxTransfer() {
    return 48;
  }
  
}
