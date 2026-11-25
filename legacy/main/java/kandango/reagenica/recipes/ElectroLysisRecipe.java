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

public class ElectroLysisRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final FluidStack fluid;
  public FluidStack getFluidIn() {
    return fluid;
  }
  private final Ingredient electrodeN;
  public Ingredient getElectrodeN() {
    return electrodeN;
  }
  private final Ingredient electrodeP;
  public Ingredient getElectrodeP() {
    return electrodeP;
  }
  private final FluidStack generatedfluid;
  public FluidStack getFluidOut() {
    return generatedfluid;
  }
  private final ItemStack outputn;
  public ItemStack getOutputN() {
    return outputn;
  }
  private final ItemStack outputp;
  public ItemStack getOutputP() {
    return outputp;
  }
  private final ItemStack outputgasn;
  public ItemStack getOutputGasN() {
    return outputgasn;
  }
  private final ItemStack outputgasp;
  public ItemStack getOutputGasP() {
    return outputgasp;
  }
  private final boolean anodeMelt;
  public boolean anodeMelts(){
    return anodeMelt;
  }
  
  public ElectroLysisRecipe(ResourceLocation id, FluidStack in, Ingredient en, Ingredient ep, FluidStack gen, ItemStack on, ItemStack op, ItemStack ogn, ItemStack ogp,boolean am){
    this.id=id;
    this.fluid = in;
    this.electrodeN = en;
    this.electrodeP = ep;
    this.generatedfluid = gen;
    this.outputn = on;
    this.outputp = op;
    this.outputgasn = ogn;
    this.outputgasp = ogp;
    this.anodeMelt = am;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    ItemStack en = container.getItem(1);
    ItemStack ep = container.getItem(2);
    return isValidElectrode(this.electrodeN, en, true) && isValidElectrode(this.electrodeP, ep, true);
  }
  public boolean matchest(@Nonnull FluidStack fluid , ItemStack en, ItemStack ep, boolean strict){
    boolean fl = FluidStackUtil.isEnoughFluid(fluid, this.fluid);
    boolean ers = isValidElectrode(this.electrodeN, en, strict) && isValidElectrode(this.electrodeP, ep, strict);
    return fl && ers;
  }
  public static Optional<ElectroLysisRecipe> getRecipe(@Nonnull FluidStack fluidstack, ItemStack en, ItemStack ep, @Nonnull Level level){
    List<ElectroLysisRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.ELECTROLYSIS_TYPE.get());
    return recipes.stream().filter(r -> r.matchest(fluidstack,en,ep,true)).findFirst()
              .or(() -> recipes.stream().filter(r -> r.matchest(fluidstack,en,ep,false)).findFirst());
  }

  private boolean isValidElectrode(Ingredient electrode,ItemStack stack,boolean strict){
    if(electrode.getItems().length != 1)return !strict && electrode.test(stack);
    return electrode.test(stack);
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return outputn; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.ELECTROLYSIS_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.ELECTROLYSIS_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return outputn.copy();
  }
}
