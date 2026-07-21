package kandango.reagenica.world;

import java.util.Set;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiPOIs {
  public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, ChemistryMod.MODID);
  public static final DeferredHolder<PoiType, PoiType> STALL_POI = POI_TYPES.register("stall_poi",
    () -> new PoiType(Set.copyOf(ChemiBlocks.TRADING_STALL.get().getStateDefinition().getPossibleStates()),1,1)
  );

  public static final DeferredHolder<PoiType, PoiType> PALEO_PORTAL_POI = POI_TYPES.register("geologist_portal", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.PALEO_PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1)
  );

  public static final DeferredHolder<PoiType, PoiType> CHEMIST_POI = POI_TYPES.register("chemist_poi", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.EXPERIMENT_BLOCK.get().getStateDefinition().getPossibleStates()), 1, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> GEOLOGIST_POI = POI_TYPES.register("geologist_poi", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.ANALYZER.get().getStateDefinition().getPossibleStates()), 1, 1)
  );
  
  public static final DeferredHolder<PoiType, PoiType> AQUAMARINE_POI = POI_TYPES.register("aquamarine_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.AQUAMARINE.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> EMERALD_POI = POI_TYPES.register("emerald_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.EMERALD.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> RED_BERYL_POI = POI_TYPES.register("red_beryl_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.RED_BERYL.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> CITRINE_POI = POI_TYPES.register("citrine_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.CITRINE.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> ROSE_QUARTZ_POI = POI_TYPES.register("rose_quartz_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.ROSE_QUARTZ.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> MORION_POI = POI_TYPES.register("morion_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.MORION.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> LAPISQUARTZ_POI = POI_TYPES.register("lapisquartz_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.LAPISQUARTZ.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );
  public static final DeferredHolder<PoiType, PoiType> PERIDOT_POI = POI_TYPES.register("peridot_budding", 
    () -> new PoiType(Set.copyOf(ChemiBlocks.PERIDOT.BUDDING_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1)
  );

  public static void register(IEventBus modEventbus){
    POI_TYPES.register(modEventbus);
  }
}
