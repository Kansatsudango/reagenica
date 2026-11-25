package kandango.reagenica.client.particle;

import javax.annotation.Nonnull;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class GlowingSpore extends TextureSheetParticle{
  protected GlowingSpore(ClientLevel level, double x, double y, double z,
                         double vx, double vy, double vz) {
    super(level, x, y, z, vx, vy, vz);

    this.gravity = 0.01f;
    this.hasPhysics=false;
    this.lifetime = 120 + this.random.nextInt(60);
    this.quadSize = 0.2f + random.nextFloat() * 0.2f;
    long time = level.getDayTime();
    double baseradian = ((double)(time%24000) / 12000.0d) * Math.PI;
    double angleNoise = (random.nextDouble() - 0.5) * 0.3;
    double radian = baseradian+angleNoise;
    this.xd = Math.cos(radian)*0.1;
    this.yd = -0.05 - random.nextDouble() * 0.03;
    this.zd = Math.sin(radian)*0.1;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick){
    return 0xF000F0;
  }

  public static class Provider implements ParticleProvider<GlowingSporeOptions>{
    private final SpriteSet sprites;

    public Provider(SpriteSet sprites){
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(@Nonnull GlowingSporeOptions options, @Nonnull ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz){
      GlowingSpore p = new GlowingSpore(level, x, y, z, vx, vy, vz);
      p.pickSprite(this.sprites);
      p.setColor(options.r(),options.g(),options.b());
      return p;
    }
  }
}
