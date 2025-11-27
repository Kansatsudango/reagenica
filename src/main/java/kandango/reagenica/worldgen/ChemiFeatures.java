package kandango.reagenica.worldgen;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChemiFeatures {
  public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, ChemistryMod.MODID);

  public static final RegistryObject<Feature<NoneFeatureConfiguration>> OIL_RESERVOIR = FEATURES.register("oil_reservoir",() -> new OilReservoirFeature(NoneFeatureConfiguration.CODEC));
}
