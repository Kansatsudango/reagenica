package kandango.reagenica.screen;

import kandango.reagenica.ChemistryMod;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IForgeMenuType;
import net.neoforged.bus.api.IEventBus;

public class ModMenus {
  public static final DeferredRegister<MenuType<?>> MENU_TYPES =
    DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChemistryMod.MODID);

  public static final DeferredHolder<MenuType<ExperimentMenu>> EXPERIMENT_MENU =
    MENU_TYPES.register("experiment_menu",
      () -> IForgeMenuType.create(ExperimentMenu::new));
  public static final DeferredHolder<MenuType<ChemicalFermenterMenu>> CHEMICAL_FERMENTER_MENU =
    MENU_TYPES.register("chemical_fermenter",
      () -> IForgeMenuType.create(ChemicalFermenterMenu::new));
  public static final DeferredHolder<MenuType<FractionalDistillerMenu>> FRACTIONAL_DISTILLER_MENU =
    MENU_TYPES.register("fractional_distiller",
      () -> IForgeMenuType.create(FractionalDistillerMenu::new));
  public static final DeferredHolder<MenuType<DebugGeneratorMenu>> GENERATOR_DEBUG_MENU =
    MENU_TYPES.register("debug_generator",
      () -> IForgeMenuType.create(DebugGeneratorMenu::new));
  public static final DeferredHolder<MenuType<DebugEnergyConsumerMenu>> CONSUMER_DEBUG_MENU =
    MENU_TYPES.register("debug_consumer",
      () -> IForgeMenuType.create(DebugEnergyConsumerMenu::new));
  public static final DeferredHolder<MenuType<FuelGeneratorMenu>> GENERATOR_FUEL_MENU =
    MENU_TYPES.register("fuel_generator",
      () -> IForgeMenuType.create(FuelGeneratorMenu::new));
  public static final DeferredHolder<MenuType<FluidFuelGeneratorMenu>> GENERATOR_FLUID_FUEL_MENU =
    MENU_TYPES.register("fluid_fuel_generator",
      () -> IForgeMenuType.create(FluidFuelGeneratorMenu::new));
  public static final DeferredHolder<MenuType<RadioIsotopeGeneratorMenu>> GENERATOR_RI_MENU =
    MENU_TYPES.register("radioisotope_generator",
      () -> IForgeMenuType.create(RadioIsotopeGeneratorMenu::new));
  public static final DeferredHolder<MenuType<SolarPowerGeneratorMenu>> GENERATOR_SOLAR_MENU =
    MENU_TYPES.register("solar_power_generator",
      () -> IForgeMenuType.create(SolarPowerGeneratorMenu::new));
  public static final DeferredHolder<MenuType<LeadBatteryMenu>> LEAD_BATTERY_MENU =
    MENU_TYPES.register("lead_battery",
      () -> IForgeMenuType.create(LeadBatteryMenu::new));
  public static final DeferredHolder<MenuType<AdvancedLeadBatteryMenu>> ADVANCED_LEAD_BATTERY_MENU =
    MENU_TYPES.register("advanced_lead_battery",
      () -> IForgeMenuType.create(AdvancedLeadBatteryMenu::new));
  public static final DeferredHolder<MenuType<BlastFurnaceMenu>> BLAST_FURNACE_MENU =
    MENU_TYPES.register("blast_furnace",
      () -> IForgeMenuType.create(BlastFurnaceMenu::new));
  public static final DeferredHolder<MenuType<HeatFurnaceMenu>> HEAT_FURNACE_MENU =
    MENU_TYPES.register("heat_furnace",
      () -> IForgeMenuType.create(HeatFurnaceMenu::new));
  public static final DeferredHolder<MenuType<CrusherMenu>> CRUSHER_MENU =
    MENU_TYPES.register("crusher",
      () -> IForgeMenuType.create(CrusherMenu::new));
  public static final DeferredHolder<MenuType<ElectroLysisMenu>> ELECTROLYSIS_MENU =
    MENU_TYPES.register("electrolysis",
      () -> IForgeMenuType.create(ElectroLysisMenu::new));
  public static final DeferredHolder<MenuType<SimpleBagMenu>> ORE_BAG_MENU =
    MENU_TYPES.register("ore_bag",
      () -> IForgeMenuType.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.ORE_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<SimpleBagMenu>> CRYSTAL_BAG_MENU =
    MENU_TYPES.register("crystal_bag",
      () -> IForgeMenuType.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.CRYSTAL_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<SimpleBagMenu>> FARMING_BAG_MENU =
    MENU_TYPES.register("farming_bag",
      () -> IForgeMenuType.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.FARMING_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<SimpleBagMenu>> NYLON_BAG_MENU =
    MENU_TYPES.register("nylon_bag",
      () -> IForgeMenuType.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.PLATINUM_BAG_MENU.get(), 66, id, inv, buf)));
  public static final DeferredHolder<MenuType<SimpleBagMenu>> PLATINUM_BAG_MENU =
    MENU_TYPES.register("platinum_bag",
      () -> IForgeMenuType.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.PLATINUM_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<SimpleBagMenu>> IRIDIUM_BAG_MENU =
    MENU_TYPES.register("iridium_bag",
      () -> IForgeMenuType.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.IRIDIUM_BAG_MENU.get(), 140, id, inv, buf)));
  public static final DeferredHolder<MenuType<TradingStallMenu>> TRADING_STALL_MENU =
    MENU_TYPES.register("trading_stall",
      () -> IForgeMenuType.create(TradingStallMenu::new));
  public static final DeferredHolder<MenuType<TankMenu>> TANK_MENU =
    MENU_TYPES.register("tank",
      () -> IForgeMenuType.create(TankMenu::new));
  public static final DeferredHolder<MenuType<AnalyzerMenu>> ANALYZER_MENU =
    MENU_TYPES.register("analyzer",
      () -> IForgeMenuType.create(AnalyzerMenu::new));
  public static final DeferredHolder<MenuType<DissolverMenu>> DISSOLVER_MENU =
    MENU_TYPES.register("dissolver",
      () -> IForgeMenuType.create(DissolverMenu::new));
  public static final DeferredHolder<MenuType<ReactorMenu>> REACTOR_MENU =
    MENU_TYPES.register("reactor",
      () -> IForgeMenuType.create(ReactorMenu::new));
  public static final DeferredHolder<MenuType<HeatGeneratorMenu>> HEAT_GENERATOR_MENU =
    MENU_TYPES.register("heat_generator",
      () -> IForgeMenuType.create(HeatGeneratorMenu::new));
  public static final DeferredHolder<MenuType<CookingPotMenu>> COOKING_POT_MENU =
    MENU_TYPES.register("cooking_pot",
      () -> IForgeMenuType.create(CookingPotMenu::new));
  public static final DeferredHolder<MenuType<IncubatorMenu>> INCUBATOR_MENU =
    MENU_TYPES.register("incubator",
      () -> IForgeMenuType.create(IncubatorMenu::new));
  public static final DeferredHolder<MenuType<ComputerMenu>> COMPUTER_MENU =
    MENU_TYPES.register("computer",
      () -> IForgeMenuType.create((id, inv, buf) -> new ComputerMenu(id, inv)));
  public static final DeferredHolder<MenuType<AirSeparatorMenu>> AIR_SEPARATOR_MENU =
    MENU_TYPES.register("air_separator",
      () -> IForgeMenuType.create(AirSeparatorMenu::new));
  public static final DeferredHolder<MenuType<HaberBoschMenu>> HABER_BOSCH_MENU =
    MENU_TYPES.register("haber_bosch",
      () -> IForgeMenuType.create(HaberBoschMenu::new));
  public static final DeferredHolder<MenuType<HydrogenReductorMenu>> HYDROGEN_REDUCTOR_MENU =
    MENU_TYPES.register("hydrogen_reductor",
      () -> IForgeMenuType.create(HydrogenReductorMenu::new));
  public static final DeferredHolder<MenuType<PEMDeviceMenu>> PEM_DEVICE_MENU =
    MENU_TYPES.register("pem_device",
      () -> IForgeMenuType.create(PEMDeviceMenu::new));
  public static final DeferredHolder<MenuType<AutoExperimenterMenu>> AUTO_EXPERIMENTER_MENU =
    MENU_TYPES.register("auto_experimenter",
      () -> IForgeMenuType.create(AutoExperimenterMenu::new));
  public static final DeferredHolder<MenuType<FiltrationDeviceMenu>> FILTRAION_DEVICE =
    MENU_TYPES.register("filtration_device",
      () -> IForgeMenuType.create(FiltrationDeviceMenu::new));
  public static final DeferredHolder<MenuType<OnsenDetecterMenu>> ONSEN_DETECTER_MENU =
    MENU_TYPES.register("onsen_detecter",
      () -> IForgeMenuType.create((id, inv, buf) -> new OnsenDetecterMenu(id, inv.player.level(), inv.player.blockPosition())));
  public static final DeferredHolder<MenuType<OnsenMinerMenu>> ONSEN_MINER_MENU =
    MENU_TYPES.register("hotspring_miner",
      () -> IForgeMenuType.create(OnsenMinerMenu::new));

  public static void register(IEventBus eventBus) {
    MENU_TYPES.register(eventBus);
  }
}

