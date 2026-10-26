package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import kandango.reagenica.jei.util.ReagenicaTank;
import kandango.reagenica.recipes.FrierRecipe;
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
import net.minecraftforge.fluids.FluidStack;

public class FrierCategory implements IRecipeCategory<FrierRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "frier");
  private final IDrawable background;
  private final IDrawable icon;

  public FrierCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/frier.png"), 0, 0, 176, 108);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.FRIER.get()));
  }

  @Override
  public RecipeType<FrierRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "frier", FrierRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.frier");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FrierRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 42, 47).addIngredients(recipe.getInput());
    ItemStack result = recipe.getOutput();
    builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 40).addItemStack(result);
    ReagenicaTank.create(39,43, 11, 43).renderSize(58, 36).capacity(30).setFluid(new FluidStack(ChemiFluids.COOKING_OIL.getFluid(), 25)).consumeAsInputTank(builder);
  }
  
}
