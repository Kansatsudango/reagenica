package kandango.reagenica.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Andon extends Block{
  public static final IntegerProperty LIGHT = IntegerProperty.create("light", 0, 3);
  private static final VoxelShape SHAPE = ShapeStream.create(4,0,4,12,16,12).build();

  public Andon() {
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.8F).sound(SoundType.WOOD)
                    .lightLevel(state -> BlockUtil.getStatus(state, LIGHT).map(x -> x*5).orElse(0))
                    .noOcclusion());
    this.registerDefaultState(this.defaultBlockState().setValue(LIGHT, 3));
  }

  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    return this.defaultBlockState().setValue(LIGHT, 3);
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIGHT);
  }

  @Override
  public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                 @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
    if(!level.isClientSide){
      int light = state.getValue(LIGHT);
      light--;
      if(light<0)light=3;
      BlockState newState = state.setValue(LIGHT, light);
      level.setBlock(pos, newState, Block.UPDATE_ALL);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
}
