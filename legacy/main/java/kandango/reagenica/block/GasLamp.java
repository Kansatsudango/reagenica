package kandango.reagenica.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GasLamp extends Block{
  private static final VoxelShape SHAPE = ShapeStream.create(4,0,4,12,9,12)
                                          .add(3, 9, 3, 13, 10, 13)
                                          .add(4,10,4,12,11,12)
                                          .add(7,11,7,9,12,9)
                                          .add(6,12,6,10,14,10)
                                          .add(7.5,14,7.5,8.5,16,8.5)
                                          .build();
  public GasLamp(){
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
                .strength(1.0f, 1.0f).sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion());
  }

  @Override
  public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    if(!level.isClientSide)return;

    double x = pos.getX() + 0.5D + random.nextDouble()*0.1-0.05;
    double y = pos.getY() + 0.25D + random.nextDouble()*0.1-0.05;
    double z = pos.getZ() + 0.5D + random.nextDouble()*0.1-0.05;
    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.01D, 0.0D);
    level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0D, 0.01D, 0.0D);
  }

  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
}
