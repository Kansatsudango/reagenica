package kandango.reagenica.event;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.fluid.ChemiFluidBurnMap;
import kandango.reagenica.item.reagent.ReagentFluidMap;
import kandango.reagenica.packet.ModMessages;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "reagenica", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ModMessages.register();
        ReagentFluidMap.registerAll(ChemiItems.listItems);
        ReagentFluidMap.printMap();
        ChemiFluidBurnMap.register();
    }
}
