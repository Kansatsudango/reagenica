package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.block.entity.LargeTankCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LargeTankPropPacket {
    private final BlockPos pos;
    private final int width;
    private final int height;
    private final int capacity;

    public LargeTankPropPacket(BlockPos pos, int width, int height, int capacity) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
    }

    public static void encode(LargeTankPropPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeVarInt(msg.width);
        buf.writeVarInt(msg.height);
        buf.writeInt(msg.capacity);
    }

    public static LargeTankPropPacket decode(FriendlyByteBuf buf) {
        return new LargeTankPropPacket(buf.readBlockPos(), buf.readVarInt(), buf.readVarInt(), buf.readInt());
    }

    public static void handle(LargeTankPropPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level!=null && mc.level.getBlockEntity(msg.pos) instanceof LargeTankCoreBlockEntity be) {
                be.receiveSizePacket(msg.width, msg.height, msg.capacity);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
