package kandango.reagenica.screen;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;

public class ModMenus {
  public static final DeferredRegister<MenuType<?>> MENU_TYPES =
    DeferredRegister.create(Registries.MENU, ChemistryMod.MODID);

  public static final DeferredHolder<MenuType<?>, MenuType<ExperimentMenu>> EXPERIMENT_MENU =
    MENU_TYPES.register("experiment_menu",
      () -> IMenuTypeExtension.create(ExperimentMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<ChemicalFermenterMenu>> CHEMICAL_FERMENTER_MENU =
    MENU_TYPES.register("chemical_fermenter",
      () -> IMenuTypeExtension.create(ChemicalFermenterMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<FractionalDistillerMenu>> FRACTIONAL_DISTILLER_MENU =
    MENU_TYPES.register("fractional_distiller",
      () -> IMenuTypeExtension.create(FractionalDistillerMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<DebugGeneratorMenu>> GENERATOR_DEBUG_MENU =
    MENU_TYPES.register("debug_generator",
      () -> IMenuTypeExtension.create(DebugGeneratorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<DebugEnergyConsumerMenu>> CONSUMER_DEBUG_MENU =
    MENU_TYPES.register("debug_consumer",
      () -> IMenuTypeExtension.create(DebugEnergyConsumerMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<FuelGeneratorMenu>> GENERATOR_FUEL_MENU =
    MENU_TYPES.register("fuel_generator",
      () -> IMenuTypeExtension.create(FuelGeneratorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<FluidFuelGeneratorMenu>> GENERATOR_FLUID_FUEL_MENU =
    MENU_TYPES.register("fluid_fuel_generator",
      () -> IMenuTypeExtension.create(FluidFuelGeneratorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<RadioIsotopeGeneratorMenu>> GENERATOR_RI_MENU =
    MENU_TYPES.register("radioisotope_generator",
      () -> IMenuTypeExtension.create(RadioIsotopeGeneratorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<SolarPowerGeneratorMenu>> GENERATOR_SOLAR_MENU =
    MENU_TYPES.register("solar_power_generator",
      () -> IMenuTypeExtension.create(SolarPowerGeneratorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<LeadBatteryMenu>> LEAD_BATTERY_MENU =
    MENU_TYPES.register("lead_battery",
      () -> IMenuTypeExtension.create(LeadBatteryMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<AdvancedLeadBatteryMenu>> ADVANCED_LEAD_BATTERY_MENU =
    MENU_TYPES.register("advanced_lead_battery",
      () -> IMenuTypeExtension.create(AdvancedLeadBatteryMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<BlastFurnaceMenu>> BLAST_FURNACE_MENU =
    MENU_TYPES.register("blast_furnace",
      () -> IMenuTypeExtension.create(BlastFurnaceMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<HeatFurnaceMenu>> HEAT_FURNACE_MENU =
    MENU_TYPES.register("heat_furnace",
      () -> IMenuTypeExtension.create(HeatFurnaceMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<CrusherMenu>> CRUSHER_MENU =
    MENU_TYPES.register("crusher",
      () -> IMenuTypeExtension.create(CrusherMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<ElectroLysisMenu>> ELECTROLYSIS_MENU =
    MENU_TYPES.register("electrolysis",
      () -> IMenuTypeExtension.create(ElectroLysisMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<SimpleBagMenu>> ORE_BAG_MENU =
    MENU_TYPES.register("ore_bag",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.ORE_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<?>, MenuType<SimpleBagMenu>> CRYSTAL_BAG_MENU =
    MENU_TYPES.register("crystal_bag",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.CRYSTAL_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<?>, MenuType<SimpleBagMenu>> FARMING_BAG_MENU =
    MENU_TYPES.register("farming_bag",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.FARMING_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<?>, MenuType<SimpleBagMenu>> NYLON_BAG_MENU =
    MENU_TYPES.register("nylon_bag",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.NYLON_BAG_MENU.get(), 66, id, inv, buf)));
  public static final DeferredHolder<MenuType<?>, MenuType<SimpleBagMenu>> PLATINUM_BAG_MENU =
    MENU_TYPES.register("platinum_bag",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.PLATINUM_BAG_MENU.get(), 84, id, inv, buf)));
  public static final DeferredHolder<MenuType<?>, MenuType<SimpleBagMenu>> IRIDIUM_BAG_MENU =
    MENU_TYPES.register("iridium_bag",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new SimpleBagMenu(ModMenus.IRIDIUM_BAG_MENU.get(), 140, id, inv, buf)));
  public static final DeferredHolder<MenuType<?>, MenuType<TradingStallMenu>> TRADING_STALL_MENU =
    MENU_TYPES.register("trading_stall",
      () -> IMenuTypeExtension.create(TradingStallMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<TankMenu>> TANK_MENU =
    MENU_TYPES.register("tank",
      () -> IMenuTypeExtension.create(TankMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<AnalyzerMenu>> ANALYZER_MENU =
    MENU_TYPES.register("analyzer",
      () -> IMenuTypeExtension.create(AnalyzerMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<DissolverMenu>> DISSOLVER_MENU =
    MENU_TYPES.register("dissolver",
      () -> IMenuTypeExtension.create(DissolverMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<ReactorMenu>> REACTOR_MENU =
    MENU_TYPES.register("reactor",
      () -> IMenuTypeExtension.create(ReactorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<HeatGeneratorMenu>> HEAT_GENERATOR_MENU =
    MENU_TYPES.register("heat_generator",
      () -> IMenuTypeExtension.create(HeatGeneratorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<CookingPotMenu>> COOKING_POT_MENU =
    MENU_TYPES.register("cooking_pot",
      () -> IMenuTypeExtension.create(CookingPotMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<IncubatorMenu>> INCUBATOR_MENU =
    MENU_TYPES.register("incubator",
      () -> IMenuTypeExtension.create(IncubatorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<ComputerMenu>> COMPUTER_MENU =
    MENU_TYPES.register("computer",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new ComputerMenu(id, inv)));
  public static final DeferredHolder<MenuType<?>, MenuType<AirSeparatorMenu>> AIR_SEPARATOR_MENU =
    MENU_TYPES.register("air_separator",
      () -> IMenuTypeExtension.create(AirSeparatorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<HaberBoschMenu>> HABER_BOSCH_MENU =
    MENU_TYPES.register("haber_bosch",
      () -> IMenuTypeExtension.create(HaberBoschMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<HydrogenReductorMenu>> HYDROGEN_REDUCTOR_MENU =
    MENU_TYPES.register("hydrogen_reductor",
      () -> IMenuTypeExtension.create(HydrogenReductorMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<PEMDeviceMenu>> PEM_DEVICE_MENU =
    MENU_TYPES.register("pem_device",
      () -> IMenuTypeExtension.create(PEMDeviceMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<AutoExperimenterMenu>> AUTO_EXPERIMENTER_MENU =
    MENU_TYPES.register("auto_experimenter",
      () -> IMenuTypeExtension.create(AutoExperimenterMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<FiltrationDeviceMenu>> FILTRAION_DEVICE =
    MENU_TYPES.register("filtration_device",
      () -> IMenuTypeExtension.create(FiltrationDeviceMenu::new));
  public static final DeferredHolder<MenuType<?>, MenuType<OnsenDetecterMenu>> ONSEN_DETECTER_MENU =
    MENU_TYPES.register("onsen_detecter",
      () -> IMenuTypeExtension.create((id, inv, buf) -> new OnsenDetecterMenu(id, inv.player.level(), inv.player.blockPosition())));
  public static final DeferredHolder<MenuType<?>, MenuType<OnsenMinerMenu>> ONSEN_MINER_MENU =
    MENU_TYPES.register("hotspring_miner",
      () -> IMenuTypeExtension.create(OnsenMinerMenu::new));

  public static void register(IEventBus eventBus) {
    MENU_TYPES.register(eventBus);
  }
}

