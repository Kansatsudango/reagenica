package kandango.reagenica.block.fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import kandango.reagenica.ChemiFluids;
import net.minecraft.world.level.material.Fluid;

public class ChemiFluidBurnMap {
  public static Map<Fluid,ChemiFluidBurnrate> fluidBurnMap = new HashMap<>();
  public static void register(){
    fluidBurnMap.put(ChemiFluids.CRUDE_OIL.getFluid(), new ChemiFluidBurnrate(40,10));
    fluidBurnMap.put(ChemiFluids.DIESEL_FUEL.getFluid(), new ChemiFluidBurnrate(40,40));
    fluidBurnMap.put(ChemiFluids.ETHANOL.getFluid(), new ChemiFluidBurnrate(80,20));
  }
  public static Optional<ChemiFluidBurnrate> getBurnrate(Fluid fluid){
    return Optional.ofNullable(fluidBurnMap.get(fluid));
  }
}
