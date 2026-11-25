package kandango.reagenica.villager;

public interface IVisitCooldown {
  long getNextVisitTime();
  void setNextVisitTime(long time);
}
