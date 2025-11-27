package kandango.reagenica.recipes;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import kandango.reagenica.recipes.items.IngredientWithCount;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ReagentMixingRecipeSerializer implements RecipeSerializer<ReagentMixingRecipe> {

    @Override
    public ReagentMixingRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        List<IngredientWithCount> inputs = RecipeJsonHelper.listFromJson(json, "inputs", j -> IngredientWithCount.fromJson(j));
        ItemStack resultA = RecipeJsonHelper.itemStackFromJsonRequired(json, "result_a");
        ItemStack resultB = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result_b");
        Ingredient catalyst = RecipeJsonHelper.ingredientFromJsonifPresent(json, "catalyst");
        boolean heat = GsonHelper.getAsBoolean(json, "requires_heat", false);
        return new ReagentMixingRecipe(id, inputs, resultA,resultB,catalyst,heat);
    }

    @Override
    public ReagentMixingRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        List<IngredientWithCount> inputs = RecipeJsonHelper.listFromFriendlyBuf(buf, b -> IngredientWithCount.fromFriendlyBuf(b));
        ItemStack resultA = buf.readItem();
        ItemStack resultB = buf.readItem();
        Ingredient catalyst = Ingredient.fromNetwork(buf);
        boolean heat = buf.readBoolean();
        return new ReagentMixingRecipe(id, inputs, resultA,resultB,catalyst,heat);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull ReagentMixingRecipe recipe) {
      RecipeJsonHelper.sendList(buf, recipe.getInputs(), (b,l) -> IngredientWithCount.toFriendlyBuf(b, l));
      buf.writeItemStack(recipe.getOutputA(), false);
      buf.writeItemStack(recipe.getOutputB(), false);
      recipe.getCatalyst().toNetwork(buf);
      buf.writeBoolean(recipe.isHeatRequired());
    }
}
