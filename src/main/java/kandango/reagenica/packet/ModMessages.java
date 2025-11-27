package kandango.reagenica.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static int id = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("reagenica", "main"),
        () -> "1.0",
        s -> true, // client version check
        s -> true  // server version check
    );

    public static void register() {
        CHANNEL.registerMessage(id++,
            ReagentMixButtonPacket.class,
            ReagentMixButtonPacket::toBytes,
            ReagentMixButtonPacket::new,
            ReagentMixButtonPacket::handle);
        CHANNEL.registerMessage(id++,
            AnalyzerButtonPacket.class,
            AnalyzerButtonPacket::toBytes,
            AnalyzerButtonPacket::new,
            AnalyzerButtonPacket::handle);
        CHANNEL.registerMessage(id++,
            SyncFluidPacket.class,
            SyncFluidPacket::encode, 
            SyncFluidPacket::decode, 
            SyncFluidPacket::handle);
        CHANNEL.registerMessage(id++,
            SyncDualFluidTanksPacket.class,
            SyncDualFluidTanksPacket::encode, 
            SyncDualFluidTanksPacket::decode, 
            SyncDualFluidTanksPacket::handle);
        CHANNEL.registerMessage(id++,
            SyncQuadFluidTanksPacket.class,
            SyncQuadFluidTanksPacket::encode, 
            SyncQuadFluidTanksPacket::decode, 
            SyncQuadFluidTanksPacket::handle);
        CHANNEL.registerMessage(id++,
            SliderValuePacket.class,
            SliderValuePacket::encode, 
            SliderValuePacket::decode, 
            SliderValuePacket::handle);
        CHANNEL.registerMessage(id++,
            SyncLampState.class,
            SyncLampState::encode, 
            SyncLampState::decode, 
            SyncLampState::handle);
        CHANNEL.registerMessage(id++,
            ComputerOrderPacket.class,
            ComputerOrderPacket::encode, 
            ComputerOrderPacket::decode, 
            ComputerOrderPacket::handle);
        CHANNEL.registerMessage(id++,
            LargeTankPropPacket.class,
            LargeTankPropPacket::encode, 
            LargeTankPropPacket::decode, 
            LargeTankPropPacket::handle);
    }
}
