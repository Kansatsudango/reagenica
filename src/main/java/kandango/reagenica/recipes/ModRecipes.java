package kandango.reagenica.recipes;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipes {
  public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
    DeferredRegister.create(Registries.RECIPE_SERIALIZER, "reagenica");

  public static final DeferredRegister<RecipeType<?>> TYPES =
    DeferredRegister.create(Registries.RECIPE_TYPE, "reagenica");

  public static final DeferredHolder<RecipeType<?>, RecipeType<ReagentMixingRecipe>> REAGENT_MIXING_TYPE = recipeType("reagent_mixing");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ReagentMixingRecipe>> REAGENT_MIXING_SERIALIZER =
    SERIALIZERS.register("reagent_mixing", ReagentMixingRecipeSerializer::new);

  public static final DeferredHolder<RecipeType<?>, RecipeType<FermentationRecipe>> FERMENTATION_TYPE = recipeType("fermentation");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FermentationRecipe>> FERMENTATION_SERIALIZER = 
    SERIALIZERS.register("fermentation", FermentationRecipeSerializer::new);

  public static final DeferredHolder<RecipeType<?>, RecipeType<BlastFurnaceRecipe>> BLAST_FURNACE_TYPE = recipeType("blast_furnace");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BlastFurnaceRecipe>> BLAST_FURNACE_SERIALIZER = 
    SERIALIZERS.register("blast_furnace", BlastFurnaceRecipeSerializer::new);

  public static final DeferredHolder<RecipeType<?>, RecipeType<FractionalDistillerRecipe>> DISTILLING_TYPE = recipeType("distilling");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FractionalDistillerRecipe>> DISTILLING_SERIALIZER = 
    SERIALIZERS.register("distilling", FractionalDistillerRecipeSerializer::new);
  
  public static final DeferredHolder<RecipeType<?>, RecipeType<HeatFurnaceRecipe>> HEAT_FURNACE_TYPE = recipeType("heat_furnace");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HeatFurnaceRecipe>> HEAT_FURNACE_SERIALIZER = 
    SERIALIZERS.register("heat_furnace", HeatFurnaceRecipeSerializer::new);

  public static final DeferredHolder<RecipeType<?>, RecipeType<CrusherRecipe>> CRUSHER_TYPE = recipeType("crushing");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrusherRecipe>> CRUSHER_SERIALIZER = 
    SERIALIZERS.register("crushing", CrusherRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<ElectroLysisRecipe>> ELECTROLYSIS_TYPE = recipeType("electrolysis");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectroLysisRecipe>> ELECTROLYSIS_SERIALIZER = 
    SERIALIZERS.register("electrolysis", ElectroLysisRecipeSerializer::new);

  public static final DeferredHolder<RecipeType<?>, RecipeType<StallTradingRecipe>> STALL_TRADING_TYPE = recipeType("stall_trading");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<StallTradingRecipe>> STALL_TRADING_SERIALIZER = 
    SERIALIZERS.register("stall_trading", StallTradingRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<AnalyzerRecipe>> ANALYZER_TYPE = recipeType("analyze");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AnalyzerRecipe>> ANALYZER_SERIALIZER = 
    SERIALIZERS.register("analyze", AnalyzerRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<DissolverRecipe>> DISSSOLVER_TYPE = recipeType("dissolving");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DissolverRecipe>> DISSOLVER_SERIALIZER = 
    SERIALIZERS.register("dissolving", DissolverRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<ReactorRecipe>> REACTOR_TYPE = recipeType("reactor");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ReactorRecipe>> REACTOR_SERIALIZER = 
    SERIALIZERS.register("reactor", ReactorRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<HydrogenReductorRecipe>> HYDROGEN_REDUCTOR_TYPE = recipeType("hydrogen_reductor");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HydrogenReductorRecipe>> HYDROGEN_REDUCTOR_SERIALIZER = 
    SERIALIZERS.register("hydrogen_reductor", HydrogenReductorRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<CookingRecipe>> COOKING_TYPE = recipeType("cooking");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER = 
    SERIALIZERS.register("cooking", CookingRecipeSerializer::new);
    
  public static final DeferredHolder<RecipeType<?>, RecipeType<ReagenimartRecipe>> REAGENIMART_TYPE = recipeType("reagenimart");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ReagenimartRecipe>> REAGENIMART_SERIALIZER = 
    SERIALIZERS.register("reagenimart", ReagenimartRecipeSerializer::new);

  public static final DeferredHolder<RecipeType<?>, RecipeType<BatteryUpgradeRecipe>> BATTERY_UPGRADE_TYPE = recipeType("battery_upgrade");
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BatteryUpgradeRecipe>> BATTERY_UPGRADE_SERIALIZER = 
    SERIALIZERS.register("battery_upgrade", BatteryUpgradeRecipeSerializer::new);

  public static void register(IEventBus bus) {
    SERIALIZERS.register(bus);
    TYPES.register(bus);
  }

  public static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> recipeType(String name){
    return TYPES.register(name, () -> RecipeType.<T>simple(ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, name)));
  }
}
