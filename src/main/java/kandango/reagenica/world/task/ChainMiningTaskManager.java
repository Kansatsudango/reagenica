package kandango.reagenica.world.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ChainMiningTaskManager implements IChainMiningData{
  private final List<ChainMiningTask> TASKS;
  public ChainMiningTaskManager(){
    this.TASKS = Collections.synchronizedList(new ArrayList<>());
    ChemistryMod.LOGGER.info("ChainMiningTaskManager Started.");
  }
  public void tick(ServerLevel slv){
    try{
      synchronized(TASKS){
        for(ChainMiningTask task : TASKS){
          final ServerPlayer player = slv.getServer().getPlayerList().getPlayer(task.player);
          if(player==null || player.isRemoved()){
            ChemistryMod.LOGGER.info("Player was removed. Mining TASK skipped.");
            continue;
          }
          if(player.serverLevel().dimension() == task.slv.dimension()){
            BlockPos pos = task.breakingQueue.poll();
            if(pos!=null){
              ItemStack original = player.getMainHandItem();
              player.setItemInHand(InteractionHand.MAIN_HAND, task.stack);
              player.gameMode.destroyBlock(pos);
              player.setItemInHand(InteractionHand.MAIN_HAND, original);
            }
          }
        }
        TASKS.removeIf(task -> task.breakingQueue.isEmpty());
        TASKS.removeIf(task -> !hasValidPlayer(slv, task));
      }
    }catch(Exception e){
      ChemistryMod.LOGGER.error("Exception reported!", e);
      throw e;
    }
  }
  private boolean hasValidPlayer(ServerLevel slv, ChainMiningTask task){
    final ServerPlayer player = slv.getServer().getPlayerList().getPlayer(task.player);
    return player!=null && !player.isRemoved();
  }

  public void add(ServerLevel slv, ChainMiningTask task){
    TASKS.add(task);
  }
}
