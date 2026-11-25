package kandango.reagenica.recipes;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.recipes.items.ItemStackWithChance;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class AnalyzerRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final Ingredient input;
  public Ingredient getInput() {
    return input;
  }
  private final List<ItemStackWithChance> results;
  public List<ItemStackWithChance> getResults() {
    return results;
  }
  private final ItemStack return_item;
  public ItemStack getReturnItem() {
    return return_item;
  }
  
  public AnalyzerRecipe(ResourceLocation id, Ingredient in, List<ItemStackWithChance> res, ItemStack ret){
    this.id=id;
    this.input = in;
    this.results = res;
    this.return_item = ret;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    ItemStack in = container.getItem(0);
    return this.input.test(in);
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return results.get(0).get(); }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.ANALYZER_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.ANALYZER_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return results.get(0).get().copy();
  }
}
