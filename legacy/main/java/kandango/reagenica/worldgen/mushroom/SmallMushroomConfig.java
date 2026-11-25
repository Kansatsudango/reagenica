package kandango.reagenica.worldgen.mushroom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SmallMushroomConfig(
  BlockStateProvider stem,
  BlockStateProvider inside,
  BlockStateProvider surface
) implements FeatureConfiguration {
  public static final Codec<SmallMushroomConfig> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("stem").forGetter(SmallMushroomConfig::stem),
            BlockStateProvider.CODEC.fieldOf("inside").forGetter(SmallMushroomConfig::inside),
            BlockStateProvider.CODEC.fieldOf("surface").forGetter(SmallMushroomConfig::surface)
        ).apply(instance, SmallMushroomConfig::new));
}
