package kandango.reagenica.block.farming.grape;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapeStem extends Block{
  private static final VoxelShape SHAPE = Block.box(5,0,5,11,16,11);
  public static final BooleanProperty LEAVES = BooleanProperty.create("leaves");
  public GrapeStem() {
    super(BlockBehaviour.Properties.of().strength(1.0f, 3.0f).randomTicks().sound(SoundType.WOOD));
    this.registerDefaultState(this.defaultBlockState().setValue(LEAVES, false));
  }
  
  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> state){
    super.createBlockStateDefinition(state);
    state.add(LEAVES);
  }
  @Override
  public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
    BlockPos below = pos.below();
    BlockState belowState = level.getBlockState(below);
    return belowState.is(this) || belowState.is(BlockTags.DIRT);
  }
  
  public BlockState getBlockStateForLeaves(boolean leaves){
    return this.defaultBlockState().setValue(LEAVES, leaves);
  }
  @SuppressWarnings("deprecation")
  @Override
  @Nonnull
  public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction dir, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
    if (!state.canSurvive(level, pos)) {
      return Blocks.AIR.defaultBlockState();
    }
    return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    if(!slv.isAreaLoaded(pos, 1))return;
    super.randomTick(state, slv, pos, random);
    if(random.nextInt(2)==0){
      BlockPos abovepos = pos.above();
      BlockState above = slv.getBlockState(abovepos);
      if(above.isAir()){
        boolean isNotTooTall=false;
        for(int i=-4;i<0;i++){
          if(slv.getBlockState(pos.offset(0,i,0)).is(BlockTags.DIRT))isNotTooTall=true;
        }
        if(isNotTooTall){
          slv.setBlock(abovepos, this.getBlockStateForLeaves(true), UPDATE_ALL);
          slv.setBlock(pos, this.getBlockStateForLeaves(false), UPDATE_ALL);
        }
      }else if(above.is(ChemiBlocks.PARGOLA.get())){
        slv.setBlock(abovepos, ChemiBlocks.GRAPE_PARGOLA.get().defaultBlockState(), UPDATE_ALL);
      }
    }
  }
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
}
