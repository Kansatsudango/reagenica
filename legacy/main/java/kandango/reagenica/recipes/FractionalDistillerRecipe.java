package kandango.reagenica.recipes;

import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.util.FluidStackUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FractionalDistillerRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final FluidStack input;
  public FluidStack getInput() {
    return input;
  }
  private final FluidStack topfluid;
  public FluidStack getTop() {
    return topfluid;
  }
  private final FluidStack bottomfluid;
  public FluidStack getBottom() {
    return bottomfluid;
  }
  private final ItemStack residual;
  public ItemStack getResidual() {
    return residual;
  }
  
  public FractionalDistillerRecipe(ResourceLocation id, FluidStack i, FluidStack uo, FluidStack lo, ItemStack re){
    this.id=id;
    this.input=i;
    this.topfluid=uo;
    this.bottomfluid=lo;
    this.residual=re;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return false;
  }
  public boolean matchest(@Nonnull FluidStack fluid){
    return FluidStackUtil.isEnoughFluid(fluid, this.input);
  }
  public static Optional<FractionalDistillerRecipe> getRecipe(@Nonnull FluidStack fluidstack, @Nonnull Level level){
    return level.getRecipeManager().getAllRecipesFor(ModRecipes.DISTILLING_TYPE.get())
            .stream().filter(r -> r.matchest(fluidstack)).findFirst();
  }


  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return ItemStack.EMPTY; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.DISTILLING_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.DISTILLING_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return ItemStack.EMPTY;
  }
}
