package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrusherRecipeSerializer implements RecipeSerializer<CrusherRecipe> {

    @Override
    public CrusherRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        ItemStack result = RecipeJsonHelper.itemStackFromJsonRequired(json, "result");
        return new CrusherRecipe(id, input, result);
    }

    @Override
    public CrusherRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        return new CrusherRecipe(id, input, result);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull CrusherRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        buf.writeItemStack(recipe.getOutput(), false);
    }
}
