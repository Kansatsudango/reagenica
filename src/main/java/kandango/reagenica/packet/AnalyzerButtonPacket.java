package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.block.entity.AnalyzerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class AnalyzerButtonPacket {
    private final BlockPos pos;

    public AnalyzerButtonPacket(BlockPos pos) {
        this.pos = pos;
    }

    public AnalyzerButtonPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Level level = player.level();
            if (level.getBlockEntity(pos) instanceof AnalyzerBlockEntity be) {
                be.analyze(); // 合成処理を行う
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
