package kandango.reagenica;

import net.neoforged.fml.common.Mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.capability.ChemiCapabilities;
import kandango.reagenica.block.fluid.ChemiFluidTypes;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.screen.ModMenus;
import kandango.reagenica.villager.ChemiVillagerProfessions;
import kandango.reagenica.world.ChemiPOIs;
import kandango.reagenica.worldgen.ChemiBiomes;
import kandango.reagenica.worldgen.ChemiFeatures;
import kandango.reagenica.worldgen.ChemiFoliagePlacers;
import kandango.reagenica.worldgen.ChemiStructures;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;

@Mod(ChemistryMod.MODID)
public class ChemistryMod {
  public static final String MODID = "reagenica";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
  
  public ChemistryMod(IEventBus modEventBus) {
    ChemiFluidTypes.FLUID_TYPES.register(modEventBus);
    ChemiFluids.FLUIDS.register(modEventBus);
    ChemiItems.ITEMS.register(modEventBus);
    ChemiBlocks.BLOCKS.register(modEventBus);
    ChemiBlocks.ITEMS.register(modEventBus);
    ChemiEntities.ENTITIES.register(modEventBus);
    ChemiFeatures.FEATURES.register(modEventBus);
    ChemiComponents.DATA_COMPONENTS.register(modEventBus);
    ChemiFoliagePlacers.FOLIAGE_PLACERS.register(modEventBus);
    ChemiFoliagePlacers.TRUNK_PLACERS.register(modEventBus);
    ChemiParticles.PARTICLES.register(modEventBus);
    ChemiSounds.SOUND_EVENTS.register(modEventBus);
    ChemiBiomes.BIOME_SOURCE.register(modEventBus);
    ChemiStructures.STRUCTURE_TYPES.register(modEventBus);
    ChemiStructures.PIECES.register(modEventBus);
    ChemiVillagerProfessions.PROFESSIONS.register(modEventBus);
    ModBlockEntities.register(modEventBus);
    ModMenus.register(modEventBus);
    ModRecipes.register(modEventBus);
    ModCreativeTabs.register(modEventBus);
    ChemiPOIs.register(modEventBus);
    ChemiGameRules.init();
    attachEvent(modEventBus);
  }

  public void attachEvent(IEventBus modEventBus){
    modEventBus.addListener(EventPriority.NORMAL, ChemiCapabilities::registerCapabilities);
  }
}
