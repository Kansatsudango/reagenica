package kandango.reagenica.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
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
  public static final MapCodec<SimpleMushroom> CODEC = simpleCodec(SimpleMushroom::new);

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
    BlockPos ground = pos.below();
    BlockState groundstate = lv.getBlockState(ground);
    if (groundstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
      return true;
    } else {
      return lv.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(groundstate, lv, ground);
    }
  }

  @Override
  protected MapCodec<? extends BushBlock> codec() {
    return CODEC;
  }
}
