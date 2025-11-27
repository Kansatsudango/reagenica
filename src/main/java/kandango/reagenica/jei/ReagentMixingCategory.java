package kandango.reagenica.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.client.ClientRenderUtil;
import kandango.reagenica.recipes.ReagentMixingRecipe;
import kandango.reagenica.recipes.items.IngredientWithCount;
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

public class ReagentMixingCategory implements IRecipeCategory<ReagentMixingRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "reagent_mixing");
  private final IDrawable background;
  private final IDrawable icon;

  public ReagentMixingCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/experiment_block.png"), 0, 0, 176, 85);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.EXPERIMENT_BLOCK.get()));
  }

  @Override
  public RecipeType<ReagentMixingRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "reagent_mixing", ReagentMixingRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.reagent_mixing");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ReagentMixingRecipe recipe, @Nonnull IFocusGroup fg){
    for(int i=0;i<recipe.getInputs().size();i++){
      IngredientWithCount ic = recipe.getInputs().get(i);
      List<ItemStack> listitems = new ArrayList<>();
      for(int j=0;j<ic.getIngredient().getItems().length;j++){
        listitems.add(ic.toItemStack(j));
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 56, i==0?17:53).addItemStacks(listitems);
    }
    builder.addSlot(RecipeIngredientRole.INPUT, 97, 55).addIngredients(recipe.getCatalyst());
    builder.addSlot(RecipeIngredientRole.INPUT, 79, 55).addItemStacks(recipe.isHeatRequired()?List.of(new ItemStack(ChemiItems.ALCHOHOL_LAMP.get()), new ItemStack(ChemiItems.EASY_TORCH.get())):List.of());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 149, 14).addItemStack(recipe.getOutputA());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 149, 32).addItemStack(recipe.getOutputB());
  }
  
  @Override
  public void draw(@Nonnull ReagentMixingRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
    ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/experiment_block.png");
    if(recipe.isHeatRequired()){
      ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, guiGraphics, 0, 0, 1, 1, 79, 39);
      if (mouseX >= 79 && mouseX <= 93 && mouseY >= 40 && mouseY <= 53) {
        guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal("Heating Required"), (int)mouseX, (int)mouseY);
      }
    }
  }
}
