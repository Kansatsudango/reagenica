package kandango.reagenica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SimpleMushroom extends BushBlock {
  protected static final float AABB_OFFSET = 3.0F;
  protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

  public SimpleMushroom(BlockBehaviour.Properties props) {
    super(props);
  }

  public VoxelShape getShape(BlockState p_54889_, BlockGetter p_54890_, BlockPos p_54891_, CollisionContext p_54892_) {
    return SHAPE;
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return state.isSolidRender(worldIn, pos);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader lv, BlockPos pos) {
    BlockPos blockpos = pos.below();
    BlockState blockstate = lv.getBlockState(blockpos);
    if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
      return true;
    } else {
      return lv.getRawBrightness(pos, 0) < 13 && blockstate.canSustainPlant(lv, blockpos, Direction.UP, this);
    }
  }
}
