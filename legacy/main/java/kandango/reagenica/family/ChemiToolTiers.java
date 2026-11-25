package kandango.reagenica.family;

import kandango.reagenica.ChemiItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ChemiToolTiers {
  public static final Tier PLATINUM = new ForgeTier(4, 1600, 10.0f, 4.0f, 12, 
    BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ChemiItems.PLATINUM_INGOT.get()));
  public static final Tier IRIDIUM = new ForgeTier(4, 4800, 16.0f, 5.0f, 15, 
    BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ChemiItems.IRIDIUM_INGOT.get()));
  public static final Tier PINK_GOLD = new ForgeTier(2, 180, 10.0f, 3.5f, 25, 
    BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(ChemiItems.PINK_GOLD_INGOT.get()));
}
