package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.recipes.ReactorRecipe;
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

public class ReactorCategory implements IRecipeCategory<ReactorRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "reactor");
  private final IDrawable background;
  private final IDrawable icon;

  public ReactorCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/reactor_jei.png"), 0, 0, 176, 120);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.ELECTROLYSIS_DEVICE.get()));
  }

  @Override
  public RecipeType<ReactorRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "reactor", ReactorRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.reactor");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ReactorRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 63, 76).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.INPUT, 63, 58).addItemStack(new ItemStack(ChemiItems.URANIUM_FUEL_ROD.get())).addTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable("tooltip.reagenica.jei_fuelrod")));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 76).addItemStack(recipe.getOutput());
    builder.addSlot(RecipeIngredientRole.INPUT,56,40)
      .addFluidStack(ChemiFluids.DISTILLED_WATER.getFluid(), 30000)
      .setFluidRenderer(30000, false, 64, 72);
  }
  
}
