package kandango.reagenica.screen;

import java.util.function.Supplier;

import net.minecraft.world.inventory.DataSlot;

public class LargerIntDataSlots {
  private int the_data;
  private final Supplier<Integer> datasource;
  public LargerIntDataSlots(Supplier<Integer> datasource){
    this.datasource = datasource;
  }
  public DataSlot getLittleSlot(){
    return new DataSlot() {
      @Override
      public int get() {
        return datasource.get()&0xFFFF;
      }
      @Override
      public void set(int value) {
        the_data = ((the_data & 0xFFFF0000) | (value & 0xFFFF));
      }
    };
  }
  public DataSlot getBiggerSlot(){
    return new DataSlot() {// most 16bit
      @Override
      public int get() {
        return (datasource.get() >> 16) & 0xFFFF;
      }
      @Override
      public void set(int value) {
        the_data = ((the_data & 0xFFFF) | ((value & 0xFFFF) << 16));
      }
    };
  }
  public int getValue(){
    return the_data;
  }
}
