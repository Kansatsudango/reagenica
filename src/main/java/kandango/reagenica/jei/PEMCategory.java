package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.PEMRecipe;
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

public class PEMCategory implements IRecipeCategory<PEMRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "pem");
  public static final RecipeType<PEMRecipe> TYPE = RecipeType.create("reagenica", "pem", PEMRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public PEMCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/pem_device.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.PEM_DEVICE.get()));
  }

  @Override
  public RecipeType<PEMRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "pem", PEMRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.pem");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull PEMRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT,28,30)
      .addFluidStack(recipe.in().getFluid(), recipe.in().getAmount())
      .setFluidRenderer(4000, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 30)
      .addFluidStack(recipe.hydro().getFluid(), recipe.hydro().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 30)
      .addFluidStack(recipe.oxy().getFluid(), recipe.oxy().getAmount())
      .setFluidRenderer(400, false, 16, 48);
  }
}
