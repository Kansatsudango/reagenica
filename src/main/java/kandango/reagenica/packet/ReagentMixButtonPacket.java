package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.block.entity.DraftChamberBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ReagentMixButtonPacket {
    private final BlockPos pos;
    private final boolean shift;

    public ReagentMixButtonPacket(BlockPos pos,boolean shift) {
        this.pos = pos;
        this.shift=shift;
    }

    public ReagentMixButtonPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.shift=buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(shift);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            if (level.getBlockEntity(pos) instanceof DraftChamberBlockEntity be) {
                be.mixButton(shift);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
