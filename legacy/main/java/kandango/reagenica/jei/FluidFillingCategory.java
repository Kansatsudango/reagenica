package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.recipes.FluidFillingRecipe;
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

public class FluidFillingCategory implements IRecipeCategory<FluidFillingRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "fluid_filling");
  public static final RecipeType<FluidFillingRecipe> TYPE = RecipeType.create("reagenica", "fluid_filling", FluidFillingRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public FluidFillingCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/tank.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.COPPER_TANK.get()));
  }

  @Override
  public RecipeType<FluidFillingRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "fluid_filling", FluidFillingRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.fluid_filling");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FluidFillingRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 98, 18).addItemStack(new ItemStack(ChemiItems.TESTTUBE.get()));
    if(!recipe.in().isEmpty())builder.addSlot(RecipeIngredientRole.INPUT,62,18)
      .addFluidStack(recipe.in().getFluid(), recipe.in().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 50).addItemStack(recipe.out());
  }
  
}
