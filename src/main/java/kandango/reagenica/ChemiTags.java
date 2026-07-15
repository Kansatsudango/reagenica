package kandango.reagenica;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ChemiTags {
  public static class Blocks {
    public static final TagKey<Block> STONE_ORE_REPLACEABLES =
      TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("minecraft", "stone_ore_replaceables"));

    public static final TagKey<Block> DEEPSLATE_ORE_REPLACEABLES =
      TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("minecraft", "deepslate_ore_replaceables"));
      
    public static final TagKey<Block> TANK_BLOCKS =
      TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("reagenica", "tank_block"));
    public static final TagKey<Block> CAVE_REPLACEABLE =
      TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("reagenica", "cave_patch_replaceable"));
    public static final TagKey<Block> LARGE_MUSHROOMS =
      TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("reagenica", "large_mushrooms"));
  }
  public static class Items {
    public static final TagKey<Item> ORE_BAG_ACCEPT =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "orebag_accept"));
    public static final TagKey<Item> CRYSTAL_SHARDS =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "crystal_shards"));
    public static final TagKey<Item> BAGS_DENY =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "bags_deny"));
    public static final TagKey<Item> ELECTRODES =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "electrodes"));
    public static final TagKey<Item> STRONG_ACIDS =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "strong_acids"));
    public static final TagKey<Item> NYLON_BAGS =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "nylon_bags"));
    public static final TagKey<Item> PLATES =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "plates"));
    public static final TagKey<Item> OFFERABLES =
      TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("reagenica", "offerable"));
  }
  public static class Structures {
    public static final TagKey<Structure> CRATER_TARGET = 
      TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath("reagenica", "crater_map_targets"));
  }
}
