package kandango.reagenica.block;

import java.util.Map;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.StackLampBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StackLamp extends Block implements EntityBlock {
  public static final DirectionProperty FACING = DirectionalBlock.FACING;
  private static final Map<Direction,VoxelShape> SHAPES = Map.of(
    Direction.UP,ShapeStream.create(6.0, 0.0, 6.0, 10.0, 15.0, 10.0).build(),
    Direction.DOWN,ShapeStream.create(6.0, 1.0, 6.0, 10.0, 16.0, 10.0).build(),
    Direction.NORTH,ShapeStream.create(6.0, 6.0, 1.0, 10.0, 10.0, 16.0).build(),
    Direction.SOUTH,ShapeStream.create(6.0, 6.0, 0.0, 10.0, 10.0, 15.0).build(),
    Direction.WEST,ShapeStream.create(1.0, 6.0, 6.0, 16.0, 10.0, 10.0).build(),
    Direction.EAST,ShapeStream.create(0.0, 6.0, 6.0, 15.0, 10.0, 10.0).build()
  );

  public StackLamp() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
  }
  
  @Nullable
  @Override
  public StackLampBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new StackLampBlockEntity(pos, state);
  }

  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getClickedFace());
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    Direction facing = state.getValue(FACING);
    return SHAPES.get(facing);
  }
}
