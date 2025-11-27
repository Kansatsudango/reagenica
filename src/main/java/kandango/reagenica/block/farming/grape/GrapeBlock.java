package kandango.reagenica.block.farming.grape;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.BlockUtil;
import kandango.reagenica.block.ShapeStream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapeBlock extends Block{
  public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
  private static final VoxelShape SHAPE_SMALL = ShapeStream.create(5, 8, 5, 11, 16, 11).build();
  private static final VoxelShape SHAPE_LARGE = ShapeStream.create(3, 3, 3, 13, 16, 13).build();

  public GrapeBlock() {
    super(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().offsetType(OffsetType.XZ).sound(SoundType.CROP));
  }
  
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    Vec3 vec3 = state.getOffset(getter, pos);
    return BlockUtil.getStatus(state, AGE).map(age -> age==3).orElse(false) ? SHAPE_LARGE.move(vec3.x, vec3.y, vec3.z) : SHAPE_SMALL.move(vec3.x, vec3.y, vec3.z);
  }
  @Override
  public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new ItemStack(ChemiBlocks.GRAPE.get());
  }
  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    return this.defaultBlockState().setValue(AGE, 0);
  }
  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
    if(!slv.isAreaLoaded(pos, 1))return;
    if (slv.getRawBrightness(pos, 0) >= 9) {
      int i = this.getAge(state);
      if (i < this.getMaxAge()) {
        if (rand.nextInt(16) == 0) {
          slv.setBlock(pos, this.getStateForAge(i + 1), 2);
        }
      }
    }
  }
  public int getAge(BlockState state){
    return BlockUtil.getStatus(state, AGE).orElse(0);
  }
  public int getMaxAge(){
    return 3;
  }
  public BlockState getStateForAge(int age) {
    return this.defaultBlockState().setValue(AGE, age);
  }
}
