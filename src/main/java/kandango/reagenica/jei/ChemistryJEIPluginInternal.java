package kandango.reagenica.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.HaberBoschBlockEntity;
import kandango.reagenica.block.entity.electrical.AirSeparatorBlockEntity;
import kandango.reagenica.block.fluid.ChemiFluidBurnMap;
import kandango.reagenica.block.fluid.ChemiFluidBurnrate;
import kandango.reagenica.item.reagent.ReagentFluidMap;
import kandango.reagenica.recipes.AirSeparationRecipe;
import kandango.reagenica.recipes.FluidBurningRecipe;
import kandango.reagenica.recipes.FluidDrainingRecipe;
import kandango.reagenica.recipes.FluidFillingRecipe;
import kandango.reagenica.recipes.HaberBoschRecipe;
import kandango.reagenica.recipes.IncubatorRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

@JeiPlugin
public class ChemistryJEIPluginInternal implements IModPlugin{
  private static final ResourceLocation PLUGIN_ID = new ResourceLocation("reagenica","jei_plugin_internal");
  @Override
  public ResourceLocation getPluginUid() {
    return PLUGIN_ID;
  }

  @Override
  public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(new FluidFillingCategory(registration.getJeiHelpers()));
    registration.addRecipeCategories(new FluidDrainingCategory(registration.getJeiHelpers()));
    registration.addRecipeCategories(new FluidBurningCategory(registration.getJeiHelpers()));
    registration.addRecipeCategories(new HaberBoschCategory(registration.getJeiHelpers()));
    registration.addRecipeCategories(new AirSeparationCategory(registration.getJeiHelpers()));
    registration.addRecipeCategories(new IncubatorCategory(registration.getJeiHelpers()));
  }

  @Override
  public void registerRecipes(@Nonnull IRecipeRegistration registration) {
    {
      List<FluidFillingRecipe> recipes = new ArrayList<>();
      for (Map.Entry<Fluid, Item> entry : ReagentFluidMap.fluidItemMap.entrySet()) {
        FluidStack input = new FluidStack(entry.getKey(),100);
        ItemStack output = new ItemStack(entry.getValue());
        recipes.add(new FluidFillingRecipe(input, output));
      }
      registration.addRecipes(FluidFillingCategory.TYPE, recipes);
    }{
      List<FluidDrainingRecipe> recipes = new ArrayList<>();
      for (Map.Entry<Item, Fluid> entry : ReagentFluidMap.itemFluidMap.entrySet()) {
        ItemStack input = new ItemStack(entry.getKey());
        FluidStack output = new FluidStack(entry.getValue(),100);
        recipes.add(new FluidDrainingRecipe(input, output));
      }
      registration.addRecipes(FluidDrainingCategory.TYPE, recipes);
    }{
      List<FluidBurningRecipe> recipes = new ArrayList<>();
      for (Map.Entry<Fluid, ChemiFluidBurnrate> entry : ChemiFluidBurnMap.fluidBurnMap.entrySet()) {
        FluidStack input = new FluidStack(entry.getKey(), 1000);
        ChemiFluidBurnrate rate = entry.getValue();
        recipes.add(new FluidBurningRecipe(input, rate.burnTick(), rate.energyPerTick()));
      }
      registration.addRecipes(FluidBurningCategory.TYPE, recipes);
    }{
      List<HaberBoschRecipe> recipes = new ArrayList<>();
      recipes.add(new HaberBoschRecipe(new FluidStack(ChemiFluids.NITROGEN.getFluid(), HaberBoschBlockEntity.REACTION_UNIT/2),
                                       new FluidStack(ChemiFluids.HYDROGEN.getFluid(), HaberBoschBlockEntity.REACTION_UNIT*3/2),
                                       new FluidStack(ChemiFluids.AMMONIA.getFluid(), HaberBoschBlockEntity.REACTION_UNIT)));
      registration.addRecipes(HaberBoschCategory.TYPE, recipes);
    }{
      List<AirSeparationRecipe> recipes = new ArrayList<>();
      recipes.add(new AirSeparationRecipe(Ingredient.of(new ItemStack(ChemiItems.COPPER_FILTER.get()),new ItemStack(ChemiItems.SILVER_FILTER.get()),new ItemStack(ChemiItems.PLATINUM_FILTER.get())),
                                        new FluidStack(ChemiFluids.NITROGEN.getFluid(), AirSeparatorBlockEntity.PRODUCTION_UNIT*4),
                                       new FluidStack(ChemiFluids.OXYGEN.getFluid(), AirSeparatorBlockEntity.PRODUCTION_UNIT)));
      registration.addRecipes(AirSeparationCategory.TYPE, recipes);
    }{
      List<IncubatorRecipe> recipes = IncubatorRecipe.getRecipes();
      registration.addRecipes(IncubatorCategory.TYPE, recipes);
    }
  }

  @Override
  public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration reg){
    reg.addRecipeCatalyst(new ItemStack(ChemiBlocks.COPPER_TANK.get()), FluidFillingCategory.TYPE);
    reg.addRecipeCatalyst(new ItemStack(ChemiBlocks.COPPER_TANK.get()), FluidDrainingCategory.TYPE);
    reg.addRecipeCatalyst(new ItemStack(ChemiBlocks.FLUID_FUEL_GENERATOR.get()), FluidBurningCategory.TYPE);
    reg.addRecipeCatalyst(new ItemStack(ChemiBlocks.HABER_BOSCH.get()), HaberBoschCategory.TYPE);
    reg.addRecipeCatalyst(new ItemStack(ChemiBlocks.AIR_SEPARATOR.get()), AirSeparationCategory.TYPE);
    reg.addRecipeCatalyst(new ItemStack(ChemiBlocks.INCUBATOR.get()), IncubatorCategory.TYPE);
  }
}