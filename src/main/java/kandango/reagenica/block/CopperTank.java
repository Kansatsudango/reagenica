package kandango.reagenica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.CopperTankBlockEntity;
import kandango.reagenica.block.entity.util.DestroyHelper;

public class CopperTank extends Block implements EntityBlock {
  public CopperTank() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false).sound(SoundType.COPPER));
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide) {
      var blockEntity = level.getBlockEntity(pos);
      if (blockEntity instanceof MenuProvider provider) {
        if(player instanceof ServerPlayer sp) sp.openMenu(provider, pos);
      }
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public CopperTankBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new CopperTankBlockEntity(pos, state);
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
  @Override
  public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    if(!state.is(newState.getBlock())){
      BlockEntity be = level.getBlockEntity(pos);
      if(be instanceof CopperTankBlockEntity tank){
        DestroyHelper.dropItems(tank.getItemHandler(), level, pos);
        DestroyHelper.dropFluid(tank.getFluidTank().getFluid(), level, pos);
      }
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }
}
