package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.recipes.ReagenimartRecipe.ReagenimartCategory;
import kandango.reagenica.screen.ComputerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ComputerOrderPacket {
  private final int tradeid;
  public ComputerOrderPacket(ReagenimartCategory cat, int index){
    this.tradeid = (cat.ordinal()<<16) + index;
  }
  private ComputerOrderPacket(int id){
    this.tradeid = id;
  }
  public static ComputerOrderPacket createNullRecipePacket(){
    return new ComputerOrderPacket(-1);
  }

  public static void encode(ComputerOrderPacket msg, FriendlyByteBuf buf){
    buf.writeInt(msg.tradeid);
  }

  public static ComputerOrderPacket decode(FriendlyByteBuf buf){
    return new ComputerOrderPacket(buf.readInt());
  }

  public static void handle(ComputerOrderPacket msg, Supplier<NetworkEvent.Context> ctx){
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      if (player != null && player.containerMenu instanceof ComputerMenu menu) {
        menu.receiveClickAction(msg.tradeid);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
