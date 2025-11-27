package kandango.reagenica.event;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.network.CableNetworkManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide && event.phase == TickEvent.Phase.END) {
            CableNetworkManager.tick((ServerLevel) event.level);
        }
    }

    @SubscribeEvent
    public static void onFuelRegister(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if(stack.is(ChemiItems.ETHANOL_FUEL.get())){
            event.setBurnTime(3200);
        }
    }
}
