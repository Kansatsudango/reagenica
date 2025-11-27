package kandango.reagenica.jei;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.FermentationRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;

public class FermentingCategory implements IRecipeCategory<FermentationRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "fermentation");
  private final IDrawable background;
  private final IDrawable icon;

  public FermentingCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/chemical_fermenter.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.CHEMICAL_FERMENTER.get()));
  }

  @Override
  public RecipeType<FermentationRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "fermentation", FermentationRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.fermentation");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FermentationRecipe recipe, @Nonnull IFocusGroup fg){
    List<Ingredient> ingredients = recipe.getInputs();
    for(int i=0;i<ingredients.size();i++){
      Ingredient item = ingredients.get(i);
      int x = 47 + 18*(i%2);
      int y = i<=1 ? 18 : 36;
      builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(item);
    }
    builder.addSlot(RecipeIngredientRole.INPUT, 90, 18).addIngredients(recipe.getBioseed());
    if(!recipe.getInputFluid().isEmpty())builder.addSlot(RecipeIngredientRole.INPUT,26,19)
      .addFluidStack(recipe.getInputFluid().getFluid(), recipe.getInputFluid().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    if(!recipe.getOutput().isEmpty())builder.addSlot(RecipeIngredientRole.OUTPUT,129,19)
      .addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 63).addItemStack(recipe.getOutputItem());
  }
  
}
