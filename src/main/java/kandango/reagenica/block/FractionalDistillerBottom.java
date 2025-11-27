package kandango.reagenica.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.entity.FractionalDistillerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class FractionalDistillerBottom extends Block implements EntityBlock{
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

  public FractionalDistillerBottom() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
  }

  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
      return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
  }

  @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                                 @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!level.isClientSide) {
          var blockEntity = level.getBlockEntity(pos);
          if (blockEntity instanceof MenuProvider provider) {
            NetworkHooks.openScreen((ServerPlayer) player, provider, pos);
          }
        }
        return InteractionResult.SUCCESS;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    if (!state.is(newState.getBlock())) {
      // Baseブロックが破壊されたらTopブロックも削除
      BlockPos topPos = pos.above();
      if (level.getBlockState(topPos).getBlock() == ChemiBlocks.FRACTIONAL_DISTILLER_TOP.get()) {
            level.destroyBlock(topPos, false);
        }
      super.onRemove(state, level, pos, newState, isMoving);
    }
  }
  @SuppressWarnings("deprecation")
  @Override
  public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
    super.onPlace(state, level, pos, oldState, isMoving);
    if (!level.isClientSide) {
      Direction facing = state.getValue(FractionalDistillerBottom.FACING);
      // 例：前方向と両サイドにサブブロックを配置
      BlockPos top = pos.relative(Direction.UP);
      BlockState subState = ChemiBlocks.FRACTIONAL_DISTILLER_TOP.get()
          .defaultBlockState()
          .setValue(FractionalDistillerTop.FACING, facing);

      level.setBlock(top, subState, Block.UPDATE_ALL);
    }
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new FractionalDistillerBlockEntity(pos, state);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
    return BlockUtil.getTicker(level, state, type);
  }
}
