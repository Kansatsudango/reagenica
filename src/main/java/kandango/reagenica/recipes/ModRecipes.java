package kandango.reagenica.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "reagenica");

    public static final DeferredRegister<RecipeType<?>> TYPES =
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "reagenica");

    public static final DeferredHolder<RecipeType<ReagentMixingRecipe>> REAGENT_MIXING_TYPE =
        TYPES.register("reagent_mixing", () -> new RecipeType<ReagentMixingRecipe>() {
            @Override
            public String toString() {
                return "reagenica:reagent_mixing";
            }
        });
    public static final DeferredHolder<RecipeSerializer<ReagentMixingRecipe>> REAGENT_MIXING_SERIALIZER =
        SERIALIZERS.register("reagent_mixing", ReagentMixingRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<FermentationRecipe>> FERMENTATION_TYPE = 
        TYPES.register("fermentation", () -> new RecipeType<FermentationRecipe>() {
            @Override
            public String toString(){
                return "reagenica:fermentation";
            }
        });
    public static final DeferredHolder<RecipeSerializer<FermentationRecipe>> FERMENTATION_SERIALIZER = 
        SERIALIZERS.register("fermentation", FermentationRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<BlastFurnaceRecipe>> BLAST_FURNACE_TYPE = 
        TYPES.register("blast_furnace", () -> new RecipeType<BlastFurnaceRecipe>() {
            @Override
            public String toString(){
                return "reagenica:blast_furnace";
            }
        });
    public static final DeferredHolder<RecipeSerializer<BlastFurnaceRecipe>> BLAST_FURNACE_SERIALIZER = 
        SERIALIZERS.register("blast_furnace", BlastFurnaceRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<FractionalDistillerRecipe>> DISTILLING_TYPE = 
        TYPES.register("distilling", () -> new RecipeType<FractionalDistillerRecipe>() {
            @Override
            public String toString(){
                return "reagenica:distilling";
            }
        });
    public static final DeferredHolder<RecipeSerializer<FractionalDistillerRecipe>> DISTILLING_SERIALIZER = 
        SERIALIZERS.register("distilling", FractionalDistillerRecipeSerializer::new);
    
    public static final DeferredHolder<RecipeType<HeatFurnaceRecipe>> HEAT_FURNACE_TYPE = 
        TYPES.register("heat_furnace", () -> new RecipeType<HeatFurnaceRecipe>() {
            @Override
            public String toString(){
                return "reagenica:heat_furnace";
            }
        });
    public static final DeferredHolder<RecipeSerializer<HeatFurnaceRecipe>> HEAT_FURNACE_SERIALIZER = 
        SERIALIZERS.register("heat_furnace", HeatFurnaceRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<CrusherRecipe>> CRUSHER_TYPE = 
        TYPES.register("crushing", () -> new RecipeType<CrusherRecipe>() {
            @Override
            public String toString(){
                return "reagenica:crushing";
            }
        });
    public static final DeferredHolder<RecipeSerializer<CrusherRecipe>> CRUSHER_SERIALIZER = 
        SERIALIZERS.register("crushing", CrusherRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<ElectroLysisRecipe>> ELECTROLYSIS_TYPE = 
        TYPES.register("electrolysis", () -> new RecipeType<ElectroLysisRecipe>() {
            @Override
            public String toString(){
                return "reagenica:electrolysis";
            }
        });
    public static final DeferredHolder<RecipeSerializer<ElectroLysisRecipe>> ELECTROLYSIS_SERIALIZER = 
        SERIALIZERS.register("electrolysis", ElectroLysisRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<StallTradingRecipe>> STALL_TRADING_TYPE = 
        TYPES.register("stall_trading", () -> new RecipeType<StallTradingRecipe>() {
            @Override
            public String toString(){
                return "reagenica:stall_trading";
            }
        });
    public static final DeferredHolder<RecipeSerializer<StallTradingRecipe>> STALL_TRADING_SERIALIZER = 
        SERIALIZERS.register("stall_trading", StallTradingRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<AnalyzerRecipe>> ANALYZER_TYPE = 
        TYPES.register("analyze", () -> new RecipeType<AnalyzerRecipe>() {
            @Override
            public String toString(){
                return "reagenica:analyze";
            }
        });
    public static final DeferredHolder<RecipeSerializer<AnalyzerRecipe>> ANALYZER_SERIALIZER = 
        SERIALIZERS.register("analyze", AnalyzerRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<DissolverRecipe>> DISSSOLVER_TYPE = 
        TYPES.register("dissolving", () -> new RecipeType<DissolverRecipe>() {
            @Override
            public String toString(){
                return "reagenica:dissolving";
            }
        });
    public static final DeferredHolder<RecipeSerializer<DissolverRecipe>> DISSOLVER_SERIALIZER = 
        SERIALIZERS.register("dissolving", DissolverRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<ReactorRecipe>> REACTOR_TYPE = 
        TYPES.register("reactor", () -> new RecipeType<ReactorRecipe>() {
            @Override
            public String toString(){
                return "reagenica:reactor";
            }
        });
    public static final DeferredHolder<RecipeSerializer<ReactorRecipe>> REACTOR_SERIALIZER = 
        SERIALIZERS.register("reactor", ReactorRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<HydrogenReductorRecipe>> HYDROGEN_REDUCTOR_TYPE = 
        TYPES.register("hydrogen_reductor", () -> new RecipeType<HydrogenReductorRecipe>() {
            @Override
            public String toString(){
                return "reagenica:hydrogen_reductor";
            }
        });
    public static final DeferredHolder<RecipeSerializer<HydrogenReductorRecipe>> HYDROGEN_REDUCTOR_SERIALIZER = 
        SERIALIZERS.register("hydrogen_reductor", HydrogenReductorRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<CookingRecipe>> COOKING_TYPE = 
        TYPES.register("cooking", () -> new RecipeType<CookingRecipe>() {
            @Override
            public String toString(){
                return "reagenica:cooking";
            }
        });
    public static final DeferredHolder<RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER = 
        SERIALIZERS.register("cooking", CookingRecipeSerializer::new);
        
    public static final DeferredHolder<RecipeType<ReagenimartRecipe>> REAGENIMART_TYPE = 
        TYPES.register("reagenimart", () -> new RecipeType<ReagenimartRecipe>() {
            @Override
            public String toString(){
                return "reagenica:reagenimart";
            }
        });
    public static final DeferredHolder<RecipeSerializer<ReagenimartRecipe>> REAGENIMART_SERIALIZER = 
        SERIALIZERS.register("reagenimart", ReagenimartRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<BatteryUpgradeRecipe>> BATTERY_UPGRADE_TYPE = 
        TYPES.register("battery_upgrade", () -> new RecipeType<BatteryUpgradeRecipe>() {
            @Override
            public String toString(){
                return "reagenica:battery_upgrade";
            }
        });
    public static final DeferredHolder<RecipeSerializer<BatteryUpgradeRecipe>> BATTERY_UPGRADE_SERIALIZER = 
        SERIALIZERS.register("battery_upgrade", BatteryUpgradeRecipeSerializer::new);

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }
}
