package kandango.reagenica.generator;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.blockfamily.WoodFamily;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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

}
