package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.jei.util.ReagenicaTank;
import kandango.reagenica.recipes.DissolverRecipe;
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

public class DissolverCategory implements IRecipeCategory<DissolverRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "dissolver");
  private final IDrawable background;
  private final IDrawable icon;

  public DissolverCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/dissolver.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.DISSOLVER.get()));
  }

  @Override
  public RecipeType<DissolverRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "dissolver", DissolverRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.dissolver");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull DissolverRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 70, 29).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 63).addItemStack(recipe.getOutputItem());
    ReagenicaTank.create(44,30, 23, 30).setFluid(recipe.getInputFluid()).consumeAsInputTank(builder);
    ReagenicaTank.create(116,30, 136, 62).setFluid(recipe.getResultFluid()).consumeAsOutputTank(builder);
  }
  
}
