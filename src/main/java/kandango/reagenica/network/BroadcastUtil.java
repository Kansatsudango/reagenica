package kandango.reagenica.network;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.electrical.ElectricCableAbstract;
import kandango.reagenica.block.entity.electrical.ElectricGeneratorAbstract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BroadcastUtil {
  public static int broadcastChange(BlockPos startpos, @Nonnull ServerLevel lv){
    Set<BlockPos> visited = new HashSet<>();
    Queue<BlockPos> queue = new ArrayDeque<>();
    queue.add(startpos);
    int watchdog = 0;
    int counter = 0;
    while (!queue.isEmpty()) {
      BlockPos pos = queue.poll();
      if(!visited.add(pos)) continue;
      watchdog++;
      if(watchdog>1000){
        ChemistryMod.LOGGER.warn("Cable tracing exceeded safety limit (1000). Origin: {}",startpos.toShortString());
        return -1;
      }
      for(Direction dir : Direction.values()){
        BlockPos neighbor = pos.relative(dir);
        BlockEntity be = lv.getBlockEntity(neighbor);
        if(be instanceof ElectricCableAbstract){
          queue.add(neighbor);
        }else if(be instanceof ElectricGeneratorAbstract gen){
          gen.notifyChange();
          counter++;
        }
      }
    }
    return counter;
  }
}
