package kandango.reagenica.block;

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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.AnalyzerBlockEntity;
import kandango.reagenica.block.entity.util.DestroyHelper;

public class Analyzer extends Block implements EntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  private static final VoxelShape SHAPE = ShapeStream.create(1, 0, 1, 15, 4, 15).build();

  public Analyzer() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0f).isRedstoneConductor((state, world, pos) -> false));
    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
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
  public AnalyzerBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new AnalyzerBlockEntity(pos, state);
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
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }

  @Override
  public BlockState rotate(@Nonnull BlockState state, @Nonnull Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(@Nonnull BlockState state, @Nonnull Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }
  
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context){
    return SHAPE;
  }
  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    if(!state.is(newState.getBlock())){
      BlockEntity be = level.getBlockEntity(pos);
      if(be instanceof AnalyzerBlockEntity an){
        DestroyHelper.dropItems(an.getItemHandler(), level, pos);
      }
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }
}
