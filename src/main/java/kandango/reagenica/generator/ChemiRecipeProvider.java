package kandango.reagenica.generator;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.family.ArmorFamily;
import kandango.reagenica.family.ToolFamily;
import kandango.reagenica.family.WoodFamily;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ChemiRecipeProvider extends RecipeProvider{
  public ChemiRecipeProvider(PackOutput output){
    super(output);
  }
  @Override
  protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
    WoodFamilyRecipeGenerator.of(ChemiBlocks.METASEQUOIA).register(consumer);
    WoodFamilyRecipeGenerator.of(ChemiBlocks.TAXODIUM).register(consumer);
    WoodFamilyRecipeGenerator.of(ChemiBlocks.GINKGO).register(consumer);
    WoodFamilyRecipeGenerator.of(ChemiBlocks.MAGNOLIA).register(consumer);
    WoodFamilyRecipeGenerator.of(ChemiBlocks.FICUS).register(consumer);
    ArmorRecipeGenerator.ofPair(ChemiItems.PLATINUM_ARMOR, ChemiItems.PLATINUM_INGOT.get()).register(consumer);
    ArmorSmithingRecipeGenerator.smith(ChemiItems.PLATINUM_ARMOR, ChemiItems.IRIDIUM_ARMOR, ChemiItems.IRIDIUM_INGOT.get(), ChemiItems.IRIDIUM_UPGRADE_STH.get()).register(consumer);
    ToolRecipeGenerator.ofPair(ChemiItems.PLATINUM_TOOLS, ChemiItems.PLATINUM_INGOT.get(), Ingredient.of(Items.DIAMOND)).register(consumer);
    ToolSmithingRecipeGenerator.smith(ChemiItems.PLATINUM_TOOLS, ChemiItems.IRIDIUM_TOOLS, ChemiItems.IRIDIUM_INGOT.get(), ChemiItems.IRIDIUM_UPGRADE_STH.get()).register(consumer);
  }
  private static class WoodFamilyRecipeGenerator {
    private final WoodFamily woodFamily;
    private final TagKey<Item> logTag;
    private WoodFamilyRecipeGenerator(WoodFamily woodFamily){
      this.woodFamily = woodFamily;
      this.logTag = TagKey.create(Registries.ITEM, new ResourceLocation(ChemistryMod.MODID, woodFamily.name+"_logs"));
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
}
