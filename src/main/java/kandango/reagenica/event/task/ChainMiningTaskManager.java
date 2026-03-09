package kandango.reagenica.event.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class ChainMiningTaskManager {
  private static final List<ChainMiningTask> TASKS = Collections.synchronizedList(new ArrayList<>());
  public static void tick(ServerLevel slv){
    for(ChainMiningTask task : TASKS){
      if(slv.dimension() != task.slv.dimension())return;
      for(int i=0;i<1;i++){
        BlockPos pos = task.breakingQueue.poll();
        if(pos!=null){
          task.player.gameMode.destroyBlock(pos);
        }
      }
    }
    TASKS.removeIf(task -> task.breakingQueue.isEmpty());
  }

  public static void add(ChainMiningTask task){
    TASKS.add(task);
  }
}
