package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import kandango.reagenica.recipes.HydrogenReductorRecipe;
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

public class HydrogenReductorCategory implements IRecipeCategory<HydrogenReductorRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "hydrogen_reductor");
  private final IDrawable background;
  private final IDrawable icon;

  public HydrogenReductorCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/hydrogen_reductor.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.HYDROGEN_REDUCTOR.get()));
  }

  @Override
  public RecipeType<HydrogenReductorRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "hydrogen_reductor", HydrogenReductorRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.hydrogen_reduction");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull HydrogenReductorRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 55, 34).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 34).addItemStack(recipe.getOutput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 59).addItemStack(recipe.getByProduct());
    builder.addSlot(RecipeIngredientRole.INPUT,26,30)
      .addFluidStack(ChemiFluids.HYDROGEN.getFluid(), 50)
      .setFluidRenderer(400, false, 16, 48);
  }
  
}
