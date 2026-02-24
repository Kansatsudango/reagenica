package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HydrogenReductorRecipeSerializer implements RecipeSerializer<HydrogenReductorRecipe> {

    @Override
    public HydrogenReductorRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        ItemStack result = RecipeJsonHelper.itemStackFromJsonRequired(json, "result");
        ItemStack byproduct = RecipeJsonHelper.itemStackFromJsonRequired(json, "byproduct");
        return new HydrogenReductorRecipe(id, input, result, byproduct);
    }

    @Override
    public HydrogenReductorRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        ItemStack byp = buf.readItem();
        return new HydrogenReductorRecipe(id, input, result, byp);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull HydrogenReductorRecipe recipe) {
      recipe.getInput().toNetwork(buf);
      buf.writeItemStack(recipe.getOutput(), false);
      buf.writeItemStack(recipe.getByProduct(), false);
    }
}
