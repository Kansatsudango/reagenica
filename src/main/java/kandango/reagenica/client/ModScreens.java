package kandango.reagenica.client;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModScreens {
    @SubscribeEvent
    public static void onRegisterScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.EXPERIMENT_MENU.get(), ExperimentScreen::new);
            MenuScreens.register(ModMenus.CHEMICAL_FERMENTER_MENU.get(), ChemicalFermenterScreen::new);
            MenuScreens.register(ModMenus.FRACTIONAL_DISTILLER_MENU.get(), FractionalDistillerScreen::new);
            MenuScreens.register(ModMenus.GENERATOR_DEBUG_MENU.get(), DebugGeneratorScreen::new);
            MenuScreens.register(ModMenus.CONSUMER_DEBUG_MENU.get(), DebugEnergyConsumerScreen::new);
            MenuScreens.register(ModMenus.GENERATOR_FUEL_MENU.get(), FuelGeneratorScreen::new);
            MenuScreens.register(ModMenus.GENERATOR_FLUID_FUEL_MENU.get(), FluidFuelGeneratorScreen::new);
            MenuScreens.register(ModMenus.GENERATOR_RI_MENU.get(), RadioIsotopeGeneratorScreen::new);
            MenuScreens.register(ModMenus.GENERATOR_SOLAR_MENU.get(), SolarPowerGeneratorScreen::new);
            MenuScreens.register(ModMenus.LEAD_BATTERY_MENU.get(), LeadBatteryScreen::new);
            MenuScreens.register(ModMenus.BLAST_FURNACE_MENU.get(), BlastFurnaceScreen::new);
            MenuScreens.register(ModMenus.HEAT_FURNACE_MENU.get(), HeatFurnaceScreen::new);
            MenuScreens.register(ModMenus.CRUSHER_MENU.get(), CrusherScreen::new);
            MenuScreens.register(ModMenus.ELECTROLYSIS_MENU.get(), ElectroLysisScreen::new);
            MenuScreens.register(ModMenus.ORE_BAG_MENU.get(), OreBagScreen::new);
            MenuScreens.register(ModMenus.NYLON_BAG_MENU.get(), NylonBagScreen::new);
            MenuScreens.register(ModMenus.TRADING_STALL_MENU.get(), TradingStallScreen::new);
            MenuScreens.register(ModMenus.TANK_MENU.get(), TankScreen::new);
            MenuScreens.register(ModMenus.ANALYZER_MENU.get(), AnalyzerScreen::new);
            MenuScreens.register(ModMenus.DISSOLVER_MENU.get(), DissolverScreen::new);
            MenuScreens.register(ModMenus.REACTOR_MENU.get(), ReactorScreen::new);
            MenuScreens.register(ModMenus.HEAT_GENERATOR_MENU.get(), HeatGeneratorScreen::new);
            MenuScreens.register(ModMenus.COOKING_POT_MENU.get(), CookingPotScreen::new);
            MenuScreens.register(ModMenus.INCUBATOR_MENU.get(), IncubatorScreen::new);
            MenuScreens.register(ModMenus.COMPUTER_MENU.get(), ComputerScreen::new);
            MenuScreens.register(ModMenus.AIR_SEPARATOR_MENU.get(), AirSeparatorScreen::new);
            MenuScreens.register(ModMenus.HABER_BOSCH_MENU.get(), HaberBoschScreen::new);
            MenuScreens.register(ModMenus.ONSEN_DETECTER_MENU.get(), OnsenDetecterScreen::new);
            MenuScreens.register(ModMenus.ONSEN_MINER_MENU.get(), OnsenMinerScreen::new);
        });
    }
}
