package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.jei.util.ReagenicaTank;
import kandango.reagenica.recipes.HaberBoschRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class HaberBoschCategory implements IRecipeCategory<HaberBoschRecipe>{
  public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("reagenica", "haber_bosch");
  public static final RecipeType<HaberBoschRecipe> TYPE = RecipeType.create("reagenica", "haber_bosch", HaberBoschRecipe.class);
  private final IDrawable background;
  private final IDrawable icon;

  public HaberBoschCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(ResourceLocation.fromNamespaceAndPath("reagenica", "textures/gui/container/haber_bosch.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.HABER_BOSCH.get()));
  }

  @Override
  public RecipeType<HaberBoschRecipe> getRecipeType(){
    return RecipeType.create("reagenica", "haber_bosch", HaberBoschRecipe.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.haber_bosch");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull HaberBoschRecipe recipe, @Nonnull IFocusGroup fg){
    ReagenicaTank.create( 9,30, 29, 30).setFluid(recipe.hydro()).consumeAsInputTank(builder);
    ReagenicaTank.create(51,30, 71, 30).setFluid(recipe.nitro()).consumeAsInputTank(builder);
    ReagenicaTank.create(116,30, 136, 62).setFluid(recipe.ammo()).consumeAsOutputTank(builder);
  }
}
