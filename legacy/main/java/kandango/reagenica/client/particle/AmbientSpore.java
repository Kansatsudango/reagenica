package kandango.reagenica.client.particle;

import javax.annotation.Nonnull;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class AmbientSpore extends TextureSheetParticle{
  protected AmbientSpore(ClientLevel level, double x, double y, double z,
                         double vx, double vy, double vz) {
    super(level, x, y, z, vx, vy, vz);

    this.lifetime = 40 + this.random.nextInt(20);
    this.quadSize = 0.1f + random.nextFloat() * 0.1f;
    long time = level.getDayTime();
    double baseradian = ((double)(time%24000) / 12000.0d) * Math.PI;
    double angleNoise = (random.nextDouble() - 0.5) * 0.3;
    double radian = baseradian+angleNoise;
    this.xd = Math.cos(radian)*0.01;
    this.yd = 0.01;
    this.zd = Math.sin(radian)*0.01;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick){
    return 0xF000F0;
  }

  public static class Provider implements ParticleProvider<SimpleParticleType>{
    private final SpriteSet sprites;

    public Provider(SpriteSet sprites){
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(@Nonnull SimpleParticleType type, @Nonnull ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz){
      AmbientSpore p = new AmbientSpore(level, x, y, z, vx, vy, vz);
      p.pickSprite(this.sprites);
      Color color = generateColor(level);
      p.setColor(color.r,color.g,color.b);
      return p;
    }
    private Color generateColor(ClientLevel lv){
      return switch (lv.getRandom().nextInt(4)) {
        case 0 -> new Color(1.0f, 0.0f, 0.0f);
        case 1 -> new Color(0.0f, 1.0f, 0.0f);
        case 2 -> new Color(0.0f, 1.0f, 1.0f);
        case 3 -> new Color(1.0f, 0.0f, 1.0f);
        default -> new Color(0.0f, 0.0f, 0.0f);
      };
    }
    private static record Color(float r, float g, float b) {
    }
  }
}
