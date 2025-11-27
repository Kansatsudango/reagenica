package kandango.reagenica.recipes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CookingRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final List<Ingredient> inputs;
  public List<Ingredient> getInputs() {
    return inputs;
  }
  private final ItemStack output;
  public ItemStack getOutput() {
    return output;
  }
  public int getOutputCount(){
    return output.getCount();
  }
  
  public CookingRecipe(ResourceLocation id, List<Ingredient> i, ItemStack o){
    this.id=id;
    this.inputs=i;
    this.output = o;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return false;
  }
  public boolean matchest(@Nonnull Container container){
    return RecipeHelper.isAllMeets(container, inputs);
  }
  public static Optional<CookingRecipe> getRecipe(@Nonnull Container container,@Nonnull Level lv){
    List<CookingRecipe> recipes = lv.getRecipeManager().getAllRecipesFor(ModRecipes.COOKING_TYPE.get());
    return recipes.stream().filter(r -> r.matchest(container)).max(Comparator.comparingInt(r -> r.getInputs().size()));
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return output; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.COOKING_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.COOKING_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return output.copy();
  }
}
