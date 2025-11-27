package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.AirSeparationRecipe;
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

public class AirSeparationCategory implements IRecipeCategory<AirSeparationRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "air_separator");
  public static final RecipeType<AirSeparationRecipe> TYPE = RecipeType.create("reagenica", "air_separator", AirSeparationRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public AirSeparationCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/air_separator.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.AIR_SEPARATOR.get()));
  }

  @Override
  public RecipeType<AirSeparationRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "air_separator", AirSeparationRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.air_separator");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AirSeparationRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 30, 31).addIngredients(recipe.filter());
    builder.addSlot(RecipeIngredientRole.OUTPUT,74,30)
      .addFluidStack(recipe.nitro().getFluid(), recipe.nitro().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT,116,30)
      .addFluidStack(recipe.oxy().getFluid(), recipe.oxy().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    
  }
}
