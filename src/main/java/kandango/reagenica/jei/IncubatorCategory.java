package kandango.reagenica.jei;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.IncubatorRecipe;
import kandango.reagenica.recipes.items.ItemStackWithChance;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
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

public class IncubatorCategory implements IRecipeCategory<IncubatorRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "incubator");
  public static final RecipeType<IncubatorRecipe> TYPE = RecipeType.create("reagenica", "incubator", IncubatorRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public IncubatorCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/incubator_jei.png"), 0, 0, 176, 90);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.INCUBATOR.get()));
  }

  @Override
  public RecipeType<IncubatorRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "incubator", IncubatorRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.incubator");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IncubatorRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 62, 37).addItemStack(recipe.in());
    List<ItemStackWithChance> results = recipe.out();
    for(int i=0;i<results.size();i++){
      ItemStackWithChance item = results.get(i);
      int x = i<=2 ? 98 : 116;
      int y = 19+(i%3)*18;
      builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(item.get()).addTooltipCallback((view, tooltip) -> addTooltip(view, tooltip, item.getChance()));
    }
  }
  private void addTooltip(IRecipeSlotView view, List<Component> tooltip, float chance){
    tooltip.add(Component.literal((int)(chance*100) + "% chance"));
  }
  @Override
  public void draw(@Nonnull IncubatorRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
    guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.reagenica.contamination_top"), 8, 73, 0xff111111,false);
    guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.reagenica.contamination_bottom"), 8, 81, 0xff111111,false);
  }
}
