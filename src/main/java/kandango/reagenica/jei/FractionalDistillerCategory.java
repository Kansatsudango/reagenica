package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.FractionalDistillerRecipe;
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

public class FractionalDistillerCategory implements IRecipeCategory<FractionalDistillerRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "distilling");
  private final IDrawable background;
  private final IDrawable icon;

  public FractionalDistillerCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/fractional_distiller.png"), 0, 0, 176, 122);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.FRACTIONAL_DISTILLER_BOTTOM.get()));
  }

  @Override
  public RecipeType<FractionalDistillerRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "distilling", FractionalDistillerRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.distilling");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FractionalDistillerRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 101).addItemStack(recipe.getResidual());
    if(!recipe.getInput().isEmpty())builder.addSlot(RecipeIngredientRole.INPUT,8,39)
      .addFluidStack(recipe.getInput().getFluid(), recipe.getInput().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    if(!recipe.getBottom().isEmpty())builder.addSlot(RecipeIngredientRole.OUTPUT,129,70)
      .addFluidStack(recipe.getBottom().getFluid(), recipe.getBottom().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    if(!recipe.getTop().isEmpty())builder.addSlot(RecipeIngredientRole.OUTPUT,129,9)
      .addFluidStack(recipe.getTop().getFluid(), recipe.getTop().getAmount())
      .setFluidRenderer(400, false, 16, 48);
  }
  
}
