package kandango.reagenica;

import kandango.reagenica.client.particle.GlowingSporeOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import com.mojang.serialization.Codec;

public class ChemiParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLES = 
                          DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ChemistryMod.MODID);
  
  public static final DeferredHolder<ParticleType<GlowingSporeOptions>> GLOWING_SPORE = 
                  PARTICLES.register("glowing_spore", () -> 
                  new ParticleType<>(false, GlowingSporeOptions.DESERIALIZER){
                    @Override
                    public Codec<GlowingSporeOptions> codec() {
                        return GlowingSporeOptions.CODEC;
                    }
                  });
  public static final DeferredHolder<SimpleParticleType> AMBIENT_SPORE = PARTICLES.register("ambient_spore", () -> new SimpleParticleType(true));
}
