package kandango.reagenica.jei;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.RegistryObject;

public class JEIPluginRecipes<C extends Container, T extends Recipe<C>>{
  public final String recipeName;
  public final Class<T> recipeclass;
  public final RecipeType<T> jeirecipetype;
  public final RegistryObject<net.minecraft.world.item.crafting.RecipeType<T>> forgelazyrecipetype;
  public final Supplier<ItemStack> catalyst;
  public final Function<IJeiHelpers,IRecipeCategory<T>> category;
  public JEIPluginRecipes(String name, Class<T> clazz, RegistryObject<net.minecraft.world.item.crafting.RecipeType<T>> forgetype, Function<IJeiHelpers,IRecipeCategory<T>> cat, Supplier<ItemStack> catalystprovider){
    recipeName = name;
    recipeclass = clazz;
    jeirecipetype = RecipeType.create("reagenica", name, clazz);
    forgelazyrecipetype = forgetype;
    category = cat;
    catalyst = catalystprovider;
  }
  public List<T> getAllRecipes(RecipeManager manager){
    return manager.getAllRecipesFor(this.forgelazyrecipetype.get());
  }
}