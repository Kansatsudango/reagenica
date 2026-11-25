package kandango.reagenica.client.particle;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import kandango.reagenica.ChemiParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public record GlowingSporeOptions(float r, float g, float b) implements ParticleOptions {
  public static final Codec<GlowingSporeOptions> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
          Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
          Codec.FLOAT.fieldOf("b").forGetter(o -> o.b)
      ).apply(instance, GlowingSporeOptions::new));

  @Override
  public ParticleType<?> getType() {
      return ChemiParticles.GLOWING_SPORE.get();
  }

  @Override
  public void writeToNetwork(@Nonnull FriendlyByteBuf buf) {
    buf.writeFloat(r);
    buf.writeFloat(g);
    buf.writeFloat(b);
  }

  @SuppressWarnings("deprecation")
  public static final ParticleOptions.Deserializer<GlowingSporeOptions> DESERIALIZER = new Deserializer();

  @Override
  public String writeToString() {
    return String.format(
    Locale.ROOT,
    "%f %f %f",
    r, g, b
    );
  }

  @SuppressWarnings("deprecation")
  private static class Deserializer implements ParticleOptions.Deserializer<GlowingSporeOptions>{
    @Override
    public GlowingSporeOptions fromCommand(@Nonnull ParticleType<GlowingSporeOptions> type, @Nonnull StringReader reader)
        throws CommandSyntaxException {
      reader.expect(' ');
      float r = reader.readFloat();
      reader.expect(' ');
      float g = reader.readFloat();
      reader.expect(' ');
      float b = reader.readFloat();

      return new GlowingSporeOptions(r, g, b);
    }

    @Override
    public GlowingSporeOptions fromNetwork(@Nonnull ParticleType<GlowingSporeOptions> type, @Nonnull FriendlyByteBuf buf) {
      float r = buf.readFloat();
      float g = buf.readFloat();
      float b = buf.readFloat();
      return new GlowingSporeOptions(r, g, b);
    }

  }
}
