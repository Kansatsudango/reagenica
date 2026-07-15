package kandango.reagenica.recipes;

import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public record AirSeparationRecipe(Ingredient filter, FluidStack nitro, FluidStack oxy) {
}