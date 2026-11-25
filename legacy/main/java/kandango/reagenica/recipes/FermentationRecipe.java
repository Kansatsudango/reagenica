package kandango.reagenica.recipes;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.util.FluidStackUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FermentationRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final List<Ingredient> inputs;
  public List<Ingredient> getInputs() {
    return inputs;
  }
  private final FluidStack inputFluid;
  public FluidStack getInputFluid() {
    return inputFluid;
  }
  private final FluidStack output;
  public FluidStack getOutput() {
    return output;
  }
  private final ItemStack outputitem;
  public ItemStack getOutputItem() {
    return outputitem;
  }
  private final Ingredient bioseed;
  public Ingredient getBioseed() {
    return bioseed;
  }
  
  public FermentationRecipe(ResourceLocation id, List<Ingredient> i, FluidStack in, FluidStack o, ItemStack io,Ingredient ca){
    this.id=id;
    this.inputs=i;
    this.inputFluid=in;
    this.output=o;
    this.outputitem = io;
    this.bioseed=ca;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return false;
  }
  private boolean matchest(@Nonnull Container container,FluidStack fluid){
    if(!RecipeHelper.isAllMeets(container, inputs))return false;
    if(!bioseed.test(container.getItem(4)))return false;
    return FluidStackUtil.isEnoughFluid(fluid, this.inputFluid);
  }
  public static Optional<FermentationRecipe> getRecipe(@Nonnull Container container,FluidStack fluid,@Nonnull Level lv){
    List<FermentationRecipe> recipes = lv.getRecipeManager().getAllRecipesFor(ModRecipes.FERMENTATION_TYPE.get());
    return recipes.stream().filter(r -> r.matchest(container,fluid)).findFirst();
  }

  public FluidStack getResultFluid(){
    return output.copy();
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return outputitem; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.FERMENTATION_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.FERMENTATION_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return ItemStack.EMPTY;
  }
}
