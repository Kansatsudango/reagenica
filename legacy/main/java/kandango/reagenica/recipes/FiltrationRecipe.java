package kandango.reagenica.recipes;

import java.util.List;

import kandango.reagenica.ChemiFluids;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public record FiltrationRecipe(FluidStack in, FluidStack out) {
  public static List<FiltrationRecipe> getRecipes(){
    return List.of(
      new FiltrationRecipe(new FluidStack(Fluids.WATER, 100), 
                           new FluidStack(ChemiFluids.DISTILLED_WATER.getFluid(), 100))
    );
  }
}