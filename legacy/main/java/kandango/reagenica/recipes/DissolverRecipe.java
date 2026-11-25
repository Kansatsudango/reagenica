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

public class DissolverRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final Ingredient input;
  public Ingredient getInput() {
    return input;
  }
  private final FluidStack fluidIn;
  public FluidStack getFluidIn() {
    return fluidIn;
  }
  private final FluidStack fluidOut;
  public FluidStack getFluidOut() {
    return fluidOut;
  }
  private final ItemStack output;
  public ItemStack getOutputItem() {
    return output;
  }
  
  public DissolverRecipe(ResourceLocation id, Ingredient i, FluidStack fin, FluidStack fout, ItemStack out){
    this.id=id;
    this.input=i;
    this.fluidIn = fin;
    this.fluidOut = fout;
    this.output=out;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return input.test(container.getItem(0));
  }
  private boolean matchest(ItemStack item,FluidStack fluid){
    return input.test(item) && FluidStackUtil.isEnoughFluid(fluid, this.fluidIn);
  }
  public static Optional<DissolverRecipe> getRecipe(@Nonnull FluidStack fluidstack, ItemStack item, @Nonnull Level level){
    List<DissolverRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.DISSSOLVER_TYPE.get());
    return recipes.stream().filter(r -> r.matchest(item,fluidstack)).findFirst();
  }

  public FluidStack getResultFluid(){
    return fluidOut.copy();
  }
  public FluidStack getInputFluid(){
    return fluidIn.copy();
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return output; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.DISSOLVER_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.DISSSOLVER_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return output;
  }
}
