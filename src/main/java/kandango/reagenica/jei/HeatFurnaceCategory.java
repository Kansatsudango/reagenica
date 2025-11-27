package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.HeatFurnaceRecipe;
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

public class HeatFurnaceCategory implements IRecipeCategory<HeatFurnaceRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "heat_furnace");
  private final IDrawable background;
  private final IDrawable icon;

  public HeatFurnaceCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/heat_furnace.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.HEAT_FURNACE.get()));
  }

  @Override
  public RecipeType<HeatFurnaceRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "heat_furnace", HeatFurnaceRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.heat_furnace");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull HeatFurnaceRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 56, 23).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 24).addItemStack(recipe.getOutput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 48).addItemStack(recipe.getByproduct());
    if(!recipe.getInputFluid().isEmpty())builder.addSlot(RecipeIngredientRole.INPUT,26,30)
      .addFluidStack(recipe.getInputFluid().getFluid(), recipe.getInputFluid().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    if(!recipe.getResultFluid().isEmpty())builder.addSlot(RecipeIngredientRole.OUTPUT,135,30)
      .addFluidStack(recipe.getResultFluid().getFluid(), recipe.getResultFluid().getAmount())
      .setFluidRenderer(400, false, 16, 48);
  }
  
}
