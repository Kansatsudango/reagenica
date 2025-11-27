package kandango.reagenica.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncDualFluidTanksPacket {
    private final BlockPos pos;
    private final FluidStack fluid1;
    private final FluidStack fluid2;

    public SyncDualFluidTanksPacket(BlockPos pos, FluidStack fluid1, FluidStack fluid2) {
        this.pos = pos;
        this.fluid1 = fluid1;
        this.fluid2 = fluid2;
    }

    public static void encode(SyncDualFluidTanksPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        msg.fluid1.writeToPacket(buf);
        msg.fluid2.writeToPacket(buf);
    }

    public static SyncDualFluidTanksPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        FluidStack fluid1 = FluidStack.readFromPacket(buf);
        FluidStack fluid2 = FluidStack.readFromPacket(buf);
        return new SyncDualFluidTanksPacket(pos, fluid1,fluid2);
    }

    public static void handle(SyncDualFluidTanksPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getBlockEntity(msg.pos) instanceof IDualTankBlock be) {
                be.receivePacket(msg.fluid1, msg.fluid2);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
