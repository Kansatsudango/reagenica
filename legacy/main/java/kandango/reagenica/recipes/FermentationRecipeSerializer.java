package kandango.reagenica.recipes;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class FermentationRecipeSerializer implements RecipeSerializer<FermentationRecipe> {

    @Override
    public FermentationRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        List<Ingredient> input = RecipeJsonHelper.listFromJson(json, "inputs", j -> Ingredient.fromJson(j));
        FluidStack influid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "input_fluid");
        FluidStack result = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "result_fluid");
        ItemStack result_item = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result_item");
        Ingredient catalyst = RecipeJsonHelper.ingredientFromJsonifPresent(json, "catalyst");
        return new FermentationRecipe(id, input, influid, result, result_item, catalyst);
    }

    @Override
    public FermentationRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        List<Ingredient> inputs = RecipeJsonHelper.listIngredientFromFrientlyBuf(buf);
        FluidStack fluidin = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        FluidStack fluid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        ItemStack ritem = buf.readItem();
        Ingredient catalyst = Ingredient.fromNetwork(buf);
        return new FermentationRecipe(id, inputs, fluidin, fluid, ritem, catalyst);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull FermentationRecipe recipe) {
        RecipeJsonHelper.sendIngredientList(buf, recipe.getInputs());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getInputFluid());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getResultFluid());
        buf.writeItemStack(recipe.getOutputItem(), false);
        recipe.getBioseed().toNetwork(buf);
    }
}
