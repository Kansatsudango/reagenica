package kandango.reagenica.worldgen;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.worldgen.mushroom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChemiFeatures {
  public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, ChemistryMod.MODID);

  public static final RegistryObject<Feature<NoneFeatureConfiguration>> OIL_RESERVOIR = FEATURES.register("oil_reservoir",() -> new OilReservoirFeature(NoneFeatureConfiguration.CODEC));
  public static final RegistryObject<Feature<LargeMushroomConfig>> LARGE_MUSHROOM = FEATURES.register("large_mushroom",() -> new LargeMushroomFeature(LargeMushroomConfig.CODEC));
  public static final RegistryObject<Feature<SmallMushroomConfig>> SMALL_MUSHROOM = FEATURES.register("small_mushroom",() -> new SmallMushroomFeature(SmallMushroomConfig.CODEC));

  public static final ResourceKey<ConfiguredFeature<?, ?>> METASEQUOIA = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ChemistryMod.MODID, "metasequoia"));
  public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_METASEQUOIA = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ChemistryMod.MODID, "mega_metasequoia"));
  public static final ResourceKey<ConfiguredFeature<?, ?>> TAXODIUM = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ChemistryMod.MODID, "taxodium"));
  public static final ResourceKey<ConfiguredFeature<?, ?>> GINKGO = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ChemistryMod.MODID, "ginkgo"));
  public static final ResourceKey<ConfiguredFeature<?, ?>> MAGNOLIA = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ChemistryMod.MODID, "magnolia"));
}
