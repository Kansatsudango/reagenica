package kandango.reagenica.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncFluidPacket {
    private final BlockPos pos;
    private final FluidStack fluid;

    public SyncFluidPacket(BlockPos pos, FluidStack fluid) {
        this.pos = pos;
        this.fluid = fluid;
    }

    public static void encode(SyncFluidPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        msg.fluid.writeToPacket(buf);
    }

    public static SyncFluidPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        FluidStack fluid = FluidStack.readFromPacket(buf);
        return new SyncFluidPacket(pos, fluid);
    }

    public static void handle(SyncFluidPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getBlockEntity(msg.pos) instanceof ISingleTankBlock be) {
                be.receivePacket(msg.fluid);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
