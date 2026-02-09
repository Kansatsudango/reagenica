package kandango.reagenica.worldgen.biome;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import kandango.reagenica.worldgen.ChemiBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;

public class PaleoBiomeSource extends BiomeSource{
  private final float OCEAN_COAST_BORDER = -0.19f;
  private final float COAST_NEAR_BORDER = -0.11f;
  private final float NEAR_MID_BORDER = 0.03f;
  private final float MID_FAR_BORDER = 0.3f;
  private final float EROSION_0_1_BORDER = -0.78f;
  private final float EROSION_1_2_BORDER = -0.375f;
  private final float EROSION_2_3_BORDER = -0.2225f;
  private final float EROSION_3_4_BORDER = 0.05f;
  private final float EROSION_4_5_BORDER = 0.45f;
  private final float EROSION_5_6_BORDER = 0.55f;
  public static final Codec<PaleoBiomeSource> CODEC =
    RecordCodecBuilder.create(instance ->
        instance.group(
            RegistryOps.retrieveRegistryLookup(Registries.BIOME).forGetter(src -> null)
        ).apply(instance, PaleoBiomeSource::new)
    );

  private final HolderLookup.RegistryLookup<Biome> biomeLookup;

  public PaleoBiomeSource(HolderLookup.RegistryLookup<Biome> biomeLookup){
    super();
    this.biomeLookup = biomeLookup;
  }
  
  @Override
  protected Codec<? extends BiomeSource> codec() {
    return CODEC;
  }

  @Override
  protected Stream<Holder<Biome>> collectPossibleBiomes() {
    return ChemiBiomes.biomeList.stream().map(key -> biomeLookup.getOrThrow(key));
  }

  @Override
  public Holder<Biome> getNoiseBiome(int x, int y, int z, @Nonnull Sampler sampler) {
    Climate.TargetPoint climate = sampler.sample(x, y, z);

    float temperature = Climate.unquantizeCoord(climate.temperature());
    float humidity = Climate.unquantizeCoord(climate.humidity());
    float continentalness = Climate.unquantizeCoord(climate.continentalness());
    float erosion = Climate.unquantizeCoord(climate.erosion());
    float weirdness = Climate.unquantizeCoord(climate.weirdness());
    float depth = Climate.unquantizeCoord(climate.depth());

    return biomeLookup.getOrThrow(biomes(temperature, humidity, continentalness, erosion, weirdness, depth));
  }

  private ResourceKey<Biome> biomes(float t, float h, float c, float e, float w, float d){
    if(d > 0.1){
      return caveBiomes(t, h, c, e, w, d);
    }else{
      if(c < -1.05){
        return ChemiBiomes.FUNGI_FIELD;
      }else if(c < OCEAN_COAST_BORDER){
        return oceanBiomes(t, h, c, e, w, d);
      }else{
        if(peakAndValleys(w) < -0.7){//Valley
          if(e > 0.445){
            return ChemiBiomes.PALEO_SWAMP;
          }else if(c > NEAR_MID_BORDER && e < EROSION_1_2_BORDER){
            return inLandBiomes(t, h, c, e, w, d);
          }else{
            return riverBiomes(t, h, c, e, w, d);
          }
        }else if(peakAndValleys(w) < -0.2){//Low
          return inLandBiomes(t, h, c, e, w, d);
        }else if(peakAndValleys(w) < 0.2){//Mid
          if(e < EROSION_0_1_BORDER && c > COAST_NEAR_BORDER){
            return slopeBiomes(t, h, c, e, w, d);
          }else if(e < EROSION_1_2_BORDER && c > MID_FAR_BORDER){
            return slopeBiomes(t, h, c, e, w, d);
          }else if(e < EROSION_2_3_BORDER && c > MID_FAR_BORDER){
            return plateauBiomes(t, h, c, e, w, d);
          }
          return inLandBiomes(t, h, c, e, w, d);
        }else if(peakAndValleys(w) < 0.7){//High
          if(c > NEAR_MID_BORDER && e < EROSION_0_1_BORDER){
            return peakBiomes(t, h, c, e, w, d);
          }else if(c > COAST_NEAR_BORDER && e < EROSION_1_2_BORDER){
            return slopeBiomes(t, h, c, e, w, d);
          }else if(c > NEAR_MID_BORDER && e < EROSION_2_3_BORDER){
            return plateauBiomes(t, h, c, e, w, d);
          }else if(c > MID_FAR_BORDER && e < EROSION_3_4_BORDER){
            return plateauBiomes(t, h, c, e, w, d);
          }
        }else if(peakAndValleys(w) < 1.0){//Peaks
          if(e < EROSION_0_1_BORDER){
            return peakBiomes(t, h, c, e, w, d);
          }else if(c > NEAR_MID_BORDER && e < EROSION_1_2_BORDER){
            return peakBiomes(t, h, c, e, w, d);
          }else if(e < EROSION_1_2_BORDER){
            return slopeBiomes(t, h, c, e, w, d);
          }
        }
      }
      return ChemiBiomes.METASEQUOIA_FOREST;
    }
  }

  private ResourceKey<Biome> caveBiomes(float t, float h, float c, float e, float w, float d){
    if(-0.6<c && c<0.2){
      if(e<0.3 && h>0.3 && t>0 && d<0.6) return ChemiBiomes.COAL_CAVE;
      else if(e<0.5 && h<0.1 && w<-0.2 && d>0.6) return ChemiBiomes.REDSTONE_CAVE;
      else if(e>0 && h<-0.1 && t<0 && d<0.6 && w<-0.2) return ChemiBiomes.LAPIS_CAVE;
    }else if(c<0.5){
      if(e>0 && h<-0.1 && t<0 && d<0.6 && w<-0.2) return ChemiBiomes.LAPIS_CAVE;
      else if(ranged(e, -0.5f, 0.5f) && h<0 && t<0 && d>0.6)return ChemiBiomes.LEAD_CAVE;
      else if(e<0.3 && h<0 && t>0.2 && w>-0.3)return ChemiBiomes.IRON_CAVE;
    }else{
      if(e<-0.5 && w>-0.2 && d>0.7)return ChemiBiomes.DIAMOND_CAVE;
      else if(e<-0.2 && h>0 && w<0.2)return ChemiBiomes.EMERALD_CAVE;
      else if(e>-0.2 && t>-0.4 && w>0)return ChemiBiomes.GOLD_CAVE;
    }
    return ChemiBiomes.COMMON_CAVE;
  }
  private ResourceKey<Biome> oceanBiomes(float t, float h, float c, float e, float w, float d){
    if(t<0)return ChemiBiomes.LUKEWARM_OCEAN;
    else return ChemiBiomes.WARM_OCEAN;
  }
  private ResourceKey<Biome> riverBiomes(float t, float h, float c, float e, float w, float d){
    if(ranged(e, -0.375f, -0.22f))return ChemiBiomes.CANYON_RIVER;
    else if(h>0)return ChemiBiomes.TAXODIUM_RIVER;
    else return ChemiBiomes.PRIMEVAL_RIVER;
  }
  private ResourceKey<Biome> inLandBiomes(float t, float h, float c, float e, float w, float d){
    if(h<0)return ChemiBiomes.PALEO_PLAIN;
    else return ChemiBiomes.METASEQUOIA_FOREST;
  }
  private ResourceKey<Biome> peakBiomes(float t, float h, float c, float e, float w, float d){
    if(w > 0 && h < 0.1f) return ChemiBiomes.VOLCANO_PEAKS;
    else if(t < 0) return ChemiBiomes.SNOWY_PEAKS;
    else return ChemiBiomes.ROCKY_PEAKS;
  }
  private ResourceKey<Biome> slopeBiomes(float t, float h, float c, float e, float w, float d){
    if(w > 0 && h < 0.1f) return ChemiBiomes.VOLCANO_SLOPES;
    else if(t < 0) return ChemiBiomes.SNOWY_SLOPES;
    else return ChemiBiomes.WARM_SLOPES;
  }
  private ResourceKey<Biome> plateauBiomes(float t, float h, float c, float e, float w, float d){
    return ChemiBiomes.PALEO_MEADOW;
  }

  private float peakAndValleys(float weirdness){
    return 1.0f - Math.abs((3 * Math.abs(weirdness)) - 2);
  }
  private boolean ranged(float x, float min, float max){
    return min<=x && x<=max;
  }
}
