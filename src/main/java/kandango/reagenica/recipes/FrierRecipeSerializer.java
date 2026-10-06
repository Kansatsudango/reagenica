package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import kandango.reagenica.recipes.items.ItemStackWithChance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FrierRecipeSerializer implements RecipeSerializer<FrierRecipe> {

    @Override
    public FrierRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        ItemStack result = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result");
        ItemStackWithChance result2 = ItemStackWithChance.fromJson(GsonHelper.getAsJsonObject(json, "byproduct"));
        return new FrierRecipe(id, input, result, result2);
    }

    @Override
    public FrierRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        ItemStackWithChance result2 = ItemStackWithChance.fromFriendlyBuf(buf);
        return new FrierRecipe(id, input, result, result2);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull FrierRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        buf.writeItemStack(recipe.getOutput(), false);
        ItemStackWithChance.toFriendlyBuf(buf, recipe.getByproduct());
    }
}
