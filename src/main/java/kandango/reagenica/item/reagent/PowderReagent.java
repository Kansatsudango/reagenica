package kandango.reagenica.item.reagent;

import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.world.level.material.Fluid;

public class PowderReagent extends Reagent{
  private final Supplier<Fluid> lazyfluid;
  
  public PowderReagent(ReagentProperties rp, Properties properties){
    super(rp,properties);
    this.lazyfluid = () -> null;
  }
  public PowderReagent(ReagentProperties rp, Properties properties, Supplier<Fluid> fluid){
    super(rp,properties);
    this.lazyfluid = fluid;
  }

  public int getColor(){
    return props.color();
  }

  public Optional<Fluid> getRelativeFluid(){
    return Optional.ofNullable(lazyfluid.get());
  }
}
