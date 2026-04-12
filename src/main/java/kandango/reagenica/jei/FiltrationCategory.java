package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.recipes.FiltrationRecipe;
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

public class FiltrationCategory implements IRecipeCategory<FiltrationRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "filtration");
  public static final RecipeType<FiltrationRecipe> TYPE = RecipeType.create("reagenica", "filtration", FiltrationRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public FiltrationCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/filtration_device.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.FILTRATION_DEVICE.get()));
  }

  @Override
  public RecipeType<FiltrationRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "filtration", FiltrationRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.filtration");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FiltrationRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT,44,23)
      .addFluidStack(recipe.in().getFluid(), recipe.in().getAmount())
      .setFluidRenderer(4000, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 23)
      .addFluidStack(recipe.out().getFluid(), recipe.out().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 55).addItemStack(new ItemStack(ChemiItems.CARBON_FILTER.get()));
  }
}
