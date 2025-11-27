package kandango.reagenica.villager.goals;

import java.util.Optional;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.TradingStallBlockEntity;
import kandango.reagenica.villager.StallVisitCooldown;
import kandango.reagenica.villager.pois.ChemiPOIs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class VisitStallGoal extends Goal{
  private final Villager villager;
  private final double speed;
  private BlockPos targetpos;

  public VisitStallGoal(Villager villager, double speed){
    this.villager = villager;
    this.speed = speed;
  }

  @Override
  public boolean canUse(){
    long gameTime = villager.level().getGameTime();
    long nextVisit = StallVisitCooldown.getStallVisitCooldown(villager).getNextVisitTime();
    if (gameTime < nextVisit) return false;
    if (!isMorningOrEvening()) return false;
    if(!isMorningOrEvening()){
      return false;
    }
    if(villager.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)){
      return false;
    }
    if (!villager.getBrain().isActive(Activity.IDLE)) return false;

    if(villager.level() instanceof ServerLevel level){
      PoiManager poiManager = level.getPoiManager();
      Optional<BlockPos> maybePos = poiManager.getRandom(
        holder -> holder.is(ChemiPOIs.STALL_POI.getKey()),
        pos -> true,
        PoiManager.Occupancy.ANY,
        villager.blockPosition(),
        32,
        villager.getRandom());
      if(maybePos.isPresent()){
        this.targetpos = maybePos.orElseThrow(() -> new IllegalStateException("It's a magic!"));
        return true;
      }
    }
    return false;
  }

  @Override
  public void start(){
    villager.getNavigation().moveTo(targetpos.getX() + 0.5, targetpos.getY() + 0.5, targetpos.getZ() + 0.5, this.speed);
  }

  @Override
  public boolean canContinueToUse(){
    // boolean movenotend = !villager.getNavigation().isDone();
    // boolean movenearby = this.isReachedTarget();
    // boolean movepathnotlost = villager.getNavigation().getPath() != null;
    // return movenotend && movenearby && movepathnotlost;
    return this.targetpos != null && !villager.getNavigation().isDone();
  }

  @Override
  public void stop(){
    if(this.isReachedTarget()){
      villager.swing(InteractionHand.MAIN_HAND);
      Level level = villager.level();
      long gameTime = level.getGameTime();
      BlockEntity be = level.getBlockEntity(targetpos);
      if(be instanceof TradingStallBlockEntity stall){
        long nexttime = stall.trade(villager, level);
        StallVisitCooldown.getStallVisitCooldown(villager).setNextVisitTime(gameTime + nexttime);
      }else{
        ChemistryMod.LOGGER.error("There was no trading stall at pos {}.",targetpos);
      }
    }
  }

  @Override
  public boolean isInterruptable() {
    return false;
  }

  private boolean isMorningOrEvening() {
    long dayTime = villager.level().dayTime() % 24000;
    return (dayTime > 0 && dayTime < 2000) || (dayTime > 10000 && dayTime < 12000);
  }
  private boolean isReachedTarget() {
    return villager.distanceToSqr(Vec3.atCenterOf(targetpos)) < 9.0D;
  }
}
