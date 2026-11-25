package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.recipes.CrusherRecipe;
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

public class CrusherCategory implements IRecipeCategory<CrusherRecipe>{
  public static final ResourceLocation UID = new ResourceLocation(ChemistryMod.MODID, "crusher");
  private final IDrawable background;
  private final IDrawable icon;
  public CrusherCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation(ChemistryMod.MODID, "textures/gui/container/crusher.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.CRUSHER.get()));
  }

  @Override
  public RecipeType<CrusherRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "crusher", CrusherRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.crusher");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CrusherRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getOutput());
  }
}
