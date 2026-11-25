package kandango.reagenica.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Hydrangea extends BushBlock implements BonemealableBlock{
  private static final VoxelShape SHAPE = ShapeStream.create(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D)
                                          .add(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D).build();
  public Hydrangea(Properties props) {
    super(props);
  }
  
  protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return state.is(Blocks.CLAY) || state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND);
  }

  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }

  @Override
  public boolean isValidBonemealTarget( LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    popResource(level, pos, new ItemStack(this));
  }
}
