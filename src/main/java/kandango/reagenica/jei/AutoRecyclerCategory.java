package kandango.reagenica.jei;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import kandango.reagenica.jei.util.ReagenicaTank;
import kandango.reagenica.recipes.AutoRecyclerRecipe;
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
import net.minecraftforge.fluids.FluidStack;

public class AutoRecyclerCategory implements IRecipeCategory<AutoRecyclerRecipe>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "auto_recycler");
  private final IDrawable background;
  private final IDrawable icon;

  public AutoRecyclerCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/auto_recycler.png"), 0, 0, 176, 85);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.AUTO_RECYCLER.get()));
  }

  @Override
  public RecipeType<AutoRecyclerRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "auto_recycler", AutoRecyclerRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.auto_recycler");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AutoRecyclerRecipe recipe, @Nonnull IFocusGroup fg){
    builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addIngredients(recipe.getInput());
    List<ItemStackWithChance> results = recipe.getResults();
    for(int i=0;i<results.size();i++){
      ItemStackWithChance item = results.get(i);
      int x = i<=2 ? 108 : 126;
      int y = 17+(i%3)*18;
      builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(item.get()).addTooltipCallback((view, tooltip) -> addTooltip(view, tooltip, item.getChance()));
    }
    ReagenicaTank.create(29,21, 8, 21).setFluid(new FluidStack(ChemiFluids.SULFURIC_ACID.getFluid(), 25)).consumeAsInputTank(builder);
  }
  private void addTooltip(IRecipeSlotView view, List<Component> tooltip, float chance){
    tooltip.add(Component.literal((int)(chance*100) + "% chance"));
  }
  
}
