package kandango.reagenica.packet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.world.retail.RetailCompanyData;
import kandango.reagenica.world.retail.SavedShopData;
import kandango.reagenica.world.retail.Transaction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public record TransactionListRequestPacket(String name) {
  private static final Map<UUID, Long> lastRequests = new HashMap<>();

  public static TransactionListRequestPacket decode(FriendlyByteBuf buf){
    return new TransactionListRequestPacket(buf.readUtf(40));
  }

  public static void encode(TransactionListRequestPacket pkt, FriendlyByteBuf buf){
    buf.writeUtf(pkt.name, 40);
  }

  public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
    NetworkEvent.Context ctx = ctxSupplier.get();
    ctx.enqueueWork(() -> {
      final ServerPlayer requestor = ctx.getSender();
      if(requestor==null) return;
      final ServerLevel slv = requestor.serverLevel();
      UUID uuid = requestor.getUUID();
      long lastRequest = lastRequests.getOrDefault(uuid, 0L);
      long now = slv.getGameTime();
      if(now-lastRequest >= 40){
        List<Transaction> transactions = SavedShopData.get(slv.getServer()).getCompany(name).map(RetailCompanyData::getTransactions).orElse(List.of());
        ModMessages.CHANNEL.send(
          PacketDistributor.PLAYER.with(() -> requestor), new TransactionHistoryPacket(transactions)
        );
        lastRequests.put(uuid, now);
      }else{
        ChemistryMod.LOGGER.info("{} requested Transaction List too often!", requestor.getName());
      }
    });
    ctx.setPacketHandled(true);
  }
}