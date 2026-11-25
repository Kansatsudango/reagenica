package kandango.reagenica.block.farming.grape;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.ShapeStream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapeSapling extends Block{
  private static final VoxelShape SHAPE = ShapeStream.create(3, 0, 3, 13, 16, 13).build();

  public GrapeSapling() {
    super(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP));
  }
  
  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
  @Override
  public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new ItemStack(ChemiBlocks.GRAPE.get());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel slv, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
    if(!slv.isAreaLoaded(pos, 1))return;
    if (slv.getRawBrightness(pos, 0) >= 9) {
      if(rand.nextInt(4)==0){
        slv.setBlock(pos, ChemiBlocks.GRAPE_STEM.get().defaultBlockState().setValue(GrapeStem.LEAVES, true), UPDATE_ALL);
      }
    }
  }
}
