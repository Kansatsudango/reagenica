package kandango.reagenica.block.farming.grape;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapePargola extends Block{
  public static final int MAX_SPREAD = 7;
  public static final IntegerProperty SPREAD = IntegerProperty.create("spread", 0, MAX_SPREAD);
  private static final VoxelShape SHAPE = Block.box(0,0,0,16,8,16);
  public GrapePargola() {
    super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES));
  }
  
  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    return this.defaultBlockState().setValue(SPREAD, 0);
  }
  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(SPREAD);
  }
  public BlockState getStateForSpread(int spread) {
    return this.defaultBlockState().setValue(SPREAD, spread);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    if(!slv.isAreaLoaded(pos, 1))return;
    check(state, slv, pos, random);
    spread(state, slv, pos, random);
    fruit(state, slv, pos, random);
  }
  private void check(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource random){
    int currentspread = BlockUtil.getStatus(state, SPREAD).orElse(MAX_SPREAD);
    boolean changedflag = false;
    for(Direction dir : Direction.Plane.HORIZONTAL){
      BlockPos relative = pos.relative(dir);
      BlockState relativestate = slv.getBlockState(relative);
      if(relativestate.is(ChemiBlocks.GRAPE_PARGOLA.get())){
        int relspr = BlockUtil.getStatus(relativestate, SPREAD).orElse(MAX_SPREAD);
        if(currentspread > relspr+1){
          currentspread = relspr+1;
          changedflag=true;
        }
      }
    }
    if(changedflag){
      slv.setBlock(pos, getStateForSpread(currentspread), Block.UPDATE_NONE);
    }
  }
  private void spread(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource random){
    int currentspread = BlockUtil.getStatus(state, SPREAD).orElse(MAX_SPREAD);
    if(currentspread<MAX_SPREAD){
      for(Direction dir : Direction.Plane.HORIZONTAL){
        BlockPos relative = pos.relative(dir);
        if(slv.getBlockState(relative).is(ChemiBlocks.PARGOLA.get())){
          if(random.nextInt(10)==0){
            slv.setBlock(relative, getStateForSpread(currentspread+1), 3);
          }
        }
      }
    }
  }
  private void fruit(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource random){
    BlockPos fruitpos = pos.below();
    if(!slv.getBlockState(fruitpos).isAir())return;
    int dense=0;
    for(int x=-1;x<=1;x++){
      for(int z=-1;z<=1;z++){
        BlockPos rel = fruitpos.offset(x, 0, z);
        if(slv.getBlockState(rel).is(ChemiBlocks.GRAPE_CROP.get())){
          dense++;
        }
      }
    }
    if(random.nextInt(dense*3+6)==0){
      slv.setBlock(fruitpos, ChemiBlocks.GRAPE_CROP.get().defaultBlockState(), Block.UPDATE_ALL);
    }
  }
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
  @Override
  public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
    return 1;
  }
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
}
