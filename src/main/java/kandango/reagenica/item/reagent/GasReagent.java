package kandango.reagenica.item.reagent;

import java.util.Optional;
import java.util.function.Supplier;

import kandango.reagenica.ChemiItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class GasReagent extends Reagent{
  private final Supplier<Fluid> lazyfluid;
  
  public GasReagent(ReagentProperties rp, Properties properties){
    super(rp,properties);
    this.lazyfluid = () -> null;
  }
  public GasReagent(ReagentProperties rp, Properties properties, Supplier<Fluid> fluid){
    super(rp,properties);
    this.lazyfluid = fluid;
  }
  public GasReagent(ReagentProperties rp){
    super(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()));
    this.lazyfluid = () -> null;
  }
  public GasReagent(ReagentProperties rp, Supplier<Fluid> fluid){
    super(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()));
    this.lazyfluid = fluid;
  }

  public int getColor(){
    return props.color();
  }

  public Optional<Fluid> getRelativeFluid(){
    return Optional.ofNullable(lazyfluid.get());
  }
}
