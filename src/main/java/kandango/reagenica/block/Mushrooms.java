package kandango.reagenica.block;

import javax.annotation.Nonnull;

import kandango.reagenica.client.particle.GlowingSporeOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class Mushrooms extends Block{
  private final float r;
  private final float g;
  private final float b;

  public Mushrooms(Properties p, float r, float g, float b) {
    super(p);
    this.r=r;
    this.g=g;
    this.b=b;
  }

  @Override
  public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
    if(!level.isClientSide)return;
    if (rand.nextInt(16) == 0) {
      level.addParticle(new GlowingSporeOptions(r, g, b),
        pos.getX() + rand.nextDouble(),
        pos.getY() ,
        pos.getZ() + rand.nextDouble(),
        r, g, b);
    }
  }
}
