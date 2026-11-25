package kandango.reagenica.recipes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import kandango.reagenica.recipes.items.ItemStackWithChance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AnalyzerRecipeSerializer implements RecipeSerializer<AnalyzerRecipe> {

    @Override
    public AnalyzerRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        ItemStack return_item = RecipeJsonHelper.itemStackFromJsonifPresent(json, "return_item");
        List<ItemStackWithChance> results = RecipeJsonHelper.listFromJson(json, "results", js -> ItemStackWithChance.fromJson(js));
        return new AnalyzerRecipe(id, input, results, return_item);
    }

    @Override
    public AnalyzerRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        int resultcount = buf.readInt();
        List<ItemStackWithChance> stacks = new ArrayList<>();
        for(int i=0;i<resultcount;i++){
            stacks.add(ItemStackWithChance.fromFriendlyBuf(buf));
        }
        ItemStack returnitem = buf.readItem();
        return new AnalyzerRecipe(id, input, stacks, returnitem);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull AnalyzerRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        List<ItemStackWithChance> stacks = recipe.getResults();
        buf.writeInt(stacks.size());
        stacks.stream().forEach(ic -> ItemStackWithChance.toFriendlyBuf(buf, ic));
        buf.writeItemStack(recipe.getReturnItem(), false);
    }
}
