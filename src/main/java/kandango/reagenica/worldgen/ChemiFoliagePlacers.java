package kandango.reagenica.worldgen;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.worldgen.forestry.MetasequoiaFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChemiFoliagePlacers {
  public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = 
                                  DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, ChemistryMod.MODID);
  
  public static final RegistryObject<FoliagePlacerType<MetasequoiaFoliagePlacer>> METASEQUOIA = 
          FOLIAGE_PLACERS.register("metasequoia", () -> new FoliagePlacerType<>(MetasequoiaFoliagePlacer.CODEC));
}
