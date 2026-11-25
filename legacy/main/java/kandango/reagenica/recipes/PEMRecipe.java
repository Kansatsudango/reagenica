package kandango.reagenica.recipes;

import java.util.List;

import kandango.reagenica.ChemiFluids;
import net.minecraftforge.fluids.FluidStack;

public record PEMRecipe(FluidStack in, FluidStack hydro, FluidStack oxy) {
  public static List<PEMRecipe> getRecipes(){
    return List.of(
      new PEMRecipe(new FluidStack(ChemiFluids.DISTILLED_WATER.getFluid(), 100), 
                    new FluidStack(ChemiFluids.HYDROGEN.getFluid(), 200), 
                    new FluidStack(ChemiFluids.OXYGEN.getFluid(), 100))
    );
  }
}