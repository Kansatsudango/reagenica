package kandango.reagenica.recipes;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidFillingRecipe(FluidStack in, ItemStack out) {
}