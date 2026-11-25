package kandango.reagenica.world.task;

import java.util.Queue;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ChainMiningTask {

  public final ServerLevel slv;
  public final UUID player;
  public final Queue<BlockPos> breakingQueue;
  public final ItemStack stack;

  public ChainMiningTask(ServerPlayer player, Queue<BlockPos> blocks) {
    this.player = player.getUUID();
    this.breakingQueue = blocks;
    this.slv = player.serverLevel();
    this.stack = player.getMainHandItem().copy();
  }
}
