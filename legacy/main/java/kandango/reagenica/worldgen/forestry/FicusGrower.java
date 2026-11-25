package kandango.reagenica.worldgen.forestry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.worldgen.ChemiFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class FicusGrower extends AbstractTreeGrower{
  @Override
  @Nullable
  protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@Nonnull RandomSource random, boolean hasFlowers) {
    return ChemiFeatures.FICUS;
  }
}
