package kandango.reagenica;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import kandango.reagenica.item.*;
import kandango.reagenica.item.bioreagent.*;
import kandango.reagenica.item.burnable.*;
import kandango.reagenica.item.farming.*;
import kandango.reagenica.item.reagent.*;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChemiItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChemistryMod.MODID);
    public static List<RegistryObject<? extends Item>> listItems = new ArrayList<>();
    public static List<CreativeTabContent> listCreativeTab = new ArrayList<>();

    public static final RegistryObject<Item> SAPPHIRE = registerandlist("sapphire", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALCHOHOL_LAMP = registerandlist("alchohol_lamp", () -> new AlcoholLamp());
    public static final RegistryObject<Item> EASY_TORCH = registerandlist("easy_torch", () -> new EasyTorch());

    public static final RegistryObject<Item> TESTTUBE = registerandlist("testtube", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_TUBE = registerandlist("water", () -> new LiquidReagent(new ReagentProperties("H2O",0xFF1212A0,0,0,0), () -> Fluids.WATER));
    public static final RegistryObject<Item> DISTILLED_WATER_TUBE = registerandlist("distilled_water", () -> new LiquidReagent(new ReagentProperties("H2O",0xFF4848C0,0,0,0), () -> ChemiFluids.DISTILLED_WATER.getFluid()));
    public static final RegistryObject<Item> HYDROCHLORIC_ACID = registerandlist("hydrochloric_acid", () -> new LiquidReagent(new ReagentProperties("HCl",0xFF4CAF50,3,0,1)));
    public static final RegistryObject<Item> SODIUM_HYDROXIDE = registerandlist("sodium_hydroxide", () -> new LiquidReagent(new ReagentProperties("NaOH",0xFF2196F3,3,0,1), () -> ChemiFluids.SODIUM_HYDROXIDE.getFluid()));
    public static final RegistryObject<Item> SALT_WATER = registerandlist("salt_water", () -> new LiquidReagent(new ReagentProperties("NaCl",0xFFb0e0e6,0,0,0), () -> ChemiFluids.SALT_WATER.getFluid()));
    public static final RegistryObject<Item> SULFURIC_ACID = registerandlist("sulfuric_acid", () -> new LiquidReagent(new ReagentProperties("H2SO4",0xFFDDAA22,3,0,2), () -> ChemiFluids.SULFURIC_ACID.getFluid()));
    public static final RegistryObject<Item> DILUTE_SULFURIC_ACID = registerandlist("dilute_sulfuric_acid", () -> new LiquidReagent(new ReagentProperties("H2SO4",0xFFDDCF7C,3,0,1), () -> ChemiFluids.DILUTE_SULFURIC_ACID.getFluid()));
    public static final RegistryObject<Item> NITRIC_ACID = registerandlist("nitric_acid", () -> new LiquidReagent(new ReagentProperties("HNO3",0xFFFFC014,3,0,2)));
    public static final RegistryObject<Item> NITROUS_ACID = registerandlist("nitrous_acid", () -> new LiquidReagent(new ReagentProperties("HNO2",0xFFf2aa98,4,0,2)));
    public static final RegistryObject<Item> MIXED_ACID = registerandlist("mixed_acid", () -> new LiquidReagent(new ReagentProperties("NO2+,HSO4-",0xFFffa500,3,0,2)));
    public static final RegistryObject<Item> AQUA_REGIA = registerandlist("aqua_regia", () -> new LiquidReagent(new ReagentProperties("NOCl",0xFFFA8072,4,0,3), () -> ChemiFluids.AQUA_REGIA.getFluid()));
    public static final RegistryObject<Item> GOLD_SOLUTION = registerandlist("gold_solution", () -> new LiquidReagent(new ReagentProperties("?",0xFFffd700,4,0,3), () -> ChemiFluids.GOLD_SOLUTION.getFluid()));
    public static final RegistryObject<Item> SILVER_SOLUTION = registerandlist("silver_solution", () -> new LiquidReagent(new ReagentProperties("?",0xFF87cefa,4,0,3), () -> ChemiFluids.SILVER_SOLUTION.getFluid()));
    public static final RegistryObject<Item> PLATINUM_SOLUTION = registerandlist("platinum_solution", () -> new LiquidReagent(new ReagentProperties("?",0xFFdda0dd,4,0,3), () -> ChemiFluids.PLATINUM_SOLUTION.getFluid()));
    public static final RegistryObject<Item> IRIDIUM_SOLUTION = registerandlist("iridium_solution", () -> new LiquidReagent(new ReagentProperties("?",0xFFbde07d,4,0,3)));
    public static final RegistryObject<Item> IRIDIUM_SOLUTION_NEU = registerandlist("iridium_solution_neu", () -> new LiquidReagent(new ReagentProperties("?",0xFFe2dc7f,4,0,3)));
    public static final RegistryObject<Item> ALUMINA_MELT = registerandlist("alumina_melt", () -> new LiquidReagent(new ReagentProperties("?",0xFFcc9988,4,0,3), () -> ChemiFluids.ALUMINA_MELT.getFluid()));
    public static final RegistryObject<Item> BIOETHANOL = registerandlist("bioethanol", () -> new LiquidReagent(new ReagentProperties("CH3CH2OH+?",0xffffddbb,2,1,0), () -> ChemiFluids.BIOETHANOL.getFluid()));
    public static final RegistryObject<Item> ETHANOL = registerandlist("ethanol", () -> new LiquidReagent(new ReagentProperties("CH3CH2OH",0xFFDDDDDD,2,3,0), () -> ChemiFluids.ETHANOL.getFluid()));
    public static final RegistryObject<Item> METHANOL = registerandlist("methanol", () -> new LiquidReagent(new ReagentProperties("CH3OH",0xFFCCCCCC,3,3,0)));
    public static final RegistryObject<Item> PHOSPHORIC_ACID = registerandlist("phosphoric_acid", () -> new LiquidReagent(new ReagentProperties("H3PO4",0xFFFFA07A,2,0,0), () -> ChemiFluids.PHOSPHORIC_ACID.getFluid()));
    public static final RegistryObject<Item> DIAMMONIUM_PHOSPHATE = registerandlist("diammonium_phosphate", () -> new LiquidReagent(new ReagentProperties("(NH4)2HPO4",0xFFF9D284,2,0,1)));
    public static final RegistryObject<Item> COPPER_SULFATE = registerandlist("copper_sulfate", () -> new LiquidReagent(new ReagentProperties("CuSO4",0xFF00AAFF,2,0,1), () -> ChemiFluids.COPPER_SULFATE.getFluid()));
    public static final RegistryObject<Item> SODIUM_SULFATE = registerandlist("sodium_sulfate", () -> new LiquidReagent(new ReagentProperties("Na2SO4",0xFF20B2AA,2,0,1)));
    public static final RegistryObject<Item> CRUDE_OIL = registerandlist("crude_oil", () -> new LiquidReagent(new ReagentProperties("?",0xFF111111,2,3,3), () -> ChemiFluids.CRUDE_OIL.getFluid()));
    public static final RegistryObject<Item> DIESEL_FUEL = registerandlist("diesel_fuel", () -> new LiquidReagent(new ReagentProperties("?",0xFF00ff7f,2,4,2), () -> ChemiFluids.DIESEL_FUEL.getFluid()));
    public static final RegistryObject<Item> NAPHTHA = registerandlist("naphtha", () -> new LiquidReagent(new ReagentProperties("?",0xFFFFFF00,2,3,2), () -> ChemiFluids.NAPHTHA.getFluid()));
    public static final RegistryObject<Item> BENZENE = registerandlist("benzene", () -> new LiquidReagent(new ReagentProperties("C6H6",0xFF808080,3,3,0), () -> ChemiFluids.BENZENE.getFluid()));
    public static final RegistryObject<Item> NITROBENZENE = registerandlist("nitrobenzene", () -> new LiquidReagent(new ReagentProperties("C6H5NO2",0xFFFFD733,3,2,1)));
    public static final RegistryObject<Item> ANILINIUM_CHLORIDE = registerandlist("anilinium_chloride", () -> new LiquidReagent(new ReagentProperties("C6H5NH3Cl",0xFFADFF2F,3,2,0)));
    public static final RegistryObject<Item> ANILINE = registerandlist("aniline", () -> new LiquidReagent(new ReagentProperties("C6H5NH2",0xFF9ACD32,3,2,0)));
    public static final RegistryObject<Item> DIMETHYLANILINE = registerandlist("dimethylaniline", () -> new LiquidReagent(new ReagentProperties("C6H5N(CH3)2",0xFF9bba4c,3,2,0)));
    public static final RegistryObject<Item> BENZENEDIAZONIUM_CHLORIDE = registerandlist("benzenediazonium_chloride", () -> new LiquidReagent(new ReagentProperties("[C6H5N2]Cl",0xFFeacb31,3,2,3)));
    public static final RegistryObject<Item> RED_AZO = registerandlist("red_azo_dye", () -> new LiquidReagent(new ReagentProperties("Virtual",0xFFff0582,1,1,1)));
    public static final RegistryObject<Item> ORANGE_AZO = registerandlist("p_phenylazophenol", () -> new LiquidReagent(new ReagentProperties("C6H5-NN-C6H4OH",0xFFeacb31,1,1,1)));
    public static final RegistryObject<Item> YELLOW_AZO = registerandlist("yellow_azo_dye", () -> new LiquidReagent(new ReagentProperties("Virtual",0xFFfff200,1,1,1)));
    public static final RegistryObject<Item> GREEN_AZO = registerandlist("green_azo_dye", () -> new LiquidReagent(new ReagentProperties("Virtual",0xFF17e800,1,1,1)));
    public static final RegistryObject<Item> BLUE_AZO = registerandlist("blue_azo_dye", () -> new LiquidReagent(new ReagentProperties("Virtual",0xFF00ffd8,1,1,1)));
    public static final RegistryObject<Item> PURPLE_AZO = registerandlist("purple_azo_dye", () -> new LiquidReagent(new ReagentProperties("Virtual",0xFFc300ff,1,1,1)));
    public static final RegistryObject<Item> PHENOL = registerandlist("phenol", () -> new LiquidReagent(new ReagentProperties("C6H5OH",0xFF8E8EFF,3,2,0)));
    public static final RegistryObject<Item> NITROPHENOL = registerandlist("nitrophenol", () -> new LiquidReagent(new ReagentProperties("HO-C6H4-NO2",0xFFf2bd8c,2,2,0)));
    public static final RegistryObject<Item> AMINOPHENOL = registerandlist("aminophenol", () -> new LiquidReagent(new ReagentProperties("HO-C6H4-NH2",0xFFeddc89,2,2,0)));
    public static final RegistryObject<Item> BENZENE_DISULFONIC = registerandlist("benzene_disulfonic", () -> new LiquidReagent(new ReagentProperties("C6H4(SO3H)2",0xFFeac175,2,2,0)));
    public static final RegistryObject<Item> RESORCINOL = registerandlist("resorcinol", () -> new LiquidReagent(new ReagentProperties("C6H4(OH)2",0xFFc0e8e5,2,2,0)));
    public static final RegistryObject<Item> NITROANILINE = registerandlist("nitroaniline", () -> new LiquidReagent(new ReagentProperties("H2N-C6H4-NO2",0xFFf7e75b,2,2,0)));
    public static final RegistryObject<Item> CYCLOHEXANE = registerandlist("cyclohexane", () -> new LiquidReagent(new ReagentProperties("C6H12",0xFFD3D3D3,1,3,0)));
    public static final RegistryObject<Item> ADIPIC_ACID = registerandlist("adipic_acid", () -> new LiquidReagent(new ReagentProperties("HOOC-(CH2)4-COOH",0xFFFFB7B7,2,1,0)));
    public static final RegistryObject<Item> ADIPAMIDE = registerandlist("adipamide", () -> new LiquidReagent(new ReagentProperties("H2NCO-(CH2)4-CONH2",0xFFFFCCFF,2,1,0)));
    public static final RegistryObject<Item> HEXAMETHYLENE_DIAMINE = registerandlist("hexamethylene_diamine", () -> new LiquidReagent(new ReagentProperties("H2N(CH2)6NH2",0xFFD1FFFF,3,2,0)));
    public static final RegistryObject<Item> SLAG_SOLUTION = registerandlist("slag_solution", () -> new LiquidReagent(new ReagentProperties("?",0xFF333333,3,0,1)));
    public static final RegistryObject<Item> ANODE_SOLUTION = registerandlist("anode_solution", () -> new LiquidReagent(new ReagentProperties("?",0xFF333333,3,0,1)));
    public static final RegistryObject<Item> PROTEIN_EXTRACT_ACID = registerandlist("protein_extract_acid", () -> new LiquidReagent(new ReagentProperties("Proteins + HCl",0xFFFF7F50,3,0,0)));
    public static final RegistryObject<Item> PROTEIN_EXTRACT_NEU = registerandlist("protein_extract_neu", () -> new LiquidReagent(new ReagentProperties("Proteins + NaCl",0xFFFFd710,0,0,0)));
    public static final RegistryObject<Item> MEDIUM_AGAR = registerandlist("medium_agar", () -> new LiquidReagent(new ReagentProperties("Proteins + NaCl + Agar",0xFFFFd700,0,0,0)));
    public static final RegistryObject<Item> CHLORINE = registerandlist("chlorine", () -> new GasReagent(new ReagentProperties("Cl2",0xFFB8D200,3,0,2)));
    public static final RegistryObject<Item> HYDROGEN_CHLORIDE = registerandlist("hydrogen_chloride", () -> new GasReagent(new ReagentProperties("HCl",0xFF90BC00,3,0,1)));
    public static final RegistryObject<Item> OXYGEN = registerandlist("oxygen", () -> new GasReagent(new ReagentProperties("O2",0xFFfef4f4,1,4,3), () -> ChemiFluids.OXYGEN.getFluid()));
    public static final RegistryObject<Item> HYDROGEN = registerandlist("hydrogen", () -> new GasReagent(new ReagentProperties("H2",0xFFdddddd,0,4,3), () -> ChemiFluids.HYDROGEN.getFluid()));
    public static final RegistryObject<Item> NITROGEN = registerandlist("nitrogen", () -> new GasReagent(new ReagentProperties("N2",0xFFf0faff,0,0,0), () -> ChemiFluids.NITROGEN.getFluid()));
    public static final RegistryObject<Item> NITROGEN_DIOXIDE = registerandlist("nitrogen_dioxide", () -> new GasReagent(new ReagentProperties("NO2",0xFFdb9a20,3,0,0)));
    public static final RegistryObject<Item> NITROGEN_MONOXIDE = registerandlist("nitrogen_monoxide", () -> new GasReagent(new ReagentProperties("NO",0xFFdd6f21,3,0,3)));
    public static final RegistryObject<Item> NITROUS_OXIDE = registerandlist("nitrous_oxide", () -> new GasReagent(new ReagentProperties("N2O",0xFFde8f40,2,0,0)));
    public static final RegistryObject<Item> ETHYLENE = registerandlist("ethylene", () -> new GasReagent(new ReagentProperties("C2H4",0xFFcccccc,2,3,3), () -> ChemiFluids.ETHYLENE.getFluid()));
    public static final RegistryObject<Item> CHLOROETHYLENE = registerandlist("chloroethylene", () -> new GasReagent(new ReagentProperties("CH2CHCl",0xFFa7bc60,3,4,3)));
    public static final RegistryObject<Item> AMMONIA = registerandlist("ammonia", () -> new GasReagent(new ReagentProperties("NH3",0xFF83ccd2,3,1,0), () -> ChemiFluids.AMMONIA.getFluid()));
    public static final RegistryObject<Item> COPPER_POWDER = registerandlist("copper_powder", () -> new PowderReagent(new ReagentProperties("Cu",0xFFBB5535,1,0,0), new Item.Properties()));
    public static final RegistryObject<Item> COPPER_OXIDE_REAGENT = registerandlist("copperoxide_reagent", () -> new PowderReagent(new ReagentProperties("CuO",0xFF302833,1,0,0), new Item.Properties()));
    public static final RegistryObject<Item> AGAR = registerandlist("agar", () -> new PowderReagent(new ReagentProperties("H[C12H18O9]nOH",0xFFF0F8FF,0,0,0), new Item.Properties()));
    public static final RegistryObject<Item> SODIUM_NITRITE = registerandlist("sodium_nitrite", () -> new LiquidReagent(new ReagentProperties("NaNO2",0xFFede9b8,3,0,1)));
    public static final RegistryObject<Item> AMMONIUM_HEXACHLOROIRIDATE = registerandlist("ammonium_hexachloroiridate", () -> new PowderReagent(new ReagentProperties("(NH4)2IrCl6",0xFF6b3e1d,3,0,1), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> IRIDIUM_DIOXIDE = registerandlist("iridium_dioxide", () -> new PowderReagent(new ReagentProperties("IrO2",0xFF26120a,3,0,1), new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> PLASTIC_DISH = registerandlist("plastic_dish", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDIUM_PLATE = registerandlist("medium_plate", () -> new BioReagent(new BioProperties("",0x00FFFFFF),64));
    public static final RegistryObject<Item> GROWING_PLATE = registerandlist("growing_plate", () -> new BioGrowingPlate(new BioProperties("",0x00FFFFFF)));
    static{
        listCreativeTab.add(new CreativeTabContent(() -> BioGrowingPlate.getPlate("Crude", 0, false)));
        listCreativeTab.add(new CreativeTabContent(() -> BioGrowingPlate.getPlate("Crude", 0, true)));
        listCreativeTab.add(new CreativeTabContent(() -> BioGrowingPlate.getPlate("Yeast", 0, false)));
        listCreativeTab.add(new CreativeTabContent(() -> BioGrowingPlate.getPlate("Yeast", 0, true)));
        listCreativeTab.add(new CreativeTabContent(() -> BioGrowingPlate.getPlate("Oryzae", 0, false)));
        listCreativeTab.add(new CreativeTabContent(() -> BioGrowingPlate.getPlate("Oryzae", 0, true)));
    }
    public static final RegistryObject<Item> YEAST = registerandlist("yeast", () -> new BioReagent(new BioProperties("yeast",0xfffae58c)));
    public static final RegistryObject<Item> ORYZAE = registerandlist("aspergillus_oryzae", () -> new BioReagent(new BioProperties("Aspergillus oryzae",0xffd6e9ca)));
    public static final RegistryObject<Item> ACETOBACTER = registerandlist("acetobacter", () -> new BioReagent(new BioProperties("acetobacter",0xfffc773f)));
    public static final RegistryObject<Item> CONTAMINATED_PLATE = registerandlist("contaminated_sample", () -> new BioReagent(new BioProperties("Contaminated",0)));
    
    public static final RegistryObject<Item> RAW_LEAD = registerandlist("raw_lead", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_CHALCOPYRITE = registerandlist("raw_chalcopyrite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_BAUXITE = registerandlist("raw_bauxite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_APATITE = registerandlist("raw_apatite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OIL_SAND = registerandlist("oilsand", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_IRIDIUM = registerandlist("raw_iridium", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> POWDER_LEAD = registerandlist("powder_lead", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDER_CHALCOPYRITE = registerandlist("powder_chalcopyrite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDER_BAUXITE = registerandlist("powder_bauxite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDER_APATITE = registerandlist("powder_apatite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDER_GOLD_ORE = registerandlist("powder_gold_ore", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDER_IRON_ORE = registerandlist("powder_iron_ore", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWDER_IRIDIUM = registerandlist("powder_iridium_ore", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    
    public static final RegistryObject<Item> LEAD_INGOT = registerandlist("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REFINED_COPPER_INGOT = registerandlist("refined_copper_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALUMINIUM_INGOT = registerandlist("aluminium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = registerandlist("silver_ingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PLATINUM_INGOT = registerandlist("platinum_ingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> URANIUM_INGOT = registerandlist("uranium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COBALT_INGOT = registerandlist("cobalt_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLUTONIUM_INGOT = registerandlist("plutonium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REINFORCED_INGOT = registerandlist("reinforced_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRIDIUM_INGOT = registerandlist("iridium_ingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> IRON_OXIDE = registerandlist("iron_oxide", () -> new ReagentPowderIndustrial(new ReagentProperties("Fe2O3",0xFF8B0000,1,0,0),new Item.Properties()));
    public static final RegistryObject<Item> COPPER_OXIDE = registerandlist("copper_oxide", () -> new ReagentPowderIndustrial(new ReagentProperties("CuO",0xFF222222,2,0,1),new Item.Properties()));
    public static final RegistryObject<Item> LEAD_OXIDE = registerandlist("lead_oxide", () -> new ReagentPowderIndustrial(new ReagentProperties("PbO",0xFFf9c89b,3,0,0),new Item.Properties()));
    public static final RegistryObject<Item> ALUMINIUM_HYDROXIDE = registerandlist("aluminium_hydroxide", () -> new ReagentPowderIndustrial(new ReagentProperties("Al(OH)3",0xFFEEEEEE,1,0,0),new Item.Properties()));
    public static final RegistryObject<Item> ALUMINA = registerandlist("alumina", () -> new ReagentPowderIndustrial(new ReagentProperties("Al2O3",0xFFFAF0E6,1,0,0),new Item.Properties()));
    public static final RegistryObject<Item> SALT = registerandlist("salt", () -> new ReagentPowderIndustrial(new ReagentProperties("NaCl",0xFFF0F8FF,0,0,0),new Item.Properties()));
    public static final RegistryObject<Item> CALCIUM_SULFATE = registerandlist("calcium_sulfate", () -> new Reagent(new ReagentProperties("CaSO4",0xFFFFFFE0,0,0,0),new Item.Properties()));
    public static final RegistryObject<Item> SULFUR = registerandlist("sulfur", () -> new ReagentPowderIndustrial(new ReagentProperties("S",0xFFF2EA13,2,2,1),new Item.Properties()));
    public static final RegistryObject<Item> POWDER_CRYOLITE = registerandlist("powder_cryolite", () -> new ReagentPowderIndustrial(new ReagentProperties("Na3AlF6",0xFFFFFFE0,0,0,0),new Item.Properties()));
    public static final RegistryObject<Item> ALUMINA_WITH_CRYOLITE = registerandlist("alumina_with_cryolite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> URANIUM_NUGGET = registerandlist("uranium_nugget", () -> new Reagent(new ReagentProperties("U",0,4,0,1),new Item.Properties()));
    public static final RegistryObject<Item> COBALT_NUGGET = registerandlist("cobalt_nugget", () -> new Reagent(new ReagentProperties("Co",0,3,0,2),new Item.Properties()));
    public static final RegistryObject<Item> PLUTONIUM_NUGGET = registerandlist("plutonium_nugget", () -> new Reagent(new ReagentProperties("Pu",0,4,0,1),new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> IRIDIUM_NUGGET = registerandlist("pure_iridium_powder", () -> new Reagent(new ReagentProperties("Ir",0,1,0,1),new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ANODE_SLIME = registerandlist("anode_slime", () -> new Reagent(new ReagentProperties("?",0,1,0,0),new Item.Properties()));
    public static final RegistryObject<Item> SLAG = registerandlist("slag", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALUMINIUM_CATALYST = registerandlist("aluminium_catalyst", () -> new Item(new Item.Properties().durability(256)));
    public static final RegistryObject<Item> COPPER_CATALYST = registerandlist("copper_catalyst", () -> new Item(new Item.Properties().durability(128)));
    public static final RegistryObject<Item> PHOSPHORIC_ACID_CATALYST = registerandlist("phosphoric_acid_catalyst", () -> new Item(new Item.Properties().durability(128)));
    public static final RegistryObject<Item> PLATINUM_CATALYST = registerandlist("platinum_catalyst", () -> new Item(new Item.Properties().durability(5120).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TRACE_SILVER = registerandlist("trace_silver", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_NUGGET = registerandlist("silver_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TRACE_PLATINUM = registerandlist("trace_platinum", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATINUM_NUGGET = registerandlist("platinum_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POLYETHYLENE = registerandlist("polyethylene", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PVC = registerandlist("pvc", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NYLON_PELLET = registerandlist("nylon_pellet", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NYLON_STRING = registerandlist("nylon_string", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_FILTER = registerandlist("copper_filter", () -> new Item(new Item.Properties().durability(40)));
    public static final RegistryObject<Item> SILVER_FILTER = registerandlist("silver_filter", () -> new Item(new Item.Properties().durability(600).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PLATINUM_FILTER = registerandlist("platinum_filter", () -> new Item(new Item.Properties().durability(2400).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> GEOLOGIST_ROD = registerandlist("geologist_rod", () -> new GeologistRod());
    
    public static final RegistryObject<Item> URANIUM_FUEL_ROD = registerandlist("uranium_fuel_rod", () -> new Item(new Item.Properties().stacksTo(1).durability(3600).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DEPLETED_FUEL_ROD = registerandlist("depleted_fuel_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COBALT_CAPSULE = registerandlist("cobalt_capsule", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).durability(256)));
    public static final RegistryObject<Item> PLUTONIUM_PELLET = registerandlist("plutonium_pellet", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SMOKY_QUARTZ = registerandlist("smoky_quartz", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_ELECTRODE = registerandlist("copper_electrode", () -> new Item(new Item.Properties().durability(60)));
    public static final RegistryObject<Item> REFINED_COPPER_ELECTRODE = registerandlist("refined_copper_electrode", () -> new Item(new Item.Properties().durability(256)));
    public static final RegistryObject<Item> CARBON_ELECTRODE = registerandlist("carbon_electrode", () -> new Item(new Item.Properties().durability(256)));
    public static final RegistryObject<Item> GOLD_ELECTRODE = registerandlist("gold_electrode", () -> new Item(new Item.Properties().durability(128)));
    public static final RegistryObject<Item> PLATINUM_ELECTRODE = registerandlist("platinum_electrode", () -> new Item(new Item.Properties().durability(2560).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> ORE_BAG = registerandlist("ore_bag", () -> new OreBag());
    public static final RegistryObject<Item> NYLON_BAG = registerandlist("nylon_bag", () -> new NylonBag());
    public static final RegistryObject<Item> FERTILIZER = registerandlist("fertilizer", () -> new Fertilizer());
    public static final RegistryObject<Item> PHOSPHO_FERTILIZER = registerandlist("phospho_fertilizer", () -> new PhosphoFertilizer());
    public static final RegistryObject<Item> MINEWIPE = registerandlist("minewipe", () -> new MineWipe());
    public static final RegistryObject<Item> SILVER_ARROW = registerandlist("silver_arrow", () -> new SilverArrowItem());
    public static final RegistryObject<Item> SILVER_BOW = registerandlist("silver_bow", () -> new SilverBowItem());
    public static final RegistryObject<Item> LAMP_LINKER = registerandlist("lamp_linker", () -> new LampLinker());
    public static final RegistryObject<Item> ONSEN_DETECTER = registerandlist("geological_surveyor", () -> new OnsenDetecter());
    public static final RegistryObject<Item> UV_LAMP = registerandlist("uv_lamp", () -> new Item(new Item.Properties().durability(512)));
    public static final RegistryObject<Item> ETHANOL_FUEL = registerandlist("ethanol_fuel_pellet", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MISO = registerandlist("miso", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KOJI = registerandlist("koji", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOY_SAUCE = registerandlist("soy_sauce", () -> new LiquidKitchen(new ReagentProperties("seasoning",0xFF431606,0,0,0), new Item.Properties(), () -> ChemiFluids.SOY_SAUCE.getFluid()));
    public static final RegistryObject<Item> COOKING_SAKE = registerandlist("cooking_sake", () -> new LiquidKitchen(new ReagentProperties("seasoning_alchohol",0xFFFFFFE0,2,3,0), new Item.Properties(), () -> ChemiFluids.COOKING_SAKE.getFluid()));
    public static final RegistryObject<Item> MIRIN = registerandlist("mirin", () -> new LiquidKitchen(new ReagentProperties("seasoning_alchohol",0xFFF8E58C,2,3,0), new Item.Properties(), () -> ChemiFluids.MIRIN.getFluid()));
    public static final RegistryObject<Item> WINE = registerandlist("wine", () -> new LiquidKitchen(new ReagentProperties("seasoning_alchohol",0xFF59006b,2,3,0), new Item.Properties(), () -> ChemiFluids.WINE.getFluid()));
    public static final RegistryObject<Item> RICE_VINEGAR = registerandlist("rice_vinegar", () -> new LiquidKitchen(new ReagentProperties("seasoning",0xFFe0bc67,1,0,0), new Item.Properties(), () -> ChemiFluids.RICE_VINEGAR.getFluid()));
    public static final RegistryObject<Item> FRUIT_VINEGAR = registerandlist("fruit_vinegar", () -> new LiquidKitchen(new ReagentProperties("seasoning",0xFFc93264,1,0,0), new Item.Properties(), () -> ChemiFluids.FRUIT_VINEGAR.getFluid()));
    public static final RegistryObject<Item> MILK = registerandlist("milk", () -> new LiquidKitchen(new ReagentProperties("cooking_ingredient",0xFFFFFFFF,0,0,0), new Item.Properties()));
    public static final RegistryObject<Item> GINKGO_NUTS = registerandlist("ginkgo_nuts", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIG = registerandlist("fig", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GOHAN = registerandlist("gohan", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.GOHAN)));
    public static final RegistryObject<Item> ONION_SOUP = registerandlist("onion_soup", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.ONION_SOUP)));
    public static final RegistryObject<Item> BEANS_SOUP = registerandlist("beans_soup", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.BEANS_SOUP)));
    public static final RegistryObject<Item> TOMATO_SOUP = registerandlist("tomato_soup", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.TOMATO_SOUP)));
    public static final RegistryObject<Item> CORN_SOUP = registerandlist("corn_soup", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.CORN_SOUP)));
    public static final RegistryObject<Item> POT_AU_FEU = registerandlist("pot_au_feu", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.POT_AU_FEU)));
    public static final RegistryObject<Item> SEASONED_GOHAN = registerandlist("seasoned_gohan", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.SEASONED_GOHAN)));
    public static final RegistryObject<Item> NIKUJAGA = registerandlist("nikujaga", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.NIKUJAGA)));
    public static final RegistryObject<Item> CHIKUZENNI = registerandlist("chikuzenni", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.CHIKUZENNI)));
    public static final RegistryObject<Item> MISO_SOUP = registerandlist("miso_soup", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.MISO_SOUP)));
    public static final RegistryObject<Item> OYAKODON = registerandlist("oyakodon", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.OYAKODON)));
    public static final RegistryObject<Item> CREAM_STEW = registerandlist("cream_stew", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.CREAM_STEW)));
    public static final RegistryObject<Item> PORK_ONION = registerandlist("pork_onion", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.PORK_ONION)));
    public static final RegistryObject<Item> CHICKEN_SAUTE = registerandlist("chicken_saute", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.CHICKEN_SAUTE)));
    public static final RegistryObject<Item> CHICKEN_POTATO = registerandlist("chicken_saute_with_potato", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.CHICKEN_SAUTE_POTATO)));
    public static final RegistryObject<Item> BEEF_STEW = registerandlist("beef_stew", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.BEEF_STEW)));
    public static final RegistryObject<Item> MEAT_WINE = registerandlist("meat_wine", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.MEAT_WINE)));
    public static final RegistryObject<Item> MEATSAUCE_PASTA = registerandlist("meatsauce_spaghetti", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.MEATSAUCE_PASTA)));
    public static final RegistryObject<Item> MINESTRONE = registerandlist("minestrone", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.MINESTRONE)));
    public static final RegistryObject<Item> SALMON_MARINADE = registerandlist("salmon_marinade", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.SALMON_MARINADE)));
    public static final RegistryObject<Item> NANBANZUKE = registerandlist("nanbanzuke", () -> new StackableBowlFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.NANBANZUKE)));
    public static final RegistryObject<Item> ROASTED_SOYBEANS = registerandlist("roasted_soybeans", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.ROASTED_SOYBEANS)));
    public static final RegistryObject<Item> ROASTED_GINKGO = registerandlist("roasted_ginkgo", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.ROASTED_GINKGO)));
    public static final RegistryObject<Item> COOKED_ONION = registerandlist("cooked_onion", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.COOKED_ONION)));
    public static final RegistryObject<Item> POPCORN = registerandlist("popcorn", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.POPCORN)));
    public static final RegistryObject<Item> CORN_BREAD = registerandlist("corn_bread", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.CORN_BREAD)));
    public static final RegistryObject<Item> RAISIN_BREAD = registerandlist("raisin_bread", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.RAISIN_BREAD)));
    public static final RegistryObject<Item> SUMESHI = registerandlist("sumeshi", () -> new Item(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.SUMESHI)));
    public static final RegistryObject<Item> SUSHI = registerandlist("sushi", () -> new SushiFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.SUSHI)));
    public static final RegistryObject<Item> SUSHI_SALMON = registerandlist("sushi_salmon", () -> new SushiFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.SUSHI_SALMON)));
    public static final RegistryObject<Item> SUSHI_SEABREAM = registerandlist("sushi_seabream", () -> new SushiFoodItem(new Item.Properties().stacksTo(64).food(ChemiFoodProperties.SUSHI_SEABREAM)));
    public static final RegistryObject<Item> SOYSAUCE_BOWL = registerandlist("soysauce_bowl", () -> new Item(new Item.Properties().durability(32)));

    public static final RegistryObject<Item> FLUIDJAR = registerandlist("fluid_jar", () -> new FluidJar());
    public static final RegistryObject<Item> DEBUG_ROD = registerandlist("debug_rod", () -> new MagicalRod());

    public static <T extends Item> RegistryObject<T> registerandlist(String name, Supplier<T> supplier) {
        RegistryObject<T> item = ITEMS.register(name, supplier);
        listItems.add(item);
        listCreativeTab.add(new CreativeTabContent(item));
        return item;
    }

    @SubscribeEvent
    public static void onCreativeTabBuild(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
        }
    }
    public record CreativeTabContent(Optional<Supplier<ItemStack>> stack, Optional<Supplier<ItemLike>> supplier) {
        public CreativeTabContent(RegistryObject<? extends Item> obj){
            this(Optional.empty(), Optional.of(() -> obj.get()));
        }
        public CreativeTabContent(Supplier<ItemStack> stacksupplier){
            this(Optional.of(stacksupplier), Optional.empty());
        }
    }
}
