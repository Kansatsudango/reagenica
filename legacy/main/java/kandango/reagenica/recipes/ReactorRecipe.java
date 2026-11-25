package kandango.reagenica.recipes;

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

public class ReactorRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final Ingredient input;
  public Ingredient getInput() {return input;}
  private final ItemStack output;
  public ItemStack getOutput() {return output;}

  public ReactorRecipe(ResourceLocation id, Ingredient in, ItemStack o){
    this.id=id;
    this.input = in;
    this.output = o;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return input.test(container.getItem(0));
  }

  @Override
  public ItemStack assemble(@Nonnull Container container,@Nonnull RegistryAccess rg) {
      return output.copy();
  }
  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return output; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.REACTOR_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.REACTOR_TYPE.get(); }
}
