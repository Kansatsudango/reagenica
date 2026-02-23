package kandango.reagenica;

import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.fluid.ChemiFluidTypes;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.screen.ModMenus;
import kandango.reagenica.world.ChemiPOIs;
import kandango.reagenica.worldgen.ChemiBiomes;
import kandango.reagenica.worldgen.ChemiFeatures;
import kandango.reagenica.worldgen.ChemiFoliagePlacers;
import kandango.reagenica.worldgen.ChemiStructures;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ChemistryMod.MODID)
public class ChemistryMod {
  public static final String MODID = "reagenica";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
  
  public ChemistryMod() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ChemiFluidTypes.FLUID_TYPES.register(modEventBus);
    ChemiFluids.FLUIDS.register(modEventBus);
    ChemiItems.ITEMS.register(modEventBus);
    ChemiBlocks.BLOCKS.register(modEventBus);
    ChemiBlocks.ITEMS.register(modEventBus);
    ChemiEntities.ENTITIES.register(modEventBus);
    ChemiFeatures.FEATURES.register(modEventBus);
    ChemiFoliagePlacers.FOLIAGE_PLACERS.register(modEventBus);
    ChemiFoliagePlacers.TRUNK_PLACERS.register(modEventBus);
    ChemiParticles.PARTICLES.register(modEventBus);
    ChemiBiomes.BIOME_SOURCE.register(modEventBus);
    ChemiStructures.STRUCTURE_TYPES.register(modEventBus);
    ChemiStructures.PIECES.register(modEventBus);
    ModBlockEntities.register(modEventBus);
    ModMenus.register(modEventBus);
    ModRecipes.register(modEventBus);
    ModCreativeTabs.register(modEventBus);
    ChemiPOIs.register(modEventBus);
    ChemiGameRules.init();
  }
}
