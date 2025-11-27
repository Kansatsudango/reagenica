package kandango.reagenica.block;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.electrical.FluidFuelGeneratorBlockEntity;
import kandango.reagenica.block.entity.util.DestroyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class FluidFuelGenerator extends Block implements EntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty LIT = BlockStateProperties.LIT;

  public FluidFuelGenerator() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
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

  @Nullable
  @Override
  public FluidFuelGeneratorBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new FluidFuelGeneratorBlockEntity(pos, state);
  }

  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, false);
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
    builder.add(LIT);
  }
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
    return BlockUtil.getTicker(level, state, type);
  }
  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    if(!state.is(newState.getBlock())){
      BlockEntity be = level.getBlockEntity(pos);
      if(be instanceof FluidFuelGeneratorBlockEntity generator){
        DestroyHelper.dropItems(generator.getItemHandler(), level, pos);
      }
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }
}
