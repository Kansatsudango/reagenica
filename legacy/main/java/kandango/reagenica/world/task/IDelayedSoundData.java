package kandango.reagenica.world.task;

import net.minecraft.server.level.ServerLevel;

public interface IDelayedSoundData {
  void tick(ServerLevel level);
  void add(ServerLevel level, DelayedSoundTask task);
}
