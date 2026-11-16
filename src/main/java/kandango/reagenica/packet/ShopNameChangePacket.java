package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.block.entity.StoreManagerComputerBlockEntity;
import kandango.reagenica.screen.StoreManagerComputerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public record ShopNameChangePacket(String shopName, String companyName) {
  public static ShopNameChangePacket decode(FriendlyByteBuf buf){
    String sn = buf.readUtf(20);
    String cn = buf.readUtf(20);
    return new ShopNameChangePacket(sn, cn);
  }

  public static void encode(ShopNameChangePacket pkt, FriendlyByteBuf buf){
    buf.writeUtf(pkt.shopName, 20);
    buf.writeUtf(pkt.companyName, 20);
  }

  public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
    NetworkEvent.Context ctx = ctxSupplier.get();
    ctx.enqueueWork(() -> {
      final ServerPlayer requestor = ctx.getSender();
      if(requestor==null) return;
      final ServerLevel slv = requestor.serverLevel();
      if(requestor.containerMenu instanceof StoreManagerComputerMenu menu){
        menu.setNames(shopName, companyName);
        StoreManagerComputerBlockEntity be = menu.getBlockEntity();
        if(be!=null){
          ModMessages.CHANNEL.send(
            PacketDistributor.TRACKING_CHUNK.with(
              () -> slv.getChunkAt(be.getBlockPos())
              ),new ShopNameSyncPacket(be.getBlockPos(), be.getShopName(), be.getCompanyName()));
        }
      }
    });
    ctx.setPacketHandled(true);
  }
}