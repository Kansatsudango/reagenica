package kandango.reagenica.generator;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.family.ArmorFamily;
import kandango.reagenica.family.CrystalFamily;
import kandango.reagenica.family.StoneFamily;
import kandango.reagenica.family.ToolFamily;
import kandango.reagenica.family.WoodFamily;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiRecipeProvider extends RecipeProvider{
  public ChemiRecipeProvider(PackOutput output){
    super(output);
  }
  @Override
  protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
    WoodFamily.Woods.forEach(family -> WoodFamilyRecipeGenerator.of(family).register(consumer));
    CrystalFamily.Crystals.forEach(family -> CrystalRecipeGenerator.of(family).register(consumer));
    StoneFamily.Stones.forEach(family -> StoneFamilyRecipeGenerator.of(family).register(consumer));
    ArmorRecipeGenerator.ofPair(ChemiItems.PLATINUM_ARMOR, ChemiItems.PLATINUM_INGOT.get()).register(consumer);
    ArmorSmithingRecipeGenerator.smith(ChemiItems.PLATINUM_ARMOR, ChemiItems.IRIDIUM_ARMOR, ChemiItems.IRIDIUM_INGOT.get(), ChemiItems.IRIDIUM_UPGRADE_STH.get()).register(consumer);
    ArmorRecipeGenerator.ofPair(ChemiItems.PINK_GOLD_ARMOR, ChemiItems.PINK_GOLD_INGOT.get()).register(consumer);
    ToolRecipeGenerator.ofPair(ChemiItems.PLATINUM_TOOLS, ChemiItems.PLATINUM_INGOT.get(), Ingredient.of(Items.DIAMOND)).register(consumer);
    ToolSmithingRecipeGenerator.smith(ChemiItems.PLATINUM_TOOLS, ChemiItems.IRIDIUM_TOOLS, ChemiItems.IRIDIUM_INGOT.get(), ChemiItems.IRIDIUM_UPGRADE_STH.get()).register(consumer);
    ToolRecipeGenerator.ofPair(ChemiItems.PINK_GOLD_TOOLS, ChemiItems.PINK_GOLD_INGOT.get(), Ingredient.of(Items.STICK)).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.RAW_CHALCOPYRITE, ChemiBlocks.CHALCOPYRITE_BLOCK, true).register(consumer);;
    CompressingRecipeGenerator.of(ChemiItems.RAW_BAUXITE, ChemiBlocks.BAUXITE_BLOCK, true).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.RAW_APATITE, ChemiBlocks.APATITE_BLOCK, true).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.OIL_SAND, ChemiBlocks.OILSAND_BLOCK, true).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.RAW_LEAD, ChemiBlocks.RAW_LEAD_BLOCK, true).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.TRACE_PLATINUM, ChemiItems.PLATINUM_NUGGET, false, "", "_primal").register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.PLATINUM_NUGGET, ChemiItems.PLATINUM_INGOT, false, "", "_middle").register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.TRACE_SILVER, ChemiItems.SILVER_NUGGET, false, "", "_primal").register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.SILVER_NUGGET, ChemiItems.SILVER_INGOT, false, "", "_middle").register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.URANIUM_NUGGET, ChemiItems.URANIUM_INGOT, false).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.PLUTONIUM_NUGGET, ChemiItems.PLUTONIUM_INGOT, false).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.COBALT_NUGGET, ChemiItems.COBALT_INGOT, false).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.LEAD_INGOT, ChemiBlocks.LEAD_BLOCK, true).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.ALUMINIUM_INGOT, ChemiBlocks.ALUMINIUM_BLOCK, true).register(consumer);
    CompressingRecipeGenerator.of(ChemiItems.REFINED_COPPER_INGOT, ChemiBlocks.REFINED_COPPER_BLOCK, true).register(consumer);
  }
  private static class WoodFamilyRecipeGenerator {
    private final WoodFamily woodFamily;
    private final TagKey<Item> logTag;
    private WoodFamilyRecipeGenerator(WoodFamily woodFamily){
      this.woodFamily = woodFamily;
      this.logTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, woodFamily.name+"_logs"));
    }
    public static WoodFamilyRecipeGenerator of(WoodFamily family){
      return new WoodFamilyRecipeGenerator(family);
    }
    public void register(Consumer<FinishedRecipe> consumer){
      wood(consumer);
      strippedwood(consumer);
      planks(consumer);
      stairs(consumer);
      slab(consumer);
      sign(consumer);
      hangingsign(consumer);
      fence(consumer);
      fencegate(consumer);
      trapdoor(consumer);
      door(consumer);
      button(consumer);
      pressureplate(consumer);
    }
    private void wood(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.WOOD.get(), 3)
            .pattern("LL")
            .pattern("LL")
            .define('L', woodFamily.LOG_ITEM.get())
            .unlockedBy("has_log", has(woodFamily.LOG.get()))
            .save(consumer);
    }
    private void strippedwood(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.STRIPPED_WOOD.get(), 3)
            .pattern("LL")
            .pattern("LL")
            .define('L', woodFamily.STRIPPED_LOG_ITEM.get())
            .unlockedBy("has_stripped_log", has(woodFamily.STRIPPED_LOG.get()))
            .save(consumer);
    }
    private void planks(Consumer<FinishedRecipe> consumer){
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, woodFamily.PLANKS.get(), 4)
            .requires(logTag)
            .unlockedBy("has_planks", has(logTag))
            .save(consumer);
    }
    private void stairs(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.STAIRS_ITEM.get(), 4)
            .pattern("P  ")
            .pattern("PP ")
            .pattern("PPP")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void slab(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.SLAB_ITEM.get(), 6)
            .pattern("PPP")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void sign(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.SIGN_ITEM.get(), 3)
            .pattern("PPP")
            .pattern("PPP")
            .pattern(" S ")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .define('S', Items.STICK)
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void hangingsign(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.HANGING_SIGN_ITEM.get(), 6)
            .pattern("C C")
            .pattern("PPP")
            .pattern("PPP")
            .define('P', woodFamily.STRIPPED_LOG_ITEM.get())
            .define('C', Items.CHAIN)
            .unlockedBy("has_stripped_log", has(woodFamily.STRIPPED_LOG.get()))
            .save(consumer);
    }
    private void fence(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.FENCE_ITEM.get(), 3)
            .pattern("PSP")
            .pattern("PSP")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .define('S', Items.STICK)
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void fencegate(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.FENCE_GATE_ITEM.get(), 1)
            .pattern("SPS")
            .pattern("SPS")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .define('S', Items.STICK)
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void trapdoor(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.TRAPDOOR_ITEM.get(), 2)
            .pattern("PPP")
            .pattern("PPP")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void door(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.DOOR_ITEM.get(), 3)
            .pattern("PP")
            .pattern("PP")
            .pattern("PP")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void button(Consumer<FinishedRecipe> consumer){
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, woodFamily.BUTTON_ITEM.get(), 1)
            .requires(woodFamily.PLANKS_ITEM.get())
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
    private void pressureplate(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodFamily.PRESSURE_PLATE_ITEM.get(), 1)
            .pattern("PP")
            .define('P', woodFamily.PLANKS_ITEM.get())
            .unlockedBy("has_planks", has(woodFamily.PLANKS_ITEM.get()))
            .save(consumer);
    }
  }
  private static class StoneFamilyRecipeGenerator {
    private final StoneFamily stoneFamily;
    private StoneFamilyRecipeGenerator(StoneFamily family){
      this.stoneFamily = family;
    }
    public static StoneFamilyRecipeGenerator of(StoneFamily family){
      return new StoneFamilyRecipeGenerator(family);
    }
    public void register(Consumer<FinishedRecipe> consumer){
      stairs(consumer);
      slab(consumer);
      p_stairs(consumer);
      p_slab(consumer);
      polish(consumer);
      wall(consumer);
      s_polish(consumer);
      s_stairs(consumer);
      s_slabs(consumer);
      s_p_stairs(consumer);
      s_p_slabs(consumer);
    }
    private void stairs(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stoneFamily.STAIRS_ITEM.get(), 4)
            .pattern("P  ")
            .pattern("PP ")
            .pattern("PPP")
            .define('P', stoneFamily.STONE.get())
            .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
            .save(consumer);
    }
    private void slab(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stoneFamily.SLAB_ITEM.get(), 6)
            .pattern("PPP")
            .define('P', stoneFamily.STONE.get())
            .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
            .save(consumer);
    }
    private void p_stairs(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stoneFamily.P_STAIRS_ITEM.get(), 4)
            .pattern("P  ")
            .pattern("PP ")
            .pattern("PPP")
            .define('P', stoneFamily.P_STONE.get())
            .unlockedBy("has_stone", has(stoneFamily.P_STONE_ITEM.get()))
            .save(consumer);
    }
    private void p_slab(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stoneFamily.P_SLAB_ITEM.get(), 6)
            .pattern("PPP")
            .define('P', stoneFamily.P_STONE.get())
            .unlockedBy("has_stone", has(stoneFamily.P_STONE_ITEM.get()))
            .save(consumer);
    }
    private void polish(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stoneFamily.P_STONE.get(), 4)
            .pattern("PP")
            .pattern("PP")
            .define('P', stoneFamily.STONE.get())
            .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
            .save(consumer);
    }
    private void wall(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stoneFamily.WALL.get(), 6)
            .pattern("PPP")
            .pattern("PPP")
            .define('P', stoneFamily.STONE.get())
            .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
            .save(consumer);
    }
    private void s_polish(Consumer<FinishedRecipe> consumer){
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(stoneFamily.STONE_ITEM.get()),
      RecipeCategory.BUILDING_BLOCKS, 
      stoneFamily.P_STONE.get(),1)
      .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
      .save(consumer, ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, "sc_polished_"+stoneFamily.name));
    }
    private void s_stairs(Consumer<FinishedRecipe> consumer){
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(stoneFamily.STONE_ITEM.get()),
      RecipeCategory.BUILDING_BLOCKS, 
      stoneFamily.STAIRS.get(),1)
      .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
      .save(consumer, concat(ForgeRegistries.ITEMS.getKey(stoneFamily.STAIRS.get().asItem()),"sc_",""));
    }
    private void s_slabs(Consumer<FinishedRecipe> consumer){
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(stoneFamily.STONE_ITEM.get()),
      RecipeCategory.BUILDING_BLOCKS, 
      stoneFamily.SLAB.get(),2)
      .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
      .save(consumer, concat(ForgeRegistries.ITEMS.getKey(stoneFamily.SLAB.get().asItem()),"sc_",""));
    }
    private void s_p_stairs(Consumer<FinishedRecipe> consumer){
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(stoneFamily.STONE.get(), stoneFamily.P_STONE.get()),
      RecipeCategory.BUILDING_BLOCKS, 
      stoneFamily.P_STAIRS.get(),1)
      .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
      .save(consumer, concat(ForgeRegistries.ITEMS.getKey(stoneFamily.P_STAIRS.get().asItem()),"sc_",""));
    }
    private void s_p_slabs(Consumer<FinishedRecipe> consumer){
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(stoneFamily.STONE.get(), stoneFamily.P_STONE.get()),
      RecipeCategory.BUILDING_BLOCKS, 
      stoneFamily.P_SLAB.get(),2)
      .unlockedBy("has_stone", has(stoneFamily.STONE_ITEM.get()))
      .save(consumer, concat(ForgeRegistries.ITEMS.getKey(stoneFamily.P_SLAB.get().asItem()),"sc_",""));
    }
  }
  private static class ArmorRecipeGenerator{
    private final Item ingredient;
    private final ArmorFamily family;

    private ArmorRecipeGenerator(ArmorFamily family, Item ingredient){
      this.family = family;
      this.ingredient = ingredient;
    }
    public static ArmorRecipeGenerator ofPair(ArmorFamily family, Item ing){
      return new ArmorRecipeGenerator(family, ing);
    }
    public void register(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, family.HELMET.get(), 1)
        .pattern("###")
        .pattern("# #")
        .define('#', ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, family.CHESTPLATE.get(), 1)
        .pattern("# #")
        .pattern("###")
        .pattern("###")
        .define('#', ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, family.LEGGINGS.get(), 1)
        .pattern("###")
        .pattern("# #")
        .pattern("# #")
        .define('#', ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, family.BOOTS.get(), 1)
        .pattern("# #")
        .pattern("# #")
        .define('#', ingredient)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
    }
  }
  private static class ToolRecipeGenerator{
    private final Item ingredient;
    private final Ingredient subIngredient;
    private final ToolFamily family;

    private ToolRecipeGenerator(ToolFamily family, Item ingredient, Ingredient sub){
      this.family = family;
      this.ingredient = ingredient;
      this.subIngredient = sub;
    }
    public static ToolRecipeGenerator ofPair(ToolFamily family, Item ing, Ingredient sub){
      return new ToolRecipeGenerator(family, ing, sub);
    }
    public void register(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, family.SWORD.get(), 1)
        .pattern("#")
        .pattern("#")
        .pattern("+")
        .define('#', ingredient)
        .define('+', subIngredient)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, family.PICKAXE.get(), 1)
        .pattern("###")
        .pattern(" + ")
        .pattern(" / ")
        .define('#', ingredient)
        .define('+', subIngredient)
        .define('/', Items.STICK)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, family.AXE.get(), 1)
        .pattern("##")
        .pattern("#+")
        .pattern(" /")
        .define('#', ingredient)
        .define('+', subIngredient)
        .define('/', Items.STICK)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, family.SHOVEL.get(), 1)
        .pattern("#")
        .pattern("+")
        .pattern("/")
        .define('#', ingredient)
        .define('+', subIngredient)
        .define('/', Items.STICK)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, family.HOE.get(), 1)
        .pattern("##")
        .pattern(" +")
        .pattern(" /")
        .define('#', ingredient)
        .define('+', subIngredient)
        .define('/', Items.STICK)
        .unlockedBy("has_material", has(ingredient))
        .save(consumer);
    }
  }
  private static class ArmorSmithingRecipeGenerator{
    private final Ingredient template;
    private final Item material;
    private final ArmorFamily baseFamily;
    private final ArmorFamily upgradedFamily;
    private ArmorSmithingRecipeGenerator(ArmorFamily base, ArmorFamily upgraded, Item material, Ingredient template){
      this.template = template;
      this.material = material;
      this.baseFamily = base;
      this.upgradedFamily = upgraded;
    }
    public static ArmorSmithingRecipeGenerator smith(ArmorFamily base, ArmorFamily upgraded, Item material, ItemLike template){
      return new ArmorSmithingRecipeGenerator(base, upgraded, material, Ingredient.of(template));
    }
    public void register(Consumer<FinishedRecipe> consumer){
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.HELMET.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.HELMET.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.HELMET.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.CHESTPLATE.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.CHESTPLATE.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.CHESTPLATE.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.LEGGINGS.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.LEGGINGS.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.LEGGINGS.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.BOOTS.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.BOOTS.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.BOOTS.getId());
    }
  }
  private static class ToolSmithingRecipeGenerator{
    private final Ingredient template;
    private final Item material;
    private final ToolFamily baseFamily;
    private final ToolFamily upgradedFamily;
    private ToolSmithingRecipeGenerator(ToolFamily base, ToolFamily upgraded, Item material, Ingredient template){
      this.template = template;
      this.material = material;
      this.baseFamily = base;
      this.upgradedFamily = upgraded;
    }
    public static ToolSmithingRecipeGenerator smith(ToolFamily base, ToolFamily upgraded, Item material, ItemLike template){
      return new ToolSmithingRecipeGenerator(base, upgraded, material, Ingredient.of(template));
    }
    public void register(Consumer<FinishedRecipe> consumer){
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.SWORD.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.SWORD.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.SWORD.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.PICKAXE.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.PICKAXE.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.PICKAXE.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.AXE.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.AXE.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.AXE.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.SHOVEL.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.SHOVEL.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.SHOVEL.getId());
      SmithingTransformRecipeBuilder.smithing(template, Ingredient.of(baseFamily.HOE.get()),
                Ingredient.of(material), RecipeCategory.COMBAT, upgradedFamily.HOE.get())
                .unlocks("has_material", has(material)).save(consumer, upgradedFamily.HOE.getId());
    }
  }
  private static class CrystalRecipeGenerator{
    private final CrystalFamily family;

    private CrystalRecipeGenerator(CrystalFamily family){
      this.family = family;
    }
    public static CrystalRecipeGenerator of(CrystalFamily family){
      return new CrystalRecipeGenerator(family);
    }
    public void register(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, family.BLOCK_ITEM.get(), 1)
        .pattern("##")
        .pattern("##")
        .define('#', family.SHARD_ITEM.get())
        .unlockedBy("has_material", has(family.SHARD_ITEM.get()))
        .save(consumer);
    }
  }
  private static class CompressingRecipeGenerator{
    private final DeferredHolder<? extends ItemLike> UNZIPPED;
    private final DeferredHolder<? extends ItemLike> COMPRESSED;
    private final boolean isCompressedFormBlock;
    private final String prefix;
    private final String suffix;

    private CompressingRecipeGenerator(DeferredHolder<? extends ItemLike> unzipped, DeferredHolder<? extends ItemLike> compressed, boolean isblock, String prefix, String suffix){
      this.UNZIPPED = unzipped;
      this.COMPRESSED = compressed;
      this.isCompressedFormBlock = isblock;
      this.prefix = prefix;
      this.suffix = suffix;
    }
    public static CompressingRecipeGenerator of(DeferredHolder<? extends ItemLike> unzipped, DeferredHolder<? extends ItemLike> compressed, boolean isBlock){
      return new CompressingRecipeGenerator(unzipped, compressed, isBlock, "", "");
    }
    public static CompressingRecipeGenerator of(DeferredHolder<? extends ItemLike> unzipped, DeferredHolder<? extends ItemLike> compressed, boolean isBlock, String prefix, String suffix){
      return new CompressingRecipeGenerator(unzipped, compressed, isBlock, prefix, suffix);
    }
    public void register(Consumer<FinishedRecipe> consumer){
      ShapedRecipeBuilder.shaped(isCompressedFormBlock?RecipeCategory.BUILDING_BLOCKS:RecipeCategory.MISC, COMPRESSED.get(), 1)
        .pattern("###")
        .pattern("###")
        .pattern("###")
        .define('#', UNZIPPED.get())
        .unlockedBy("has_material", has(UNZIPPED.get()))
        .save(consumer, concat(ForgeRegistries.ITEMS.getKey(UNZIPPED.get().asItem()),prefix,suffix));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UNZIPPED.get(), 9)
        .requires(COMPRESSED.get())
        .unlockedBy("has_block", has(COMPRESSED.get()))
        .save(consumer, concat(ForgeRegistries.ITEMS.getKey(COMPRESSED.get().asItem()),prefix,suffix));
    }
  }

  private static ResourceLocation concat(ResourceLocation base, String prefix, String suffix){
    String basePath = base.getPath();
    String baseNamespace = base.getNamespace();
    return ResourceLocation.fromNamespaceAndPath(baseNamespace, prefix+basePath+suffix);
  }
}
