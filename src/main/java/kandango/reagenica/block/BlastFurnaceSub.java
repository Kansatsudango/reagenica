package kandango.reagenica.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.entity.BlastFurnaceSubBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BlastFurnaceSub extends Block implements EntityBlock{
  public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 2); // 1=中段, 2=上段

  public BlastFurnaceSub() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
    this.registerDefaultState(this.defaultBlockState().setValue(LEVEL, 1));
  }

  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LEVEL);
  }

  @Override
  public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                             @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
    if (!level.isClientSide) {
      BlockPos basePos = getbasepos(pos, state.getValue(LEVEL));
      var blockEntity = level.getBlockEntity(basePos);
      if (blockEntity instanceof MenuProvider provider) {
        NetworkHooks.openScreen((ServerPlayer) player, provider, basePos);
      }
    }
    return InteractionResult.SUCCESS;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    if (!state.is(newState.getBlock())) {
      int floor = state.getValue(BlastFurnaceSub.LEVEL);
      BlockPos bottompos = pos.below(floor);
      BlockPos anothersub = floor==1 ? pos.above() : pos.below();
      if (level.getBlockState(anothersub).getBlock() == ChemiBlocks.BLASTFURNACE_SUB.get()) {
        level.destroyBlock(anothersub, false);
      }
      
      if (level.getBlockState(bottompos).getBlock() == ChemiBlocks.BLASTFURNACE_BOTTOM.get()) {
        level.destroyBlock(bottompos, false);
      }
      super.onRemove(state, level, pos, newState, isMoving);
    }
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new BlastFurnaceSubBlockEntity(pos, state);
  }

  private BlockPos getbasepos(BlockPos pos,Integer lv){
    BlockPos basePos = switch (lv) {
        case 1 -> pos.below(1);
        case 2 -> pos.below(2);
        default -> pos;
    };
    return basePos;
  }
}
