package kandango.reagenica.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import kandango.reagenica.screen.StoreManagerComputerScreen;
import kandango.reagenica.world.retail.Transaction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record TransactionHistoryPacket(List<Transaction> transactions) {
  public static void encode(TransactionHistoryPacket pkt, FriendlyByteBuf buf){
    buf.writeShort(pkt.transactions.size());
    for(Transaction transaction : pkt.transactions){
      buf.writeNbt(transaction.toCompoundTag());
    }
  }

  public static TransactionHistoryPacket decode(FriendlyByteBuf buf){
    int size = buf.readShort();
    if(size < 0 || size > 1000){
      throw new IllegalStateException("Received packet with invalid transaction count!");
    }
    List<Transaction> transactions = new ArrayList<>(size);
    for(int i=0; i<size; i++){
      CompoundTag tag = buf.readNbt();
      Objects.requireNonNull(tag);
      transactions.add(Transaction.fromCompoundTag(tag));
    }
    return new TransactionHistoryPacket(transactions);
  }

  public static void handle(TransactionHistoryPacket pkt, Supplier<NetworkEvent.Context> ctx){
    ctx.get().enqueueWork(() -> {
      Minecraft minecraft = Minecraft.getInstance();
      if(minecraft.screen instanceof StoreManagerComputerScreen smcs){
        smcs.onListReflesh(pkt.transactions);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
