package kandango.reagenica;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ChemiTags {
  public static class Blocks {
    public static final TagKey<Block> STONE_ORE_REPLACEABLES =
      TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "stone_ore_replaceables"));

    public static final TagKey<Block> DEEPSLATE_ORE_REPLACEABLES =
      TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "deepslate_ore_replaceables"));
      
    public static final TagKey<Block> TANK_BLOCKS =
      TagKey.create(Registries.BLOCK, new ResourceLocation("reagenica", "tank_block"));
    public static final TagKey<Block> CAVE_REPLACEABLE =
      TagKey.create(Registries.BLOCK, new ResourceLocation("reagenica", "cave_patch_replaceable"));
  }
  public static class Items {
    public static final TagKey<Item> ORE_BAG_ACCEPT =
      TagKey.create(Registries.ITEM, new ResourceLocation("reagenica", "orebag_accept"));
    public static final TagKey<Item> ELECTRODES =
      TagKey.create(Registries.ITEM, new ResourceLocation("reagenica", "electrodes"));
    public static final TagKey<Item> STRONG_ACIDS =
      TagKey.create(Registries.ITEM, new ResourceLocation("reagenica", "strong_acids"));
    public static final TagKey<Item> NYLON_BAGS =
      TagKey.create(Registries.ITEM, new ResourceLocation("reagenica", "nylon_bags"));
  }
}
