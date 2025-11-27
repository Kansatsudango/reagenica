package kandango.reagenica.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampStates;

public class SyncLampState {
    private final BlockPos pos;
    private final LampStates states;

    public SyncLampState(BlockPos pos, LampStates states) {
        this.pos = pos;
        this.states = states;
    }

    public static void encode(SyncLampState msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.states.toInt());
    }

    public static SyncLampState decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        LampStates states = LampStates.fromInt(buf.readInt());
        return new SyncLampState(pos, states);
    }

    public static void handle(SyncLampState msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getBlockEntity(msg.pos) instanceof ILampController lc) {
                lc.receivePacket(msg.states);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
