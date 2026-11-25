package kandango.reagenica.jei;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.farming.MushroomBed.MushroomMutationManager.BlockCounts;
import kandango.reagenica.block.farming.MushroomBed.MushroomMutationManager.MutationCondition;
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

public class MutationConditionCategory implements IRecipeCategory<MutationCondition>{
  public static final ResourceLocation UID = new ResourceLocation("reagenica", "mutation_condition");
  public static final RecipeType<MutationCondition> TYPE = RecipeType.create("reagenica", "mutation_condition", MutationCondition.class);
  private final IDrawable background;
  private final IDrawable icon;

  public MutationConditionCategory(IJeiHelpers helpers){
    this.background = helpers.getGuiHelper().createDrawable(new ResourceLocation("reagenica", "textures/gui/container/mutation_jei.png"), 0, 0, 176, 82);
    this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChemiBlocks.MUSHROOM_BED.get()));
  }

  @Override
  public RecipeType<MutationCondition> getRecipeType(){
    return RecipeType.create("reagenica", "mutation_condition", MutationCondition.class);
  }

  @Override
  public Component getTitle(){
    return Component.translatable("jei.reagenica.mutation_condition");
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
  public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull MutationCondition condition, @Nonnull IFocusGroup fg){
    int count=0;
    for(int i=0;i<condition.conditions().size();i++){
      BlockCounts bc = condition.conditions().get(i);
      for(int j=0;j<bc.count();j++){
        int slot = count<4?count:count+1;
        builder.addSlot(RecipeIngredientRole.INPUT, 62+(slot%3)*18, 17+(slot/3)*18).addItemStack(bc.block().asItem().getDefaultInstance());
        count++;
      }
    }
    builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 35).addItemStack(condition.result().getBlock().asItem().getDefaultInstance());
  }
  
}
