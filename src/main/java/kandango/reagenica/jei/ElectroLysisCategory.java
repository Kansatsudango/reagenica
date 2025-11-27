package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.ElectroLysisRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ElectroLysisCategory implements IRecipeCategory<ElectroLysisRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "electrolysis");
  private final IDrawable background;
  private final IDrawable icon;

  public ElectroLysisCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/electrolysis.png"), 0, 0, 176, 106);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.ELECTROLYSIS_DEVICE.get()));
  }

  @Override
  public RecipeType<ElectroLysisRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "electrolysis", ElectroLysisRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.electrolysis");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElectroLysisRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 87, 57).addIngredients(recipe.getElectrodeN());
    builder.addSlot(RecipeIngredientRole.INPUT, 49, 57).addIngredients(recipe.getElectrodeP());
    if(!recipe.getFluidOut().isEmpty())builder.addSlot(RecipeIngredientRole.OUTPUT,134,51)
      .addFluidStack(recipe.getFluidOut().getFluid(), recipe.getFluidOut().getAmount())
      .setFluidRenderer(400, false, 16, 48);
    builder.addSlot(RecipeIngredientRole.OUTPUT, 49, 81).addItemStack(recipe.getOutputP());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 79).addItemStack(recipe.getOutputN());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 23, 32).addItemStack(recipe.getOutputGasP());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 32).addItemStack(recipe.getOutputGasN());
    if(!recipe.getFluidIn().isEmpty())builder.addSlot(RecipeIngredientRole.INPUT,44,51)
      .addFluidStack(recipe.getFluidIn().getFluid(), recipe.getFluidIn().getAmount())
      .setFluidRenderer(400, false, 64, 48);
  }
  @Override
  public void draw(@Nonnull ElectroLysisRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
    if(recipe.anodeMelts()){
      guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.reagenica.anode_oxidize"), 30, 100, 0xff333333,false);
    }
  }
  
}
