package kandango.reagenica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.electrical.SolarPowerGeneratorBlockEntity;

public class SolarPowerGenerator extends Block implements EntityBlock {

  public SolarPowerGenerator() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
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
  public SolarPowerGeneratorBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new SolarPowerGeneratorBlockEntity(pos, state);
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
}
