package kandango.reagenica.utils;

import java.util.function.Supplier;

import kandango.reagenica.ChemistryMod;

public class CounterLog<T> {
  private final Supplier<? extends RuntimeException> screamer;
  private final Supplier<? extends T> other;
  private int remaining;
  public CounterLog(Supplier<? extends RuntimeException> log, Supplier<? extends T> alter, int maxCount){
    this.screamer = log;
    this.other = alter;
    this.remaining = maxCount;
  }

  public synchronized T fallback(){
    if(remaining>0){
      ChemistryMod.LOGGER.error("Internal Error: ", screamer.get());
      remaining--;
      if(remaining==0){
        ChemistryMod.LOGGER.warn("From now on, duplicate errors will be ignored.");
      }
    }
    return other.get();
  }
}
