package kandango.reagenica.world.task;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class SeedPlacingTask {

  public final ServerLevel slv;
  public final UUID player;
  public final BlockPos pos;
  public final BlockState state;

  public SeedPlacingTask(ServerPlayer player, BlockPos pos, BlockState state) {
    this.player = player.getUUID();
    this.pos = pos;
    this.slv = player.serverLevel();
    this.state = state;
  }
}
