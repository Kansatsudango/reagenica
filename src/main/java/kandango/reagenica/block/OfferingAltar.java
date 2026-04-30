package kandango.reagenica.block;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.OfferingAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class OfferingAltar extends Block implements EntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

  public OfferingAltar() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
  }

  @Override
  public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                 @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
    if (!level.isClientSide) {
      if(player.getCooldowns().isOnCooldown(player.getItemInHand(hand).getItem())){
        return InteractionResult.PASS;
      }
      BlockEntity be = level.getBlockEntity(pos);
      if(be instanceof OfferingAltarBlockEntity alter){
        alter.onInteract(player, player.getItemInHand(hand));
      }
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean moving) {
    boolean powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
    boolean triggered = state.getValue(TRIGGERED);

    if (powered && !triggered) {
      level.scheduleTick(pos, this, 4);
      level.setBlock(pos, state.setValue(TRIGGERED, true), 4);
    } else if (!powered && triggered) {
      level.setBlock(pos, state.setValue(TRIGGERED, false), 4);
    }
  }
  @Override
  public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    BlockEntity be = level.getBlockEntity(pos);
    if (be instanceof OfferingAltarBlockEntity altar) {
        altar.signal(level);
    }
  }

  @Nullable
  @Override
  public OfferingAltarBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new OfferingAltarBlockEntity(pos, state);
  }

  @Override
  public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(TRIGGERED, false);
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
    builder.add(TRIGGERED);
  }
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
}
