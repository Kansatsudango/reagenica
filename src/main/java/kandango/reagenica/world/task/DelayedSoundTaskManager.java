package kandango.reagenica.world.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class DelayedSoundTaskManager implements IDelayedSoundData{
  private final List<DelayedSoundTask> TASKS;

  public DelayedSoundTaskManager(){
    this.TASKS = Collections.synchronizedList(new ArrayList<>());
    ChemistryMod.LOGGER.info("DelayedSoundTaskManager Started.");
  }

  @Override
  public void tick(ServerLevel level) {
    try{
      synchronized(TASKS){
        for(DelayedSoundTask task : TASKS){
          getPlayer(level, task).ifPresent(player -> {
            if(player.serverLevel().dimension() == task.slv.dimension()){
              task.tick();
              if(task.isReady()){
                player.connection.send(new ClientboundSoundPacket(
                  Holder.direct(SoundEvents.AMETHYST_BLOCK_RESONATE),
                  SoundSource.PLAYERS,
                  player.getX()+task.x,
                  player.getY()+task.y,
                  player.getZ()+task.z,
                  1.0f,
                  1.0f,
                  task.slv.random.nextLong()
                ));
                if(task.message!=null)player.displayClientMessage(task.message, true);
                task.setUsed(true);
              }
            }
          });
        }
        TASKS.removeIf(task -> task.isUsed());
        TASKS.removeIf(task -> getPlayer(level, task).isEmpty());
      }
    }catch(Exception e){
      ChemistryMod.LOGGER.error("Exception reported!", e);
      throw e;
    }
  }

  private Optional<ServerPlayer> getPlayer(ServerLevel level, DelayedSoundTask task){
    return Optional.ofNullable(level.getServer().getPlayerList().getPlayer(task.player));
  }

  @Override
  public void add(ServerLevel level, DelayedSoundTask task) {
    TASKS.add(task);
  }

}
