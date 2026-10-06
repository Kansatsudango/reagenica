package kandango.reagenica.recipes;

import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.recipes.items.ItemStackWithChance;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FrierRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final Ingredient input;
  public Ingredient getInput() {
    return input;
  }
  private final ItemStack output;
  public ItemStack getOutput() {
    return output;
  }
  private final ItemStackWithChance byproduct;
  public ItemStackWithChance getByproduct() {
    return byproduct;
  }
  
  public FrierRecipe(ResourceLocation id, Ingredient i, ItemStack o1, ItemStackWithChance o2){
    this.id=id;
    this.input=i;
    this.output = o1;
    this.byproduct = o2;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return input.test(container.getItem(0));
  }
  public static Optional<FrierRecipe> getRecipe(FluidStack fluid, ItemStack item, @Nonnull Level lv){
    return lv.getRecipeManager().getRecipeFor(ModRecipes.FRYER_RECIPE.get(), new SimpleContainer(item), lv);
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return output; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.HEAT_FURNACE_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.HEAT_FURNACE_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return output.copy();
  }
}
