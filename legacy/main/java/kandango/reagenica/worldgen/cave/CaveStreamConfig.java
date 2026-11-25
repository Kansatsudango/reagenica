package kandango.reagenica.worldgen.cave;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record CaveStreamConfig(
  BlockStateProvider ore,
  BlockStateProvider block,
  FloatProvider rockiness
) implements FeatureConfiguration {
  public static final Codec<CaveStreamConfig> CODEC =
    RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("ore").forGetter(CaveStreamConfig::ore),
            BlockStateProvider.CODEC.fieldOf("block").forGetter(CaveStreamConfig::block),
            FloatProvider.CODEC.fieldOf("rockiness").forGetter(CaveStreamConfig::rockiness)
        ).apply(instance, CaveStreamConfig::new));
}
