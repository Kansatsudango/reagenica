package kandango.reagenica.event.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ChainMiningTaskManager {
  private static final List<ChainMiningTask> TASKS = Collections.synchronizedList(new ArrayList<>());
  public static void tick(ServerLevel slv){
    for(ChainMiningTask task : TASKS){
      if(slv.dimension() != task.slv.dimension())continue;
      for(int i=0;i<1;i++){
        BlockPos pos = task.breakingQueue.poll();
        if(pos!=null){
          ItemStack original = task.player.getMainHandItem();
          task.player.setItemInHand(InteractionHand.MAIN_HAND, task.stack);
          task.player.gameMode.destroyBlock(pos);
          task.player.setItemInHand(InteractionHand.MAIN_HAND, original);
        }
      }
    }
    TASKS.removeIf(task -> task.breakingQueue.isEmpty());
  }

  public static void add(ChainMiningTask task){
    TASKS.add(task);
  }
}
