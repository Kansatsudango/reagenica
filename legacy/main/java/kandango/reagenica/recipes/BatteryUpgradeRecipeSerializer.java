package kandango.reagenica.recipes;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class BatteryUpgradeRecipeSerializer implements RecipeSerializer<BatteryUpgradeRecipe>{

  @Override
  public BatteryUpgradeRecipe fromJson(ResourceLocation id, JsonObject json) {
    ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
    return new BatteryUpgradeRecipe(recipe);
  }

  @Override
  public @Nullable BatteryUpgradeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
    ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
    return new BatteryUpgradeRecipe(recipe);
  }

  @Override
  public void toNetwork(FriendlyByteBuf buf, BatteryUpgradeRecipe recipe) {
    RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe);
  }
  
}
