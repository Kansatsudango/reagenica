package kandango.reagenica.world.task;

import net.minecraft.server.level.ServerLevel;

public interface IChainMiningData {
  void tick(ServerLevel level);
  void add(ServerLevel level, ChainMiningTask task);
}
