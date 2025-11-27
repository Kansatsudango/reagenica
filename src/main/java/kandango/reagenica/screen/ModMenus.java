package kandango.reagenica.screen;

import kandango.reagenica.ChemistryMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChemistryMod.MODID);

    public static final RegistryObject<MenuType<ExperimentMenu>> EXPERIMENT_MENU =
        MENU_TYPES.register("experiment_menu",
            () -> IForgeMenuType.create(ExperimentMenu::new));
    public static final RegistryObject<MenuType<ChemicalFermenterMenu>> CHEMICAL_FERMENTER_MENU =
        MENU_TYPES.register("chemical_fermenter",
            () -> IForgeMenuType.create(ChemicalFermenterMenu::new));
    public static final RegistryObject<MenuType<FractionalDistillerMenu>> FRACTIONAL_DISTILLER_MENU =
        MENU_TYPES.register("fractional_distiller",
            () -> IForgeMenuType.create(FractionalDistillerMenu::new));
    public static final RegistryObject<MenuType<DebugGeneratorMenu>> GENERATOR_DEBUG_MENU =
        MENU_TYPES.register("debug_generator",
            () -> IForgeMenuType.create(DebugGeneratorMenu::new));
    public static final RegistryObject<MenuType<DebugEnergyConsumerMenu>> CONSUMER_DEBUG_MENU =
        MENU_TYPES.register("debug_consumer",
            () -> IForgeMenuType.create(DebugEnergyConsumerMenu::new));
    public static final RegistryObject<MenuType<FuelGeneratorMenu>> GENERATOR_FUEL_MENU =
        MENU_TYPES.register("fuel_generator",
            () -> IForgeMenuType.create(FuelGeneratorMenu::new));
    public static final RegistryObject<MenuType<FluidFuelGeneratorMenu>> GENERATOR_FLUID_FUEL_MENU =
        MENU_TYPES.register("fluid_fuel_generator",
            () -> IForgeMenuType.create(FluidFuelGeneratorMenu::new));
    public static final RegistryObject<MenuType<RadioIsotopeGeneratorMenu>> GENERATOR_RI_MENU =
        MENU_TYPES.register("radioisotope_generator",
            () -> IForgeMenuType.create(RadioIsotopeGeneratorMenu::new));
    public static final RegistryObject<MenuType<LeadBatteryMenu>> LEAD_BATTERY_MENU =
        MENU_TYPES.register("lead_battery",
            () -> IForgeMenuType.create(LeadBatteryMenu::new));
    public static final RegistryObject<MenuType<BlastFurnaceMenu>> BLAST_FURNACE_MENU =
        MENU_TYPES.register("blast_furnace",
            () -> IForgeMenuType.create(BlastFurnaceMenu::new));
    public static final RegistryObject<MenuType<HeatFurnaceMenu>> HEAT_FURNACE_MENU =
        MENU_TYPES.register("heat_furnace",
            () -> IForgeMenuType.create(HeatFurnaceMenu::new));
    public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER_MENU =
        MENU_TYPES.register("crusher",
            () -> IForgeMenuType.create(CrusherMenu::new));
    public static final RegistryObject<MenuType<ElectroLysisMenu>> ELECTROLYSIS_MENU =
        MENU_TYPES.register("electrolysis",
            () -> IForgeMenuType.create(ElectroLysisMenu::new));
    public static final RegistryObject<MenuType<OreBagMenu>> ORE_BAG_MENU =
        MENU_TYPES.register("ore_bag",
            () -> IForgeMenuType.create(OreBagMenu::new));
    public static final RegistryObject<MenuType<NylonBagMenu>> NYLON_BAG_MENU =
        MENU_TYPES.register("nylon_bag",
            () -> IForgeMenuType.create(NylonBagMenu::new));
    public static final RegistryObject<MenuType<TradingStallMenu>> TRADING_STALL_MENU =
        MENU_TYPES.register("trading_stall",
            () -> IForgeMenuType.create(TradingStallMenu::new));
    public static final RegistryObject<MenuType<TankMenu>> TANK_MENU =
        MENU_TYPES.register("tank",
            () -> IForgeMenuType.create(TankMenu::new));
    public static final RegistryObject<MenuType<AnalyzerMenu>> ANALYZER_MENU =
        MENU_TYPES.register("analyzer",
            () -> IForgeMenuType.create(AnalyzerMenu::new));
    public static final RegistryObject<MenuType<DissolverMenu>> DISSOLVER_MENU =
        MENU_TYPES.register("dissolver",
            () -> IForgeMenuType.create(DissolverMenu::new));
    public static final RegistryObject<MenuType<ReactorMenu>> REACTOR_MENU =
        MENU_TYPES.register("reactor",
            () -> IForgeMenuType.create(ReactorMenu::new));
    public static final RegistryObject<MenuType<HeatGeneratorMenu>> HEAT_GENERATOR_MENU =
        MENU_TYPES.register("heat_generator",
            () -> IForgeMenuType.create(HeatGeneratorMenu::new));
    public static final RegistryObject<MenuType<CookingPotMenu>> COOKING_POT_MENU =
        MENU_TYPES.register("cooking_pot",
            () -> IForgeMenuType.create(CookingPotMenu::new));
    public static final RegistryObject<MenuType<IncubatorMenu>> INCUBATOR_MENU =
        MENU_TYPES.register("incubator",
            () -> IForgeMenuType.create(IncubatorMenu::new));
    public static final RegistryObject<MenuType<ComputerMenu>> COMPUTER_MENU =
        MENU_TYPES.register("computer",
            () -> IForgeMenuType.create((id, inv, buf) -> new ComputerMenu(id, inv)));
    public static final RegistryObject<MenuType<AirSeparatorMenu>> AIR_SEPARATOR_MENU =
        MENU_TYPES.register("air_separator",
            () -> IForgeMenuType.create(AirSeparatorMenu::new));
    public static final RegistryObject<MenuType<HaberBoschMenu>> HABER_BOSCH_MENU =
        MENU_TYPES.register("haber_bosch",
            () -> IForgeMenuType.create(HaberBoschMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}

