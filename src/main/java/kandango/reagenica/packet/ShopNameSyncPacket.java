package kandango.reagenica.packet;

import java.util.function.Supplier;

import kandango.reagenica.block.entity.StoreManagerComputerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public record ShopNameSyncPacket(BlockPos pos, String shopName, String companyName) {
  public static ShopNameSyncPacket decode(FriendlyByteBuf buf){
    BlockPos pos = buf.readBlockPos();
    String sn = buf.readUtf(20);
    String cn = buf.readUtf(20);
    return new ShopNameSyncPacket(pos, sn, cn);
  }

  public static void encode(ShopNameSyncPacket pkt, FriendlyByteBuf buf){
    buf.writeBlockPos(pkt.pos);
    buf.writeUtf(pkt.shopName, 20);
    buf.writeUtf(pkt.companyName, 20);
  }

  public static void handle(ShopNameSyncPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
    NetworkEvent.Context ctx = ctxSupplier.get();
    ctx.enqueueWork(() -> {
      Minecraft minecraft = Minecraft.getInstance();
      Level lv = minecraft.level;
      if(lv!=null){
        BlockEntity be = lv.getBlockEntity(pkt.pos);
        if(be instanceof StoreManagerComputerBlockEntity smc){
          smc.setNames(pkt.shopName, pkt.companyName);
        }
      }
    });
    ctx.setPacketHandled(true);
  }
}