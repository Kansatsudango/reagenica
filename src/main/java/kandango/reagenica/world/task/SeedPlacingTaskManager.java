package kandango.reagenica.world.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kandango.reagenica.ChemistryMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

public class SeedPlacingTaskManager implements ISeedPlacingData{
  private final List<SeedPlacingTask> TASKS;
  public SeedPlacingTaskManager(){
    this.TASKS = Collections.synchronizedList(new ArrayList<>());
    ChemistryMod.LOGGER.info("SeedPlacingTaskManager Started.");
  }
  
  public void tick(ServerLevel slv){
    try{
      synchronized(TASKS){
        for(SeedPlacingTask task : TASKS){
          task.slv.setBlock(task.pos, task.state, Block.UPDATE_ALL);
        }
        TASKS.clear();
      }
    }catch(Exception e){
      ChemistryMod.LOGGER.error("Exception reported!", e);
      throw e;
    }
  }

  public void add(ServerLevel slv, SeedPlacingTask task){
    TASKS.add(task);
  }
}
