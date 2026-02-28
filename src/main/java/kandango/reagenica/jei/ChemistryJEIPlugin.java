package kandango.reagenica.jei;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.recipes.AnalyzerRecipe;
import kandango.reagenica.recipes.BlastFurnaceRecipe;
import kandango.reagenica.recipes.CookingRecipe;
import kandango.reagenica.recipes.CrusherRecipe;
import kandango.reagenica.recipes.DissolverRecipe;
import kandango.reagenica.recipes.ElectroLysisRecipe;
import kandango.reagenica.recipes.FermentationRecipe;
import kandango.reagenica.recipes.FractionalDistillerRecipe;
import kandango.reagenica.recipes.HeatFurnaceRecipe;
import kandango.reagenica.recipes.HydrogenReductorRecipe;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.recipes.ReactorRecipe;
import kandango.reagenica.recipes.ReagentMixingRecipe;
import kandango.reagenica.recipes.StallTradingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class ChemistryJEIPlugin implements IModPlugin{
  private static final ResourceLocation PLUGIN_ID = new ResourceLocation("reagenica","jei_plugin");
  private static List<JEIPluginRecipes<? extends Container, ? extends Recipe<?>>> pluginrecipes = List.of(
    new JEIPluginRecipes<>(
      "reagent_mixing",
      ReagentMixingRecipe.class,
      ModRecipes.REAGENT_MIXING_TYPE,
      helper -> new ReagentMixingCategory(helper),
      () -> new ItemStack(ChemiBlocks.EXPERIMENT_BLOCK.get())
    ),
    new JEIPluginRecipes<>(
      "fermentation",
      FermentationRecipe.class,
      ModRecipes.FERMENTATION_TYPE,
      helper -> new FermentingCategory(helper),
      () -> new ItemStack(ChemiBlocks.CHEMICAL_FERMENTER.get())
    ),
    new JEIPluginRecipes<>(
      "blast_furnace",
      BlastFurnaceRecipe.class,
      ModRecipes.BLAST_FURNACE_TYPE,
      helper -> new BlastFurnaceCategory(helper),
      () -> new ItemStack(ChemiBlocks.BLASTFURNACE_BOTTOM.get())
    ),
    new JEIPluginRecipes<>(
      "heat_furnace",
      HeatFurnaceRecipe.class,
      ModRecipes.HEAT_FURNACE_TYPE,
      helper -> new HeatFurnaceCategory(helper),
      () -> new ItemStack(ChemiBlocks.HEAT_FURNACE.get())
    ),
    new JEIPluginRecipes<>(
      "distilling",
      FractionalDistillerRecipe.class,
      ModRecipes.DISTILLING_TYPE,
      helper -> new FractionalDistillerCategory(helper),
      () -> new ItemStack(ChemiBlocks.FRACTIONAL_DISTILLER_BOTTOM.get())
    ),
    new JEIPluginRecipes<>(
      "electrolysis",
      ElectroLysisRecipe.class,
      ModRecipes.ELECTROLYSIS_TYPE,
      helper -> new ElectroLysisCategory(helper),
      () -> new ItemStack(ChemiBlocks.ELECTROLYSIS_DEVICE.get())
    ),
    new JEIPluginRecipes<>(
      "analyzer",
      AnalyzerRecipe.class,
      ModRecipes.ANALYZER_TYPE,
      helper -> new AnalyzerCategory(helper),
      () -> new ItemStack(ChemiBlocks.ANALYZER.get())
    ),
    new JEIPluginRecipes<>(
      "crusher",
      CrusherRecipe.class,
      ModRecipes.CRUSHER_TYPE,
      helper -> new CrusherCategory(helper),
      () -> new ItemStack(ChemiBlocks.CRUSHER.get())
    ),
    new JEIPluginRecipes<>(
      "dissolver",
      DissolverRecipe.class,
      ModRecipes.DISSSOLVER_TYPE,
      helper -> new DissolverCategory(helper),
      () -> new ItemStack(ChemiBlocks.DISSOLVER.get())
    ),
    new JEIPluginRecipes<>(
      "reactor",
      ReactorRecipe.class,
      ModRecipes.REACTOR_TYPE,
      helper -> new ReactorCategory(helper),
      () -> new ItemStack(ChemiBlocks.REACTOR.get())
    ),
    new JEIPluginRecipes<>(
      "cooking",
      CookingRecipe.class,
      ModRecipes.COOKING_TYPE,
      helper -> new CookingCategory(helper),
      () -> new ItemStack(ChemiBlocks.COOKING_POT.get())
    ),
    new JEIPluginRecipes<>(
      "trading",
      StallTradingRecipe.class,
      ModRecipes.STALL_TRADING_TYPE,
      helper -> new TradingCategory(helper),
      () -> new ItemStack(ChemiBlocks.TRADING_STALL.get())
    ),
    new JEIPluginRecipes<>(
      "hydrogen_reductor",
      HydrogenReductorRecipe.class,
      ModRecipes.HYDROGEN_REDUCTOR_TYPE,
      helper -> new HydrogenReductorCategory(helper),
      () -> new ItemStack(ChemiBlocks.HYDROGEN_REDUCTOR.get())
    )
  );

  @Override
  public ResourceLocation getPluginUid(){
    return PLUGIN_ID;
  }

  @Override
  public void registerCategories(@Nonnull IRecipeCategoryRegistration reg){
    for(JEIPluginRecipes<? extends Container, ? extends Recipe<?>> def : pluginrecipes){
      reg.addRecipeCategories(def.category.apply(reg.getJeiHelpers()));
    }
  }

  @Override
  public void registerRecipes(@Nonnull IRecipeRegistration reg){
    Minecraft mc = Minecraft.getInstance();
    Objects.requireNonNull(mc.level,"Ancient NullPointer Attack!!");
    RecipeManager recipeManager = mc.level.getRecipeManager();
    for (JEIPluginRecipes<? extends Container,? extends Recipe<?>> def : pluginrecipes) {
      registerRecipeHelper(reg, recipeManager, def);
    }
  }
  private <C extends Container, T extends Recipe<C>> void registerRecipeHelper(IRecipeRegistration reg, RecipeManager manager, JEIPluginRecipes<C, T> def) {
    reg.addRecipes(def.jeirecipetype, def.getAllRecipes(manager));
  }

  @Override
  public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration reg){
    for (JEIPluginRecipes<?,?> def : pluginrecipes) {
      reg.addRecipeCatalyst(def.catalyst.get(), def.jeirecipetype);
    }
  }
}