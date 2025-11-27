package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.client.ClientRenderUtil;
import kandango.reagenica.recipes.BlastFurnaceRecipe;
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

public class BlastFurnaceCategory implements IRecipeCategory<BlastFurnaceRecipe>{
  public static final ResourceLocation UID = new ResourceLocation(ChemistryMod.MODID, "blast_furnace");
  private final IDrawable background;
  private final IDrawable icon;
  public BlastFurnaceCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation(ChemistryMod.MODID, "textures/gui/container/blast_furnace.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.BLASTFURNACE_BOTTOM.get()));
  }

  @Override
  public RecipeType<BlastFurnaceRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "blast_furnace", BlastFurnaceRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.blast_furnace");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull BlastFurnaceRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 56, 23).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 24).addItemStack(recipe.getOutput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 48).addItemStack(recipe.getByproduct());
  }

  public void draw(@Nonnull BlastFurnaceRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
    ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/blast_furnace.png");
    int temp_min = recipe.getMinTemp()/10;
    int temp_max = recipe.getMaxTemp()/10;
    ClientRenderUtil.renderEnergyInGui(TEXTURE, guiGraphics, 0, 0, temp_max, 1600, 148, 18, 176, 31, 4, 48);
    if (mouseX >= 148 && mouseX <= 151 && mouseY >= 18 && mouseY <= 65) {
      guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(temp_min + " ℃ - " + temp_max + " ℃"), (int)mouseX, (int)mouseY);
    }
  }
  
}
