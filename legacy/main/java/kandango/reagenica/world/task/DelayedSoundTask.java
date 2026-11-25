package kandango.reagenica.world.task;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;

public class DelayedSoundTask {
  public final ServerLevel slv;
  public final UUID player;
  public final Holder<SoundEvent> sound;
  public final double x;
  public final double y;
  public final double z;
  @Nullable public final Component message;
  private int delay;
  private boolean isUsed = false;

  public DelayedSoundTask(ServerLevel slv, ServerPlayer player, Holder<SoundEvent> sound, int delay, double x, double y, double z, @Nullable Component message){
    this.delay = delay;
    this.slv = slv;
    this.player = player.getUUID();
    this.sound = sound;
    this.x = x;
    this.y = y;
    this.z = z;
    this.message = message;
  }

  public void tick(){
    delay--;
  }

  public boolean isReady(){
    return delay <= 0;
  }

  public void setUsed(boolean used){
    isUsed = used;
  }

  public boolean isUsed(){
    return isUsed;
  }
}
