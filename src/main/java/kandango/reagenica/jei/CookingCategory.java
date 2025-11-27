package kandango.reagenica.jei;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.recipes.CookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CookingCategory implements IRecipeCategory<CookingRecipe>{
  public static final ResourceLocation UID = new ResourceLocation(ChemistryMod.MODID, "blast_furnace");
  private final IDrawable background;
  private final IDrawable icon;
  public CookingCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation(ChemistryMod.MODID, "textures/gui/container/cooking_pot.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.COOKING_POT.get()));
  }

  @Override
  public RecipeType<CookingRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "cooking", CookingRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.cooking");
  }

  @Override
  public IDrawable getBackground(){
    return background;
  }

  @Override
  public IDrawable getIcon(){
    return icon;
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CookingRecipe recipe, @Nonnull IFocusGroup fg){
    List<Ingredient> ingredients = recipe.getInputs();
    for(int i=0;i<ingredients.size();i++){
      Ingredient item = ingredients.get(i);
      int x = 27 + 18*(i%3);
      int y = i<=2 ? 17 : 35;
      builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(item);
    }
    builder.addSlot(RecipeIngredientRole.INPUT, 120, 59).addItemStack(new ItemStack(Items.BOWL, recipe.getOutput().getCount()));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 33).addItemStack(recipe.getOutput());
  } 
}
