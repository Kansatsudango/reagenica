package kandango.reagenica.worldgen.cave;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record CaveCrystalPatchConfig(
  BlockStateProvider crystal,
  IntProvider count
) implements FeatureConfiguration {
  public static final Codec<CaveCrystalPatchConfig> CODEC =
    RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("crystal").forGetter(CaveCrystalPatchConfig::crystal),
            IntProvider.CODEC.fieldOf("count").forGetter(CaveCrystalPatchConfig::count)
        ).apply(instance, CaveCrystalPatchConfig::new));
}
