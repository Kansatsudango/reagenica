package kandango.reagenica;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import kandango.reagenica.block.*;
import kandango.reagenica.block.farming.*;
import kandango.reagenica.block.farming.crop.*;
import kandango.reagenica.block.farming.grape.*;
import kandango.reagenica.generator.BlockLootType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = ChemistryMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ChemiBlocks {

  public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, ChemistryMod.MODID);

  public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(ForgeRegistries.ITEMS, ChemistryMod.MODID);
  
  public static List<BlockLootType> listBlocks = new ArrayList<>();
  public static List<RegistryObject<Item>> listBlockItems = new ArrayList<>();
  public static List<RegistryObject<LiquidBlock>> listLiquids = new ArrayList<>();

  public static final RegistryObject<Block> CUSTOM_BLOCK = registerBlockandlist("custom_block",
    () -> new Block(BlockBehaviour.Properties.of().strength(2.0f, 3.0f).sound(SoundType.STONE)));
  public static final RegistryObject<Item> CUSTOM_BLOCK_ITEM = registerItemandlist("custom_block",
    () -> new BlockItem(CUSTOM_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Block> BUGGED_BLOCK = registerBlockandlist("bugged_block",
    () -> new Block(BlockBehaviour.Properties.of().strength(0.05f, 1000.0f).sound(SoundType.SCULK)));
  public static final RegistryObject<Item> BUGGED_BLOCK_ITEM = registerItemandlist("bugged_block",
    () -> new BlockItem(BUGGED_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Block> EXPERIMENT_BLOCK = registerMachineBlockandlist("experiment_block", ExperimentBlock::new);

  public static final RegistryObject<Item> EXPERIMENT_BLOCK_ITEM = registerItemandlist("experiment_block",
      () -> new BlockItem(EXPERIMENT_BLOCK.get(), new Item.Properties()));
  
  public static final RegistryObject<Block> CHEMICAL_FERMENTER = registerMachineBlockandlist("chemical_fermenter", ChemicalFermenter::new);

  public static final RegistryObject<Item> CHEMICAL_FERMENTER_ITEM = registerItemandlist("chemical_fermenter",
      () -> new BlockItem(CHEMICAL_FERMENTER.get(), new Item.Properties()));
  
  public static final RegistryObject<Block> FRACTIONAL_DISTILLER_TOP = registerMachineBlockandlist("fractional_distiller_top", FractionalDistillerTop::new); 
  public static final RegistryObject<Block> FRACTIONAL_DISTILLER_BOTTOM = registerMachineBlockandlist("fractional_distiller_bottom", FractionalDistillerBottom::new); 
  public static final RegistryObject<Item> FRACTIONAL_DISTILLER_ITEM = registerItemandlist("fractional_distiller",
      () -> new FractionalDistillerBlockItem(FRACTIONAL_DISTILLER_BOTTOM.get(), new Item.Properties()));
  
  public static final RegistryObject<Block> DEBUG_GENERTOR = registerBlockandlist("debug_generator", DebugGenerator::new); 
  public static final RegistryObject<Item> DEBUG_GENERATOR_ITEM = registerItemandlist("debug_generator",
      () -> new BlockItem(DEBUG_GENERTOR.get(), new Item.Properties()));

  public static final RegistryObject<Block> DEBUG_CONSUMER = registerBlockandlist("debug_consumer", DebugEnergyConsumer::new); 
  public static final RegistryObject<Item> DEBUG_CONSUMER_ITEM = registerItemandlist("debug_consumer",
      () -> new BlockItem(DEBUG_CONSUMER.get(), new Item.Properties()));
    
  public static final RegistryObject<Block> CABLE_COPPER = registerBlockandlist("copper_cable", CableCopper::new); 
  public static final RegistryObject<Item> CABLE_COPPER_ITEM = registerItemandlist("copper_cable",
      () -> new BlockItem(CABLE_COPPER.get(), new Item.Properties()));

  public static final RegistryObject<Block> FUEL_GENERATOR = registerMachineBlockandlist("fuel_generator", FuelGenerator::new); 
  public static final RegistryObject<Item> FUEL_GENERATOR_ITEM = registerItemandlist("fuel_generator",
      () -> new BlockItem(FUEL_GENERATOR.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> FLUID_FUEL_GENERATOR = registerMachineBlockandlist("fluid_fuel_generator", FluidFuelGenerator::new); 
  public static final RegistryObject<Item> FLUID_FUEL_GENERATOR_ITEM = registerItemandlist("fluid_fuel_generator",
      () -> new BlockItem(FLUID_FUEL_GENERATOR.get(), new Item.Properties()));

  public static final RegistryObject<Block> HEAT_GENERATOR = registerBlockandlist("heat_generator", HeatGenerator::new); 
  public static final RegistryObject<Item> HEAT_GENERATOR_ITEM = registerItemandlist("heat_generator",
      () -> new BlockItem(HEAT_GENERATOR.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> RADIOISOTOPE_GENERATOR = registerBlockandlist("radioisotope_generator", RadioIsotopeGenerator::new);
  public static final RegistryObject<Item> RADIOISOTOPE_GENERATOR_ITEM = registerItemandlist("radioisotope_generator",
      () -> new BlockItem(RADIOISOTOPE_GENERATOR.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> SOLAR_POWER_GENERATOR = registerMachineBlockandlist("solar_power_generator", SolarPowerGenerator::new); 
  public static final RegistryObject<Item> SOLAR_POWER_GENERATOR_ITEM = registerItemandlist("solar_power_generator",
      () -> new BlockItem(SOLAR_POWER_GENERATOR.get(), new Item.Properties()));

  public static final RegistryObject<Block> BLASTFURNACE_BOTTOM = registerMachineBlockandlist("blast_furnace_bottom", BlastFurnaceBottom::new); 
  public static final RegistryObject<Block> BLASTFURNACE_SUB = registerMachineBlockandlist("blast_furnace_sub", BlastFurnaceSub::new);
  public static final RegistryObject<Item> BLASTFURNACE_ITEM = registerItemandlist("large_blast_furnace",
      () -> new BlastFurnaceBlockItem(BLASTFURNACE_BOTTOM.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> HEAT_FURNACE = registerMachineBlockandlist("heat_furnace", HeatFurnace::new); 
  public static final RegistryObject<Item> HEAT_FURNACE_ITEM = registerItemandlist("heat_furnace",
      () -> new BlockItem(HEAT_FURNACE.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> CRUSHER = registerMachineBlockandlist("crusher", Crusher::new); 
  public static final RegistryObject<Item> CRUSHER_ITEM = registerItemandlist("crusher",
      () -> new BlockItem(CRUSHER.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> REACTOR = registerMachineBlockandlist("reactor", Reactor::new); 
  public static final RegistryObject<Item> REACTOR_ITEM = registerItemandlist("reactor",
      () -> new BlockItem(REACTOR.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> ANALYZER = registerMachineBlockandlist("analyzer", Analyzer::new); 
  public static final RegistryObject<Item> ANALYZER_ITEM = registerItemandlist("analyzer",
      () -> new BlockItem(ANALYZER.get(), new Item.Properties()));

  public static final RegistryObject<Block> ELECTROLYSIS_DEVICE = registerMachineBlockandlist("electrolysis_device", ElectroLysisDevice::new); 
  public static final RegistryObject<Item> ELECTROLYSIS_DEVICE_ITEM = registerItemandlist("electrolysis_device",
      () -> new BlockItem(ELECTROLYSIS_DEVICE.get(), new Item.Properties()));

  public static final RegistryObject<Block> COPPER_TANK = registerMachineBlockandlist("copper_tank",CopperTank::new);
  public static final RegistryObject<Item> COPPER_TANK_ITEM = registerItemandlist("copper_tank",
    () -> new BlockItem(COPPER_TANK.get(), new Item.Properties()));
    
  public static final RegistryObject<Block> DISSOLVER = registerMachineBlockandlist("dissolver",Dissolver::new);
  public static final RegistryObject<Item> DISSOLVER_ITEM = registerItemandlist("dissolver",
    () -> new BlockItem(DISSOLVER.get(), new Item.Properties()));

  public static final RegistryObject<Block> LEAD_BATTERY = registerBatteryBlockandlist("lead_battery", LeadBattery::new); 
  public static final RegistryObject<Item> LEAD_BATTERY_ITEM = registerItemandlist("lead_battery",
      () -> new ElectricBlockItem(LEAD_BATTERY.get(), new Item.Properties()));

  public static final RegistryObject<Block> STACK_LAMP = registerBlockandlist("stack_lamp", StackLamp::new); 
  public static final RegistryObject<Item> STACK_LAMP_ITEM = registerItemandlist("stack_lamp",
      () -> new BlockItem(STACK_LAMP.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> COOKING_POT = registerMachineBlockandlist("cooking_pot",CookingPot::new);
  public static final RegistryObject<Item> COOKING_POT_ITEM = registerItemandlist("cooking_pot",
    () -> new BlockItem(COOKING_POT.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> INCUBATOR = registerMachineBlockandlist("incubator",Incubator::new);
  public static final RegistryObject<Item> INCUBATOR_ITEM = registerItemandlist("incubator",
    () -> new BlockItem(INCUBATOR.get(), new Item.Properties()));
    
  public static final RegistryObject<Block> AIR_SEPARATOR = registerMachineBlockandlist("air_separator",AirSeparator::new);
  public static final RegistryObject<Item> AIR_SEPARATOR_ITEM = registerItemandlist("air_separator",
    () -> new BlockItem(AIR_SEPARATOR.get(), new Item.Properties()));
    
  public static final RegistryObject<Block> HABER_BOSCH = registerMachineBlockandlist("haber_bosch",HaberBosch::new);
  public static final RegistryObject<Item> HABER_BOSCH_ITEM = registerItemandlist("haber_bosch",
    () -> new BlockItem(HABER_BOSCH.get(), new Item.Properties()));
    
  public static final RegistryObject<Block> LARGE_TANK_CORE = registerMachineBlockandlist("large_tank_core",LargeTankCore::new);
  public static final RegistryObject<Item> LARGE_TANK_CORE_ITEM = registerItemandlist("large_tank_core",
    () -> new BlockItem(LARGE_TANK_CORE.get(), new Item.Properties()));
  public static final RegistryObject<Block> LARGE_TANK_INTERFACE = registerMachineBlockandlist("large_tank_interface",LargeTankInterface::new);
  public static final RegistryObject<Item> LARGE_TANK_INTERFACE_ITEM = registerItemandlist("large_tank_interface",
    () -> new BlockItem(LARGE_TANK_INTERFACE.get(), new Item.Properties()));
    
  public static final RegistryObject<Block> COMPUTER = registerMachineBlockandlist("computer",Computer::new);
  public static final RegistryObject<Item> COMPUTER_ITEM = registerItemandlist("computer",
    () -> new BlockItem(COMPUTER.get(), new Item.Properties()));

  public static final RegistryObject<Block> ONSEN_MINER = registerMachineBlockandlist("hotspring_miner",OnsenMiner::new);
  public static final RegistryObject<Item> ONSEN_MINER_ITEM = registerItemandlist("hotspring_miner",
    () -> new BlockItem(ONSEN_MINER.get(), new Item.Properties()));
  public static final RegistryObject<Block> ONSEN_FILLER_WOOD = registerWoodBlockandlist("spring_outlet_wood",OnsenFillerWood::new);
  public static final RegistryObject<Item> ONSEN_FILLER_WOOD_ITEM = registerItemandlist("spring_outlet_wood",
    () -> new BlockItem(ONSEN_FILLER_WOOD.get(), new Item.Properties()));
  public static final RegistryObject<Block> ONSEN_FILLER_STONE = registerStoneBlockandlist("spring_outlet_stone",OnsenFillerStone::new);
  public static final RegistryObject<Item> ONSEN_FILLER_STONE_ITEM = registerItemandlist("spring_outlet_stone",
    () -> new BlockItem(ONSEN_FILLER_STONE.get(), new Item.Properties()));

  public static final RegistryObject<Block> LEAD_ORE = registerOreBlockandlist("lead_ore", OresConfig::stoneOres, 1, () -> ChemiItems.RAW_LEAD, 2); 
  public static final RegistryObject<Item> LEAD_ORE_ITEM = registerItemandlist("lead_ore",
      () -> new BlockItem(LEAD_ORE.get(), new Item.Properties()));
  public static final RegistryObject<Block> LEAD_DEEPSLATE_ORE = registerOreBlockandlist("deepslate_lead_ore", OresConfig::deepslateOres, 1, () -> ChemiItems.RAW_LEAD, 2); 
  public static final RegistryObject<Item> LEAD_DEEPSLATE_ORE_ITEM = registerItemandlist("deepslate_lead_ore",
      () -> new BlockItem(LEAD_DEEPSLATE_ORE.get(), new Item.Properties()));

  public static final RegistryObject<Block> CHALCOPYRITE_ORE = registerOreBlockandlist("chalcopyrite_ore", OresConfig::stoneOres, 3, () -> ChemiItems.RAW_CHALCOPYRITE, 1);
  public static final RegistryObject<Item> CHALCOPYRITE_ORE_ITEM = registerItemandlist("chalcopyrite_ore",
      () -> new BlockItem(CHALCOPYRITE_ORE.get(), new Item.Properties()));
  public static final RegistryObject<Block> CHALCOPYRITE_DEEPSLATE_ORE = registerOreBlockandlist("deepslate_chalcopyrite_ore", OresConfig::deepslateOres, 3, () -> ChemiItems.RAW_CHALCOPYRITE, 1);
  public static final RegistryObject<Item> CHALCOPYRITE_DEEPSLATE_ORE_ITEM = registerItemandlist("deepslate_chalcopyrite_ore",
      () -> new BlockItem(CHALCOPYRITE_DEEPSLATE_ORE.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> BAUXITE_ORE = registerOreBlockandlist("bauxite_ore", OresConfig::stoneOres, 1, () -> ChemiItems.RAW_BAUXITE, 2);
  public static final RegistryObject<Item> BAUXITE_ORE_ITEM = registerItemandlist("bauxite_ore",
      () -> new BlockItem(BAUXITE_ORE.get(), new Item.Properties()));
  public static final RegistryObject<Block> BAUXITE_DEEPSLATE_ORE = registerOreBlockandlist("deepslate_bauxite_ore", OresConfig::deepslateOres, 1, () -> ChemiItems.RAW_BAUXITE, 2);
  public static final RegistryObject<Item> BAUXITE_DEEPSLATE_ORE_ITEM = registerItemandlist("deepslate_bauxite_ore",
      () -> new BlockItem(BAUXITE_DEEPSLATE_ORE.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> APATITE_ORE = registerOreBlockandlist("apatite_ore", OresConfig::stoneOres, 3, () -> ChemiItems.RAW_APATITE, 1);
  public static final RegistryObject<Item> APATITE_ORE_ITEM = registerItemandlist("apatite_ore",
      () -> new BlockItem(APATITE_ORE.get(), new Item.Properties()));
  public static final RegistryObject<Block> APATITE_DEEPSLATE_ORE = registerOreBlockandlist("deepslate_apatite_ore", OresConfig::deepslateOres, 3, () -> ChemiItems.RAW_APATITE, 1);
  public static final RegistryObject<Item> APATITE_DEEPSLATE_ORE_ITEM = registerItemandlist("deepslate_apatite_ore",
      () -> new BlockItem(APATITE_DEEPSLATE_ORE.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> OILSAND_ORE = registerOreBlockandlist("oilsand_ore", OresConfig::stoneOres, 1, () -> ChemiItems.OIL_SAND, 1);
  public static final RegistryObject<Item> OILSAND_ORE_ITEM = registerItemandlist("oilsand_ore",
      () -> new BlockItem(OILSAND_ORE.get(), new Item.Properties()));
  public static final RegistryObject<Block> OILSAND_DEEPSLATE_ORE = registerOreBlockandlist("deepslate_oilsand_ore", OresConfig::deepslateOres, 1, () -> ChemiItems.OIL_SAND, 1);
  public static final RegistryObject<Item> OILSAND_DEEPSLATE_ORE_ITEM = registerItemandlist("deepslate_oilsand_ore",
      () -> new BlockItem(OILSAND_DEEPSLATE_ORE.get(), new Item.Properties()));

  public static final RegistryObject<Block> CRYOLITE = registerStoneBlockandlist("cryolite", () -> new Block(BlockBehaviour.Properties.of().strength(2.0f, 3.0f).sound(SoundType.CALCITE)));
  public static final RegistryObject<Item> CRYOLITE_ITEM = registerItemandlist("cryolite",
      () -> new BlockItem(CRYOLITE.get(), new Item.Properties()));

  public static final RegistryObject<Block> TRADING_STALL = registerMachineBlockandlist("trading_stall", TradingStall::new); 
  public static final RegistryObject<Item> TRADING_STALL_ITEM = registerItemandlist("trading_stall",
      () -> new BlockItem(TRADING_STALL.get(), new Item.Properties()));

  public static final RegistryObject<Block> ADVANCED_FARMLAND = registerSilktouchBlockandlist("advanced_farmland", AdvancedFarmland::new);
  public static final RegistryObject<Item> ADVANCED_FARMLAND_ITEM = registerItemandlist("advanced_farmland",
      () -> new BlockItem(ADVANCED_FARMLAND.get(), new Item.Properties()));

  public static final RegistryObject<Block> DEBUG_CROP = registerPlantBlockandlist("debug_crop", () -> new AdvancedCropBlock(ChemiBlocks.DEBUG_CROP_SEEDS::get),2,() -> ChemiBlocks.DEBUG_CROP_SEEDS);
  public static final RegistryObject<Item> DEBUG_CROP_SEEDS = registerItemandlist("debug_seeds", 
      () -> new ItemNameBlockItem(DEBUG_CROP.get(), new Item.Properties()));
  public static final RegistryObject<Block> SOYBEAN_CROP = registerPlantBlockandlist("soybeans_crop", () -> new AdvancedCropBlock(ChemiBlocks.SOYBEAN_SEEDS::get),2,() -> ChemiBlocks.SOYBEAN_SEEDS);
  public static final RegistryObject<Item> SOYBEAN_SEEDS = registerItemandlist("soybeans", 
      () -> new ItemNameBlockItem(SOYBEAN_CROP.get(), new Item.Properties()));
  public static final RegistryObject<Block> RICE_CROP = registerPlantBlockandlist("rice_crop", () -> new AdvancedCropBlock(ChemiBlocks.RICE_SEEDS::get),2,() -> ChemiBlocks.RICE_SEEDS);
  public static final RegistryObject<Item> RICE_SEEDS = registerItemandlist("rice", 
      () -> new ItemNameBlockItem(RICE_CROP.get(), new Item.Properties()));
  public static final RegistryObject<Block> ONION_CROP = registerNoneBlockandlist("onion_crop", () -> new AdvancedCropBlock(ChemiBlocks.ONION_SEEDS::get));//Using manually provided loottable
  public static final RegistryObject<Item> ONION_SEEDS = registerItemandlist("onion", 
      () -> new ItemNameBlockItem(ONION_CROP.get(), new Item.Properties()));
  public static final RegistryObject<Item> ONION_SEEDS_PURPLE = registerItemandlist("purple_onion", 
      () -> new ItemNameBlockItem(ONION_CROP.get(), new Item.Properties()));
  public static final RegistryObject<Block> TOMATO_CROP = registerPlantBlockandlist("tomato_crop", () -> new AdvancedCropBlock(ChemiBlocks.TOMATO_SEEDS::get),2,() -> ChemiBlocks.TOMATO_SEEDS);
  public static final RegistryObject<Item> TOMATO_SEEDS = registerItemandlist("tomato", 
      () -> new ItemNameBlockItem(TOMATO_CROP.get(), new Item.Properties()));
  public static final RegistryObject<Block> CORN_CROP = registerPlantBlockandlist("corn_crop", () -> new AdvancedCropBlock(ChemiBlocks.CORN_SEEDS::get),2,() -> ChemiBlocks.CORN_SEEDS);
  public static final RegistryObject<Item> CORN_SEEDS = registerItemandlist("corn", 
      () -> new ItemNameBlockItem(CORN_CROP.get(), new Item.Properties()));

  public static final RegistryObject<Block> GRAPE_STEM = registerSilktouchBlockandlist("grape_stem", GrapeStem::new);
  public static final RegistryObject<Item> GRAPE_STEM_ITEM = registerItemandlist("grape_stem",
      () -> new BlockItem(GRAPE_STEM.get(), new Item.Properties()));
  public static final RegistryObject<Block> PARGOLA = registerWoodBlockandlist("pargola", Pargola::new);
  public static final RegistryObject<Item> PARGOLA_ITEM = registerItemandlist("pargola",
      () -> new BlockItem(PARGOLA.get(), new Item.Properties()));
  public static final RegistryObject<Block> GRAPE_PARGOLA = registerHoeBlockandlist("grape_pargola", GrapePargola::new);
  public static final RegistryObject<Item> GRAPE_PARGOLA_ITEM = registerItemandlist("grape_pargola",
      () -> new BlockItem(GRAPE_PARGOLA.get(), new Item.Properties()));
  public static final RegistryObject<Block> GRAPE_CROP = registerNoneBlockandlist("grape_crop", GrapeBlock::new);
  public static final RegistryObject<Item> GRAPE = registerItemandlist("grape", 
      () -> new Item(new Item.Properties()));
  public static final RegistryObject<Block> GRAPE_SAPLING = registerNoneBlockandlist("grape_sapling", GrapeSapling::new);
  public static final RegistryObject<Item> GRAPE_SAPLING_ITEM = registerItemandlist("grape_sapling", 
      () -> new BlockItem(GRAPE_SAPLING.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> PIPE_COPPER = registerBlockandlist("copper_pipe", PipeCopper::new); 
  public static final RegistryObject<Item> PIPE_COPPER_ITEM = registerItemandlist("copper_pipe",
      () -> new BlockItem(PIPE_COPPER.get(), new Item.Properties()));
  public static final RegistryObject<Block> PIPE_GOLD = registerBlockandlist("gold_pipe", PipeGold::new); 
  public static final RegistryObject<Item> PIPE_GOLD_ITEM = registerItemandlist("gold_pipe",
      () -> new BlockItem(PIPE_GOLD.get(), new Item.Properties()));
  public static final RegistryObject<Block> PIPE_PVC = registerBlockandlist("pvc_pipe", PipePVC::new); 
  public static final RegistryObject<Item> PIPE_PVC_ITEM = registerItemandlist("pvc_pipe",
      () -> new BlockItem(PIPE_PVC.get(), new Item.Properties()));
  public static final RegistryObject<Block> PUMP = registerBlockandlist("drain_pipe", PumpChemistry::new); 
  public static final RegistryObject<Item> PUMP_ITEM = registerItemandlist("drain_pipe",
      () -> new BlockItem(PUMP.get(), new Item.Properties()));
      
  public static final RegistryObject<Block> WILD_ONION = registerNoneBlockandlist("wild_onion", () -> new BushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.CROP).offsetType(OffsetType.XZ)));
  public static final RegistryObject<Item> WILD_ONION_ITEM = registerItemandlist("wild_onion", 
      () -> new ItemNameBlockItem(WILD_ONION.get(), new Item.Properties()));
  public static final RegistryObject<Block> WILD_RICE = registerNoneBlockandlist("wild_rice", () -> new BushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.CROP).offsetType(OffsetType.XZ)));
  public static final RegistryObject<Item> WILD_RICE_ITEM = registerItemandlist("wild_rice", 
      () -> new ItemNameBlockItem(WILD_RICE.get(), new Item.Properties()));
  public static final RegistryObject<Block> WILD_SOYBEAN = registerNoneBlockandlist("wild_soybeans", () -> new BushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.CROP).offsetType(OffsetType.XZ)));
  public static final RegistryObject<Item> WILD_SOYBEAN_ITEM = registerItemandlist("wild_soybeans", 
      () -> new ItemNameBlockItem(WILD_SOYBEAN.get(), new Item.Properties()));
  public static final RegistryObject<Block> WILD_TOMATO = registerNoneBlockandlist("wild_tomato", () -> new BushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.CROP).offsetType(OffsetType.XZ)));
  public static final RegistryObject<Item> WILD_TOMATO_ITEM = registerItemandlist("wild_tomato", 
      () -> new ItemNameBlockItem(WILD_TOMATO.get(), new Item.Properties()));
  public static final RegistryObject<Block> WILD_CORN = registerNoneBlockandlist("wild_corn", () -> new BushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.CROP).offsetType(OffsetType.XZ)));
  public static final RegistryObject<Item> WILD_CORN_ITEM = registerItemandlist("wild_corn", 
      () -> new ItemNameBlockItem(WILD_CORN.get(), new Item.Properties()));

  public static final RegistryObject<Yunohana> YUNOHANA_WHITE = registerBlockandlist("yunohana_white", Yunohana::new); 
  public static final RegistryObject<Item> YUNOHANA_WHITE_ITEM = registerItemandlist("yunohana_white",
      () -> new BlockItem(YUNOHANA_WHITE.get(), new Item.Properties()));
  public static final RegistryObject<Yunohana> YUNOHANA_YELLOW = registerBlockandlist("yunohana_yellow", Yunohana::new); 
  public static final RegistryObject<Item> YUNOHANA_YELLOW_ITEM = registerItemandlist("yunohana_yellow",
      () -> new BlockItem(YUNOHANA_YELLOW.get(), new Item.Properties()));
  public static final RegistryObject<Yunohana> YUNOHANA_RED = registerBlockandlist("yunohana_red", Yunohana::new); 
  public static final RegistryObject<Item> YUNOHANA_RED_ITEM = registerItemandlist("yunohana_red",
      () -> new BlockItem(YUNOHANA_RED.get(), new Item.Properties()));
  public static final RegistryObject<Yunohana> YUNOHANA_SPOTTED = registerBlockandlist("yunohana_spotted", Yunohana::new); 
  public static final RegistryObject<Item> YUNOHANA_SPOTTED_ITEM = registerItemandlist("yunohana_spotted",
      () -> new BlockItem(YUNOHANA_SPOTTED.get(), new Item.Properties()));
  public static final RegistryObject<Yunohana> YUNOHANA_DARK_YELLOW = registerBlockandlist("yunohana_dark_yellow", Yunohana::new); 
  public static final RegistryObject<Item> YUNOHANA_DARK_YELLOW_ITEM = registerItemandlist("yunohana_dark_yellow",
      () -> new BlockItem(YUNOHANA_DARK_YELLOW.get(), new Item.Properties()));

  public static final RegistryObject<Block> URANIUM_GLASS = registerSilktouchBlockandlist("uranium_glass",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> 7).noOcclusion()));
  public static final RegistryObject<Item> URANIUM_GLASS_ITEM = registerItemandlist("uranium_glass",
      () -> new BlockItem(URANIUM_GLASS.get(), new Item.Properties()));
  public static final RegistryObject<Block> LEAD_GLASS = registerSilktouchBlockandlist("lead_glass",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
  public static final RegistryObject<Item> LEAD_GLASS_ITEM = registerItemandlist("lead_glass",
      () -> new BlockItem(LEAD_GLASS.get(), new Item.Properties()));
  public static final RegistryObject<Block> CUT_LEAD_GLASS = registerSilktouchBlockandlist("cut_lead_glass",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
  public static final RegistryObject<Item> CUT_LEAD_GLASS_ITEM = registerItemandlist("cut_lead_glass",
      () -> new BlockItem(CUT_LEAD_GLASS.get(), new Item.Properties()));

  public static final RegistryObject<Block> GAS_LAMP = registerStoneBlockandlist("gas_lamp",GasLamp::new);
  public static final RegistryObject<Item> GAS_LAMP_ITEM = registerItemandlist("gas_lamp",
    () -> new BlockItem(GAS_LAMP.get(), new Item.Properties()));
  public static final RegistryObject<Block> OIL_LAMP = registerStoneBlockandlist("oil_lamp",OilLamp::new);
  public static final RegistryObject<Item> OIL_LAMP_ITEM = registerItemandlist("oil_lamp",
    () -> new BlockItem(OIL_LAMP.get(), new Item.Properties()));

  public static final RegistryObject<Block> LEAD_BLOCK = registerStoneBlockandlist("lead_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)
                    .strength(4.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> LEAD_BLOCK_ITEM = registerItemandlist("lead_block",
      () -> new BlockItem(LEAD_BLOCK.get(), new Item.Properties()));
  public static final RegistryObject<Block> REFINED_COPPER_BLOCK = registerStoneBlockandlist("refined_copper_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(4.0F).sound(SoundType.COPPER)));
  public static final RegistryObject<Item> REFINED_COPPER_BLOCK_ITEM = registerItemandlist("refined_copper_block",
      () -> new BlockItem(REFINED_COPPER_BLOCK.get(), new Item.Properties()));
  public static final RegistryObject<Block> ALUMINIUM_BLOCK = registerStoneBlockandlist("aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(4.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> ALUMINIUM_BLOCK_ITEM = registerItemandlist("aluminium_block",
      () -> new BlockItem(ALUMINIUM_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Block> GYPSUM = registerStoneBlockandlist("gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> GYPSUM_ITEM = registerItemandlist("gypsum",
      () -> new BlockItem(GYPSUM.get(), new Item.Properties()));
  public static final RegistryObject<Block> RED_GYPSUM = registerStoneBlockandlist("red_gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> RED_GYPSUM_ITEM = registerItemandlist("red_gypsum",
      () -> new BlockItem(RED_GYPSUM.get(), new Item.Properties()));
  public static final RegistryObject<Block> ORANGE_GYPSUM = registerStoneBlockandlist("orange_gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> ORANGE_GYPSUM_ITEM = registerItemandlist("orange_gypsum",
      () -> new BlockItem(ORANGE_GYPSUM.get(), new Item.Properties()));
  public static final RegistryObject<Block> YELLOW_GYPSUM = registerStoneBlockandlist("yellow_gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> YELLOW_GYPSUM_ITEM = registerItemandlist("yellow_gypsum",
      () -> new BlockItem(YELLOW_GYPSUM.get(), new Item.Properties()));
  public static final RegistryObject<Block> GREEN_GYPSUM = registerStoneBlockandlist("green_gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> GREEN_GYPSUM_ITEM = registerItemandlist("green_gypsum",
      () -> new BlockItem(GREEN_GYPSUM.get(), new Item.Properties()));
  public static final RegistryObject<Block> BLUE_GYPSUM = registerStoneBlockandlist("blue_gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> BLUE_GYPSUM_ITEM = registerItemandlist("blue_gypsum",
      () -> new BlockItem(BLUE_GYPSUM.get(), new Item.Properties()));
  public static final RegistryObject<Block> PURPLE_GYPSUM = registerStoneBlockandlist("purple_gypsum",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.0F).sound(SoundType.STONE)));
  public static final RegistryObject<Item> PURPLE_GYPSUM_ITEM = registerItemandlist("purple_gypsum",
      () -> new BlockItem(PURPLE_GYPSUM.get(), new Item.Properties()));

  public static final RegistryObject<Block> POLYETHYLENE_BLOCK = registerBlockandlist("polyethylene_block",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> POLYETHYLENE_BLOCK_ITEM = registerItemandlist("polyethylene_block",
      () -> new BlockItem(POLYETHYLENE_BLOCK.get(), new Item.Properties()));
  public static final RegistryObject<Block> POLYETHYLENE_PANE = registerBlockandlist("polyethylene_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> POLYETHYLENE_PANE_ITEM = registerItemandlist("polyethylene_pane",
      () -> new BlockItem(POLYETHYLENE_PANE.get(), new Item.Properties()));

  public static final RegistryObject<Block> PVC_BLOCK = registerBlockandlist("pvc_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> PVC_BLOCK_ITEM = registerItemandlist("pvc_block",
      () -> new BlockItem(PVC_BLOCK.get(), new Item.Properties()));

  public static final RegistryObject<Block> PVC_PANE = registerBlockandlist("pvc_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> PVC_PANE_ITEM = registerItemandlist("pvc_pane",
      () -> new BlockItem(PVC_PANE.get(), new Item.Properties()));

  public static final RegistryObject<Block> RED_PLASTIC = registerBlockandlist("red_plastic",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> RED_PLASTIC_ITEM = registerItemandlist("red_plastic",
      () -> new BlockItem(RED_PLASTIC.get(), new Item.Properties()));
  public static final RegistryObject<Block> ORANGE_PLASTIC = registerBlockandlist("orange_plastic",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> ORANGE_PLASTIC_ITEM = registerItemandlist("orange_plastic",
      () -> new BlockItem(ORANGE_PLASTIC.get(), new Item.Properties()));
  public static final RegistryObject<Block> YELLOW_PLASTIC = registerBlockandlist("yellow_plastic",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> YELLOW_PLASTIC_ITEM = registerItemandlist("yellow_plastic",
      () -> new BlockItem(YELLOW_PLASTIC.get(), new Item.Properties()));
  public static final RegistryObject<Block> GREEN_PLASTIC = registerBlockandlist("green_plastic",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> GREEN_PLASTIC_ITEM = registerItemandlist("green_plastic",
      () -> new BlockItem(GREEN_PLASTIC.get(), new Item.Properties()));
  public static final RegistryObject<Block> BLUE_PLASTIC = registerBlockandlist("blue_plastic",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> BLUE_PLASTIC_ITEM = registerItemandlist("blue_plastic",
      () -> new BlockItem(BLUE_PLASTIC.get(), new Item.Properties()));
  public static final RegistryObject<Block> PURPLE_PLASTIC = registerBlockandlist("purple_plastic",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> PURPLE_PLASTIC_ITEM = registerItemandlist("purple_plastic",
      () -> new BlockItem(PURPLE_PLASTIC.get(), new Item.Properties()));

  public static final RegistryObject<Block> RED_PLASTIC_TILE = registerBlockandlist("red_plastic_tile",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> RED_PLASTIC_TILE_ITEM = registerItemandlist("red_plastic_tile",
      () -> new BlockItem(RED_PLASTIC_TILE.get(), new Item.Properties()));
  public static final RegistryObject<Block> ORANGE_PLASTIC_TILE = registerBlockandlist("orange_plastic_tile",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> ORANGE_PLASTIC_TILE_ITEM = registerItemandlist("orange_plastic_tile",
      () -> new BlockItem(ORANGE_PLASTIC_TILE.get(), new Item.Properties()));
  public static final RegistryObject<Block> YELLOW_PLASTIC_TILE = registerBlockandlist("yellow_plastic_tile",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> YELLOW_PLASTIC_TILE_ITEM = registerItemandlist("yellow_plastic_tile",
      () -> new BlockItem(YELLOW_PLASTIC_TILE.get(), new Item.Properties()));
  public static final RegistryObject<Block> GREEN_PLASTIC_TILE = registerBlockandlist("green_plastic_tile",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> GREEN_PLASTIC_TILE_ITEM = registerItemandlist("green_plastic_tile",
      () -> new BlockItem(GREEN_PLASTIC_TILE.get(), new Item.Properties()));
  public static final RegistryObject<Block> BLUE_PLASTIC_TILE = registerBlockandlist("blue_plastic_tile",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> BLUE_PLASTIC_TILE_ITEM = registerItemandlist("blue_plastic_tile",
      () -> new BlockItem(BLUE_PLASTIC_TILE.get(), new Item.Properties()));
  public static final RegistryObject<Block> PURPLE_PLASTIC_TILE = registerBlockandlist("purple_plastic_tile",
            () -> new GlassLikeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> PURPLE_PLASTIC_TILE_ITEM = registerItemandlist("purple_plastic_tile",
      () -> new BlockItem(PURPLE_PLASTIC_TILE.get(), new Item.Properties()));

  public static final RegistryObject<Block> RED_PLASTIC_PANE = registerBlockandlist("red_plastic_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> RED_PLASTIC_PANE_ITEM = registerItemandlist("red_plastic_pane",
      () -> new BlockItem(RED_PLASTIC_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> ORANGE_PLASTIC_PANE = registerBlockandlist("orange_plastic_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> ORANGE_PLASTIC_PANE_ITEM = registerItemandlist("orange_plastic_pane",
      () -> new BlockItem(ORANGE_PLASTIC_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> YELLOW_PLASTIC_PANE = registerBlockandlist("yellow_plastic_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> YELLOW_PLASTIC_PANE_ITEM = registerItemandlist("yellow_plastic_pane",
      () -> new BlockItem(YELLOW_PLASTIC_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> GREEN_PLASTIC_PANE = registerBlockandlist("green_plastic_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> GREEN_PLASTIC_PANE_ITEM = registerItemandlist("green_plastic_pane",
      () -> new BlockItem(GREEN_PLASTIC_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> BLUE_PLASTIC_PANE = registerBlockandlist("blue_plastic_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> BLUE_PLASTIC_PANE_ITEM = registerItemandlist("blue_plastic_pane",
      () -> new BlockItem(BLUE_PLASTIC_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> PURPLE_PLASTIC_PANE = registerBlockandlist("purple_plastic_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> PURPLE_PLASTIC_PANE_ITEM = registerItemandlist("purple_plastic_pane",
      () -> new BlockItem(PURPLE_PLASTIC_PANE.get(), new Item.Properties()));

  public static final RegistryObject<Block> RED_PLASTIC_TILE_PANE = registerBlockandlist("red_plastic_tile_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> RED_PLASTIC_TILE_PANE_ITEM = registerItemandlist("red_plastic_tile_pane",
      () -> new BlockItem(RED_PLASTIC_TILE_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> ORANGE_PLASTIC_TILE_PANE = registerBlockandlist("orange_plastic_tile_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> ORANGE_PLASTIC_TILE_PANE_ITEM = registerItemandlist("orange_plastic_tile_pane",
      () -> new BlockItem(ORANGE_PLASTIC_TILE_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> YELLOW_PLASTIC_TILE_PANE = registerBlockandlist("yellow_plastic_tile_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> YELLOW_PLASTIC_TILE_PANE_ITEM = registerItemandlist("yellow_plastic_tile_pane",
      () -> new BlockItem(YELLOW_PLASTIC_TILE_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> GREEN_PLASTIC_TILE_PANE = registerBlockandlist("green_plastic_tile_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> GREEN_PLASTIC_TILE_PANE_ITEM = registerItemandlist("green_plastic_tile_pane",
      () -> new BlockItem(GREEN_PLASTIC_TILE_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> BLUE_PLASTIC_TILE_PANE = registerBlockandlist("blue_plastic_tile_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> BLUE_PLASTIC_TILE_PANE_ITEM = registerItemandlist("blue_plastic_tile_pane",
      () -> new BlockItem(BLUE_PLASTIC_TILE_PANE.get(), new Item.Properties()));
  public static final RegistryObject<Block> PURPLE_PLASTIC_TILE_PANE = registerBlockandlist("purple_plastic_tile_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.3F).sound(SoundType.CHERRY_WOOD).noOcclusion()));
  public static final RegistryObject<Item> PURPLE_PLASTIC_TILE_PANE_ITEM = registerItemandlist("purple_plastic_tile_pane",
      () -> new BlockItem(PURPLE_PLASTIC_TILE_PANE.get(), new Item.Properties()));

  private static RegistryObject<Item> registerItemandlist(String name, Supplier<Item> supplier) {
    RegistryObject<Item> item = ITEMS.register(name, supplier);
    listBlockItems.add(item);
    return item;
  }
  private static <T extends Block> RegistryObject<T> registerBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.normal(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerStoneBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.stone(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerWoodBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.wood(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerHoeBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.hoe(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerMachineBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.machine(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerBatteryBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.battery(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerSilktouchBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.silktouch(block));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerOreBlockandlist(String name, Supplier<T> supplier,int count, Supplier<RegistryObject<? extends Item>> item, int lev) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.ore(block, count, item, lev));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerPlantBlockandlist(String name, Supplier<T> supplier,int count, Supplier<RegistryObject<? extends Item>> veggies) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.plants(block, count, veggies));
    return block;
  }
  private static <T extends Block> RegistryObject<T> registerNoneBlockandlist(String name, Supplier<T> supplier) {
    RegistryObject<T> block = BLOCKS.register(name, supplier);
    listBlocks.add(BlockLootType.none(block));
    return block;
  }
  
  @SubscribeEvent
  public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
    }
  }
}
