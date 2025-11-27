package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.recipes.StallTradingRecipe;
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

public class TradingCategory implements IRecipeCategory<StallTradingRecipe>{
  public static final ResourceLocation UID = new ResourceLocation(ChemistryMod.MODID, "trading");
  private final IDrawable background;
  private final IDrawable icon;
  public TradingCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation(ChemistryMod.MODID, "textures/gui/container/stall_jei.png"), 0, 0, 176, 80);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.TRADING_STALL.get()));
  }

  @Override
  public RecipeType<StallTradingRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "trading", StallTradingRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.trading");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull StallTradingRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addItemStack(recipe.getMerchandise());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 35).addItemStack(recipe.getPrice());
  } 
}
