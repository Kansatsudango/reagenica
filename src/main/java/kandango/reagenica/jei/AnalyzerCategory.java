package kandango.reagenica.jei;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.AnalyzerRecipe;
import kandango.reagenica.recipes.items.ItemStackWithChance;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AnalyzerCategory implements IRecipeCategory<AnalyzerRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "analyzer");
  private final IDrawable background;
  private final IDrawable icon;

  public AnalyzerCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/analyzer.png"), 0, 0, 176, 85);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.ANALYZER.get()));
  }

  @Override
  public RecipeType<AnalyzerRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "analyzer", AnalyzerRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.analyzer");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AnalyzerRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addIngredients(recipe.getInput());
    List<ItemStackWithChance> results = recipe.getResults();
    for(int i=0;i<results.size();i++){
      ItemStackWithChance item = results.get(i);
      int x = i<=2 ? 108 : 126;
      int y = 17+(i%3)*18;
      builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(item.get()).addTooltipCallback((view, tooltip) -> addTooltip(view, tooltip, item.getChance()));
    }
  }
  private void addTooltip(IRecipeSlotView view, List<Component> tooltip, float chance){
    tooltip.add(Component.literal((int)(chance*100) + "% chance"));
  }
  
}
