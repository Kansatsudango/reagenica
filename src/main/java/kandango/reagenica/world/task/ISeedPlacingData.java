package kandango.reagenica.world.task;

import net.minecraft.server.level.ServerLevel;

public interface ISeedPlacingData {
  void tick(ServerLevel level);
  void add(ServerLevel level, SeedPlacingTask task);
}
