package kandango.reagenica.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.entity.FractionalDistillerSubBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;


public class FractionalDistillerTop extends Block implements EntityBlock{
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

  public FractionalDistillerTop() {
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
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide) {
        var blockEntity = level.getBlockEntity(pos.below());
        if (blockEntity instanceof MenuProvider provider) {
          if(player instanceof ServerPlayer sp) sp.openMenu(provider, pos.below());
        }
    }
    return InteractionResult.SUCCESS;
  }
  
  @Override
  public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    if (!state.is(newState.getBlock())) {
      BlockPos bottomPos = pos.below();
      if (level.getBlockState(bottomPos).getBlock() == ChemiBlocks.FRACTIONAL_DISTILLER_BOTTOM.get()) {
            level.destroyBlock(bottomPos, false);
        }
      super.onRemove(state, level, pos, newState, isMoving);
    }
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new FractionalDistillerSubBlockEntity(pos, state);
  }

}
