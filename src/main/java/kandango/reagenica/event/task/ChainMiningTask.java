package kandango.reagenica.event.task;

import java.util.Queue;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ChainMiningTask {

  public final ServerLevel slv;
  public final ServerPlayer player;
  public final Queue<BlockPos> breakingQueue;
  public final ItemStack stack;

  public ChainMiningTask(ServerPlayer player, Queue<BlockPos> blocks) {
    this.player = player;
    this.breakingQueue = blocks;
    this.slv = player.serverLevel();
    this.stack = player.getMainHandItem();
  }
}
