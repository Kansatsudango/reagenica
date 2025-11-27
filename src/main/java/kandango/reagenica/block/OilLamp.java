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

public class OilLamp extends Block{
  private static final VoxelShape SHAPE = ShapeStream.create(5,0,5,11,3,11)
                                          .add(6, 3, 6, 10, 5, 10)
                                          .add(5,5,5,11,13,11)
                                          .build();
  public OilLamp(){
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
                .strength(1.0f, 1.0f).sound(SoundType.LANTERN).lightLevel(state -> 12).noOcclusion());
  }

  @Override
  public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    if(!level.isClientSide)return;

    double x = pos.getX() + 0.5D + random.nextDouble()*0.1-0.05;
    double y = pos.getY() + 0.45D + random.nextDouble()*0.1-0.05;
    double z = pos.getZ() + 0.5D + random.nextDouble()*0.1-0.05;
    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.01D, 0.0D);
    level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0.0D, 0.01D, 0.0D);
  }

  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
}
