package kandango.reagenica.network;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class CableNetworkManager {
  private static final Map<ResourceKey<Level>,Set<BlockPos>> pendingUpdates = new ConcurrentHashMap<>();

  public static void requestUpdate(@Nonnull ServerLevel slv, BlockPos pos) {
    pendingUpdates.computeIfAbsent(slv.dimension(), r -> ConcurrentHashMap.newKeySet()).add(pos);
  }

  public static void tick(@Nonnull ServerLevel slv) {
    if (pendingUpdates.isEmpty()) return;
    ResourceKey<Level> currentDim = slv.dimension();
    final Set<BlockPos> positions = pendingUpdates.get(currentDim);
    if(positions!=null && !positions.isEmpty()){
      for(BlockPos pos : positions){
        int count = BroadcastUtil.broadcastChange(pos, slv);
        ChemistryMod.LOGGER.debug("Broadcasted from {}, notified to {} BEs.",pos.toShortString(),count);
      }
    }
    pendingUpdates.remove(currentDim);
  }
}
