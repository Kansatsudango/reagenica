package kandango.reagenica.jei;

import java.util.Optional;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import net.minecraftforge.fluids.FluidStack;

public class JEIEmptyGuard {
  public static Optional<IRecipeLayoutBuilder> fluid(IRecipeLayoutBuilder builder, FluidStack stack){
    if(stack.isEmpty())return Optional.empty();
    else return Optional.of(builder);
  }
}
