package kandango.reagenica.item.reagent;

public class ColoredReagent extends Reagent{
  public ColoredReagent(ReagentProperties rp, Properties properties){
    super(rp,properties);
  }

  public int getColor(){
    return props.color();
  }
}
