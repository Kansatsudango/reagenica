package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.screen.FlatMapperMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class FlatMapperTogglePacket {

  public FlatMapperTogglePacket() {
  }

  public FlatMapperTogglePacket(FriendlyByteBuf buf) {
  }

  public void toBytes(FriendlyByteBuf buf) {
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      if (player == null) return;

      if(player.containerMenu instanceof FlatMapperMenu menu){
        menu.processToggleButton();
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
