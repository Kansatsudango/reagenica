package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.FluidBurningRecipe;
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

public class FluidBurningCategory implements IRecipeCategory<FluidBurningRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "fluid_burning");
  public static final RecipeType<FluidBurningRecipe> TYPE = RecipeType.create("reagenica", "fluid_burning", FluidBurningRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public FluidBurningCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/fluid_fuel_generator.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.FLUID_FUEL_GENERATOR.get()));
  }

  @Override
  public RecipeType<FluidBurningRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "fluid_burning", FluidBurningRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.fluid_burning");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FluidBurningRecipe recipe, @Nonnull IFocusGroup fg){
    if(!recipe.in().isEmpty())builder.addSlot(RecipeIngredientRole.INPUT,44,23)
      .addFluidStack(recipe.in().getFluid(), recipe.in().getAmount())
      .setFluidRenderer(4000, false, 16, 48);
  }
  @Override
  public void draw(@Nonnull FluidBurningRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
    guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.reagenica.burn_tick", recipe.tick()), 104, 57, 0xff111111,false);
    guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.reagenica.burn_energy", recipe.ept()), 104, 67, 0xff111111,false);
  }
}
