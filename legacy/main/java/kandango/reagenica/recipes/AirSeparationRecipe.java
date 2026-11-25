package kandango.reagenica.recipes;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public record AirSeparationRecipe(Ingredient filter, FluidStack nitro, FluidStack oxy) {
}