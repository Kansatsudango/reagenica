package kandango.reagenica.item.reagent;

import java.util.Optional;
import java.util.function.Supplier;

import kandango.reagenica.ChemiItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.ForgeRegistries;

public class LiquidReagent extends Reagent{
  protected final Supplier<Fluid> lazyfluid;
  private final ResourceLocation fluidTagName;
  
  public LiquidReagent(ReagentProperties rp, Properties properties){
    this(rp, properties, () -> null, null);
  }
  public LiquidReagent(ReagentProperties rp, Properties properties, Supplier<Fluid> fluid){
    this(rp, properties, fluid, null);
  }
  public LiquidReagent(ReagentProperties rp){
    this(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()), () -> null, null);
  }
  public LiquidReagent(ReagentProperties rp, Supplier<Fluid> fluid){
    this(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()), fluid, null);
  }
  public LiquidReagent(ReagentProperties rp, Supplier<Fluid> fluid, ResourceLocation name){
    this(rp, new Item.Properties().craftRemainder(ChemiItems.TESTTUBE.get()), fluid, name);
  }
  public LiquidReagent(ReagentProperties rp, Properties properties, Supplier<Fluid> fluid, ResourceLocation name){
    super(rp,properties);
    this.lazyfluid = fluid;
    this.fluidTagName = name;
  }

  @Override
  public Optional<Fluid> getRelativeFluid(){
    return Optional.ofNullable(lazyfluid.get());
  }
  @Override
  public Optional<ResourceLocation> getRelativeFluidTag(){
    if(this.fluidTagName!=null){
      return Optional.of(this.fluidTagName);
    }else{
      return getRelativeFluid().map(ForgeRegistries.FLUIDS::getKey).map(ResourceLocation::getPath)
                        .map(path -> ResourceLocation.fromNamespaceAndPath("forge", path));
    }
  }


}
