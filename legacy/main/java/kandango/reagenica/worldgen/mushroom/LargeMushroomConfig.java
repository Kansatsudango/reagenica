package kandango.reagenica.worldgen.mushroom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record LargeMushroomConfig(
  IntProvider height,
  BlockStateProvider stem,
  BlockStateProvider inside,
  BlockStateProvider surface
) implements FeatureConfiguration {
  public static final Codec<LargeMushroomConfig> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.CODEC.fieldOf("height").forGetter(LargeMushroomConfig::height),
            BlockStateProvider.CODEC.fieldOf("stem").forGetter(LargeMushroomConfig::stem),
            BlockStateProvider.CODEC.fieldOf("inside").forGetter(LargeMushroomConfig::inside),
            BlockStateProvider.CODEC.fieldOf("surface").forGetter(LargeMushroomConfig::surface)
        ).apply(instance, LargeMushroomConfig::new));
}
