package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.block.entity.IBlockEntityWithSlider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

// SliderValuePacket.java

public class SliderValuePacket {
    private final BlockPos pos;
    private final double value;

    public SliderValuePacket(BlockPos pos, double value) {
        this.pos = pos;
        this.value = value;
    }

    public static void encode(SliderValuePacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeDouble(msg.value);
    }

    public static SliderValuePacket decode(FriendlyByteBuf buf) {
        return new SliderValuePacket(buf.readBlockPos(), buf.readDouble());
    }

    public static void handle(SliderValuePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Level level = player.level();
            if(!level.isLoaded(msg.pos))return;
            if (player != null && level.getBlockEntity(msg.pos) instanceof IBlockEntityWithSlider be) {
                be.setSliderValue(msg.value);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
