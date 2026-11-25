package kandango.reagenica.recipes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.item.burnable.HeatProvidingItem;
import kandango.reagenica.recipes.items.IngredientWithCount;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class ReagentMixingRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final List<IngredientWithCount> inputs;
  public List<IngredientWithCount> getInputs() {
    return inputs;
  }
  private final ItemStack outputA;
  public ItemStack getOutputA() {
    return outputA;
  }
  private final ItemStack outputB;
  public ItemStack getOutputB() {
    return outputB;
  }
  private final Ingredient catalyst;
  public Ingredient getCatalyst() {
    return catalyst;
  }
  private final boolean isHeatRequired;

  public boolean isHeatRequired() {
    return isHeatRequired;
  }

  public ReagentMixingRecipe(ResourceLocation id, List<IngredientWithCount> ings, ItemStack oA, ItemStack oB, Ingredient ca,boolean heat){
    this.id=id;
    this.inputs=ings;
    this.outputA=oA;
    this.outputB=oB;
    this.catalyst=ca;
    this.isHeatRequired=heat;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    boolean inputreagent = true;
    for(IngredientWithCount ic : this.inputs){
      boolean flag=false;
      for(int i=0;i<2;i++){
        flag |= ic.test(container.getItem(i));
      }
      if(!flag)return false;
    }
    ItemStack flame = container.getItem(2);
    boolean heatExists = !isHeatRequired || (flame.getItem() instanceof HeatProvidingItem && flame.getDamageValue() < flame.getMaxDamage());
    boolean catalyst = this.catalyst==Ingredient.EMPTY || this.catalyst.test(container.getItem(3));
    return inputreagent && heatExists && catalyst;
  }
  public static Optional<ReagentMixingRecipe> getRecipe(@Nonnull Container container,@Nonnull Level lv){
    List<ReagentMixingRecipe> recipes = lv.getRecipeManager().getAllRecipesFor(ModRecipes.REAGENT_MIXING_TYPE.get());
    return recipes.stream().filter(r -> r.matches(container,lv)).max(Comparator.comparingInt(r -> r.priority()));
  }
  private int priority(){
    int input = this.inputs.size();
    boolean heat = this.isHeatRequired;
    boolean cat = !this.catalyst.isEmpty();
    return input*4+(cat?2:0)+(heat?1:0);
  }

  @Override
  public ItemStack assemble(@Nonnull Container container,@Nonnull RegistryAccess rg) {
      return outputA.copy();
  }
  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return outputA; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.REAGENT_MIXING_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.REAGENT_MIXING_TYPE.get(); }
}
