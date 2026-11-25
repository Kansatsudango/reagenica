package kandango.reagenica.recipes;

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

public class HeatFurnaceRecipe implements Recipe<Container> {
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
  public ItemStack getOutput() {
    return output;
  }
  private final ItemStack byproduct;
  public ItemStack getByproduct() {
    return byproduct;
  }
  
  public HeatFurnaceRecipe(ResourceLocation id, Ingredient i, ItemStack o1, ItemStack o2, FluidStack fin, FluidStack fout){
    this.id=id;
    this.input=i;
    this.output = o1;
    this.byproduct = o2;
    this.fluidIn = fin;
    this.fluidOut = fout;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return input.test(container.getItem(0));
  }
  private boolean matchest(FluidStack fluid, ItemStack item){
    return input.test(item) && FluidStackUtil.isEnoughFluid(fluid, this.fluidIn);
  }
  public static Optional<HeatFurnaceRecipe> getRecipe(FluidStack fluid, ItemStack item, @Nonnull Level lv){
    return lv.getRecipeManager().getAllRecipesFor(ModRecipes.HEAT_FURNACE_TYPE.get())
            .stream().filter(r -> r.matchest(fluid, item)).findFirst();
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
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.HEAT_FURNACE_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.HEAT_FURNACE_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return output.copy();
  }
}
