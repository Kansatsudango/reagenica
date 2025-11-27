package kandango.reagenica.item.reagent;

import java.util.Optional;
import java.util.function.Supplier;

import kandango.reagenica.ChemiItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class LiquidReagent extends Reagent{
  protected final Supplier<Fluid> lazyfluid;
  
  public LiquidReagent(ReagentProperties rp, Properties properties){
    super(rp,properties);
    this.lazyfluid = () -> null;
  }
  public LiquidReagent(ReagentProperties rp, Properties properties, Supplier<Fluid> fluid){
    super(rp,properties);
    this.lazyfluid = fluid;
  }
  public LiquidReagent(ReagentProperties rp){
    super(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()));
    this.lazyfluid = () -> null;
  }
  public LiquidReagent(ReagentProperties rp, Supplier<Fluid> fluid){
    super(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()));
    this.lazyfluid = fluid;
  }

  public Optional<Fluid> getRelativeFluid(){
    return Optional.ofNullable(lazyfluid.get());
  }


}
