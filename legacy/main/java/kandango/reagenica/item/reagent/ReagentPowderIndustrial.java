package kandango.reagenica.item.reagent;

public class ReagentPowderIndustrial extends Reagent{
  public ReagentPowderIndustrial(ReagentProperties rp, Properties properties){
    super(rp,properties);
  }

  public int getColor(){
    return props.color();
  }
}
