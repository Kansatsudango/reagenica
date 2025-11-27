package kandango.reagenica.block.entity.lamp;

public record LampStates(LampState red,LampState yellow,LampState green) {
  public static final LampStates OFF = new LampStates(LampState.OFF, LampState.OFF, LampState.OFF);
  public static final LampStates GREEN = new LampStates(LampState.OFF, LampState.OFF, LampState.ON);
  public static final LampStates YELLOW = new LampStates(LampState.OFF, LampState.ON, LampState.OFF);
  public static final LampStates RED = new LampStates(LampState.ON, LampState.OFF, LampState.OFF);
  public static final LampStates EMERG = new LampStates(LampState.BLINK, LampState.OFF, LampState.OFF);
  public static final LampStates WARN = new LampStates(LampState.OFF, LampState.BLINK, LampState.OFF);
  public static final LampStates SLOW = new LampStates(LampState.OFF, LampState.ON, LampState.ON);
  public static final LampStates BOOTING = new LampStates(LampState.OFF, LampState.OFF, LampState.BLINK);
  
  public LampState getLampState(int layer){
    if(layer==0)return this.green;
    else if(layer==1)return this.yellow;
    else if(layer==2)return this.red;
    else throw new IndexOutOfBoundsException();
  }
  public int toInt(){
    return sti(green) | sti(yellow)<<8 | sti(red)<<16;
  }
  public static LampStates fromInt(int id){
    return new LampStates(its(id>>16 & 15),its(id>>8 & 15),its(id & 15));
  }
  private int sti(LampState s){
    if(s == LampState.OFF)return 0;
    else if(s == LampState.BLINK)return 1;
    else return 2;
  }
  private static LampState its(int i){
    if(i == 0)return LampState.OFF;
    else if(i == 1)return LampState.BLINK;
    else return LampState.ON;
  }
}