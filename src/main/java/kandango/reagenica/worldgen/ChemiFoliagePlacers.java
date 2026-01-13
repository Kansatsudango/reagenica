package kandango.reagenica.worldgen;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.worldgen.forestry.MegaMetasequoiaFoliagePlacer;
import kandango.reagenica.worldgen.forestry.MetasequoiaFoliagePlacer;
import kandango.reagenica.worldgen.forestry.TaxodiumFoliagePlacer;
import kandango.reagenica.worldgen.forestry.TaxodiumTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChemiFoliagePlacers {
  public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = 
                                  DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, ChemistryMod.MODID);
  public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = 
                                  DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ChemistryMod.MODID);
  
  public static final RegistryObject<FoliagePlacerType<MetasequoiaFoliagePlacer>> METASEQUOIA = 
          FOLIAGE_PLACERS.register("metasequoia", () -> new FoliagePlacerType<>(MetasequoiaFoliagePlacer.CODEC));
  public static final RegistryObject<FoliagePlacerType<MegaMetasequoiaFoliagePlacer>> MEGA_METASEQUOIA = 
          FOLIAGE_PLACERS.register("mega_metasequoia", () -> new FoliagePlacerType<>(MegaMetasequoiaFoliagePlacer.CODEC));
  public static final RegistryObject<FoliagePlacerType<TaxodiumFoliagePlacer>> TAXODIUM = 
          FOLIAGE_PLACERS.register("taxodium", () -> new FoliagePlacerType<>(TaxodiumFoliagePlacer.CODEC));
  
          public static final RegistryObject<TrunkPlacerType<TaxodiumTrunkPlacer>> TAXODIUM_TRUNK = 
          TRUNK_PLACERS.register("taxodium", () -> new TrunkPlacerType<>(TaxodiumTrunkPlacer.CODEC));
}
