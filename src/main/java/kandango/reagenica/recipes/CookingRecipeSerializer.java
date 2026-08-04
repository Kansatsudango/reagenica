package kandango.reagenica.recipes;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CookingRecipeSerializer implements RecipeSerializer<CookingRecipe> {

    @Override
    public CookingRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        List<Ingredient> inputs = RecipeJsonHelper.listFromJson(json, "inputs", j -> Ingredient.fromJson(j));
        ItemStack result = RecipeJsonHelper.itemStackFromJsonRequired(json, "result");
        boolean noBowl = RecipeJsonHelper.boolFromJsonOptional(json, "no_bowl").orElse(false);
        return new CookingRecipe(id, inputs, result, noBowl);
    }

    @Override
    public CookingRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        List<Ingredient> inputs = RecipeJsonHelper.listIngredientFromFrientlyBuf(buf);
        ItemStack result = buf.readItem();
        boolean noBowl = buf.readBoolean();
        return new CookingRecipe(id, inputs, result, noBowl);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull CookingRecipe recipe) {
        RecipeJsonHelper.sendIngredientList(buf, recipe.getInputs());
        buf.writeItemStack(recipe.getOutput(), false);
        buf.writeBoolean(recipe.isNotRequireBowl());
    }
}
