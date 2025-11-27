package kandango.reagenica.block.entity.lamp;

import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncLampState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

public class LampControllerHelper<T extends BlockEntity & ILampController> {
  private final T be;
  private LampStates lamp = LampStates.OFF;
  private int ticker=0;
  public LampControllerHelper(T be) {
    this.be = be;
  }
  public LampStates getLampStates() {
    return this.lamp;
  }
  public boolean changeLampState(LampStates states){
    if(!states.equals(this.lamp)){
      this.lamp = states;
      boolean sent = syncLampToClient();
      if(!sent) lamp = LampStates.WARN;
      return true;
    }
    return false;
  }
  public boolean syncLampToClient(){
    Level lv = be.getLevel();
    BlockPos pos = be.getBlockPos();
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(pos)
          ),
          new SyncLampState(pos, lamp)
      );
      return true;
    }else if(lv == null){
      return false;
    }
    return true;
  }
  public void lampSyncer(){//force-sync every 3 seconds
    ticker++;
    if(ticker>=60){
      ticker=0;
      syncLampToClient();
    }
  }
  public void receivePacket(LampStates states) {
    this.lamp=states;
  }
}
