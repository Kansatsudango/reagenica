package kandango.reagenica.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

import kandango.reagenica.block.entity.FractionalDistillerBlockEntity;

public class SyncQuadFluidTanksPacket {
    private final BlockPos pos;
    private final FluidStack fluid1;
    private final FluidStack fluid2;
    private final FluidStack fluid3;
    private final FluidStack fluid4;

    public SyncQuadFluidTanksPacket(BlockPos pos, FluidStack fluid1, FluidStack fluid2, FluidStack fluid3, FluidStack fluid4) {
        this.pos = pos;
        this.fluid1 = fluid1;
        this.fluid2 = fluid2;
        this.fluid3 = fluid3;
        this.fluid4 = fluid4;
    }

    public static void encode(SyncQuadFluidTanksPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        msg.fluid1.writeToPacket(buf);
        msg.fluid2.writeToPacket(buf);
        msg.fluid3.writeToPacket(buf);
        msg.fluid4.writeToPacket(buf);
    }

    public static SyncQuadFluidTanksPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        FluidStack fluid1 = FluidStack.readFromPacket(buf);
        FluidStack fluid2 = FluidStack.readFromPacket(buf);
        FluidStack fluid3 = FluidStack.readFromPacket(buf);
        FluidStack fluid4 = FluidStack.readFromPacket(buf);
        return new SyncQuadFluidTanksPacket(pos, fluid1,fluid2,fluid3,fluid4);
    }

    public static void handle(SyncQuadFluidTanksPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getBlockEntity(msg.pos) instanceof FractionalDistillerBlockEntity be) {
                be.getFluidTankInput().setFluid(msg.fluid1);
                be.getFluidTankTop().setFluid(msg.fluid2);
                be.getFluidTankBottom().setFluid(msg.fluid3);
                be.getFluidTankWater().setFluid(msg.fluid4);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
