package kandango.reagenica.villager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.Villager;

public class StallVisitCooldown implements IVisitCooldown{
  private long nextVisitTime = 0L;

  @Override
  public long getNextVisitTime() {
    return this.nextVisitTime;
  }

  @Override
  public void setNextVisitTime(long time) {
    this.nextVisitTime = time;
  }

  public void save(CompoundTag tag) {
    tag.putLong("nextStallVisitTime", nextVisitTime);
  }

  public void load(CompoundTag nbt) {
    this.nextVisitTime = nbt.getLong("nextStallVisitTime");
  }

  public static StallVisitCooldown getStallVisitCooldown(Villager villager){
    return villager.getCapability(VisitCooldownCapability.STALL_VISIT_COOLDOWN)
      .orElseThrow(() -> new IllegalStateException("Stall visit cooldown capability missing."));
  }
}
