package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BlastFurnaceRecipeSerializer implements RecipeSerializer<BlastFurnaceRecipe> {

    @Override
    public BlastFurnaceRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        ItemStack result = RecipeJsonHelper.itemStackFromJsonRequired(json, "result");
        ItemStack byproduct = RecipeJsonHelper.itemStackFromJsonifPresent(json, "byproduct");
        int minTemp = RecipeJsonHelper.intFromJson(json, "temp_min");
        int maxTemp = RecipeJsonHelper.intFromJson(json, "temp_max");
        float exp = RecipeJsonHelper.floatFromJson(json, "exp");
        return new BlastFurnaceRecipe(id, input, result, byproduct, minTemp, maxTemp, exp);
    }

    @Override
    public BlastFurnaceRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        ItemStack byproduct = buf.readItem();
        int minTemp = buf.readInt();
        int maxTemp = buf.readInt();
        float exp = buf.readFloat();
        return new BlastFurnaceRecipe(id, input, result, byproduct, minTemp, maxTemp, exp);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull BlastFurnaceRecipe recipe) {
      recipe.getInput().toNetwork(buf);
      buf.writeItemStack(recipe.getOutput(), false);
      buf.writeItemStack(recipe.getByproduct(), false);
      buf.writeInt(recipe.getMinTemp());
      buf.writeInt(recipe.getMaxTemp());
      buf.writeFloat(recipe.getExp());
    }
}
