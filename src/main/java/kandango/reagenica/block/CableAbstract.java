package kandango.reagenica.block;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.electrical.ElectricAbstract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class CableAbstract extends Block implements EntityBlock {
  public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
  public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
  public static final BooleanProperty EAST = BlockStateProperties.EAST;
  public static final BooleanProperty WEST = BlockStateProperties.WEST;
  public static final BooleanProperty UP = BlockStateProperties.UP;
  public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
  public static final Map<Direction, BooleanProperty> FACING_MAP = Map.of(
        Direction.NORTH, NORTH,
        Direction.SOUTH, SOUTH,
        Direction.EAST, EAST,
        Direction.WEST, WEST,
        Direction.UP, UP,
        Direction.DOWN, DOWN
    );
  private static final Map<Integer, VoxelShape> SHAPE_CACHE = new HashMap<>();
  private static final VoxelShape CORE_SHAPE = Block.box(6,6,6,10,10,10);

  public CableAbstract(Properties p) {
    super(p);
    this.registerDefaultState(this.defaultBlockState()
            .setValue(NORTH, false)
            .setValue(SOUTH, false)
            .setValue(EAST, false)
            .setValue(WEST, false)
            .setValue(UP, false)
            .setValue(DOWN, false));
  }

  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    Level level = context.getLevel();
      BlockPos pos = context.getClickedPos();
      BlockState state = this.defaultBlockState();

      for (Direction dir : Direction.values()) {
        BlockPos neighbor = pos.relative(dir);
        boolean connectable = shouldConnectTo(level,neighbor);
        state = state.setValue(FACING_MAP.get(dir), connectable);
      }

      return state;
  }
  private boolean shouldConnectTo(Level lv, BlockPos pos) {
    return (lv.getBlockEntity(pos) instanceof ElectricAbstract);
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(NORTH,SOUTH,EAST,WEST,UP,DOWN);
  }
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
  @Override
  public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
    return true;
  }

  @Override
  public boolean isOcclusionShapeFullBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
    return false;
  }
  @Override
  public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction, @Nonnull BlockState neighborState,
                             @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
    if (!level.isClientSide() && level instanceof Level lv) {
      boolean connected = shouldConnectTo(lv, neighborPos);
      return state.setValue(FACING_MAP.get(direction), connected);
    }
    return state;
  }

  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context){
    int key = getConnectionKey(state);
    return SHAPE_CACHE.computeIfAbsent(key, k -> buildShapefromState(state));
  }
  private int getConnectionKey(BlockState state){
    int key = 0;
    if (state.getValue(UP))    key |= 1 << 0;
    if (state.getValue(DOWN))  key |= 1 << 1;
    if (state.getValue(NORTH)) key |= 1 << 2;
    if (state.getValue(SOUTH)) key |= 1 << 3;
    if (state.getValue(WEST))  key |= 1 << 4;
    if (state.getValue(EAST))  key |= 1 << 5;
    return key;
  }
  private VoxelShape buildShapefromState(BlockState state){
    return ShapeStream.of(CORE_SHAPE)
    .addif(state.getValue(UP)   ,makeConnectionShape(Direction.UP))
    .addif(state.getValue(DOWN) ,makeConnectionShape(Direction.DOWN))
    .addif(state.getValue(NORTH),makeConnectionShape(Direction.NORTH))
    .addif(state.getValue(SOUTH),makeConnectionShape(Direction.SOUTH))
    .addif(state.getValue(WEST) ,makeConnectionShape(Direction.WEST))
    .addif(state.getValue(EAST) ,makeConnectionShape(Direction.EAST))
    .build();
  }
  private static VoxelShape makeConnectionShape(Direction direction) {
    switch (direction) {
      case UP:
        return box(6, 10, 6, 10, 16, 10); // 上方向
      case DOWN:
        return box(6, 0, 6, 10, 6, 10);   // 下方向
      case NORTH:
        return box(6, 6, 0, 10, 10, 6);   // 北（Z-）
      case SOUTH:
        return box(6, 6, 10, 10, 10, 16); // 南（Z+）
      case WEST:
        return box(0, 6, 6, 6, 10, 10);   // 西（X-）
      case EAST:
        return box(10, 6, 6, 16, 10, 10); // 東（X+）
      default:
        return Shapes.empty(); // 念のため
    }
  }
}
