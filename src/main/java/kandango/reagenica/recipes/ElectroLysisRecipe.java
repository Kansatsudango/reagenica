package kandango.reagenica.recipes;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.util.FluidStackUtil;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectroLysisRecipe implements Recipe<ElectroLysisRecipe.Input> {
  private final FluidStack fluid;
  private final Ingredient electrodeN;
  private final Ingredient electrodeP;
  public Ingredient getElectrodeP() {
    return electrodeP;
  }
  private final FluidStack generatedfluid;
  private final ItemStack outputn;
  private final ItemStack outputp;
  private final ItemStack outputgasn;
  private final ItemStack outputgasp;
  public Output getOutputs(){
    return new Output(generatedfluid, outputn, outputp, outputgasn, outputgasp);
  }
  private final boolean anodeMelt;
  public boolean anodeMelts(){
    return anodeMelt;
  }
  
  public ElectroLysisRecipe(FluidStack in, Ingredient en, Ingredient ep, FluidStack gen, ItemStack on, ItemStack op, ItemStack ogn, ItemStack ogp,boolean am){
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

  public boolean matchesInput(Input in, boolean strict){
    boolean fl = FluidStackUtil.isEnoughFluid(fluid, this.fluid);
    boolean ers = isValidElectrode(this.electrodeN, in.cathode, strict) && isValidElectrode(this.electrodeP, in.anode, strict);
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
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.ELECTROLYSIS_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.ELECTROLYSIS_TYPE.get(); }

  @Override
  public ItemStack assemble(Input arg0, Provider arg1) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'assemble'");
  }

  @Override
  public ItemStack getResultItem(Provider arg0) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getResultItem'");
  }

  @Override
  public boolean matches(Input input, Level lv) {
    return matchesInput(input, false);
  }

  public static record Input(FluidStack fluid, ItemStack cathode, ItemStack anode) implements RecipeInput{
    @Override
    public ItemStack getItem(int index) {
      return switch (index) {
        case 0 -> cathode;
        case 1 -> anode;
        default -> throw new IndexOutOfBoundsException();
      };
    }

    @Override
    public int size() {
      return 2;
    }
  }
  public static record Output(FluidStack fluid, ItemStack cathode, ItemStack anode, ItemStack cathodeGas, ItemStack anodeGas) {
  }
}
