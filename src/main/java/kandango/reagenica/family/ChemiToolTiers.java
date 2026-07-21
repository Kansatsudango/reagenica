package kandango.reagenica.family;

import java.util.function.Supplier;

import kandango.reagenica.ChemiItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ChemiToolTiers {
  public static final Tier PLATINUM = new ChemiTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 
    1600, 10.0f, 4.0f, 12, 
    () -> Ingredient.of(ChemiItems.PLATINUM_INGOT.get()));
  public static final Tier IRIDIUM = new ChemiTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
    4800, 16.0f, 5.0f, 15, 
    () -> Ingredient.of(ChemiItems.IRIDIUM_INGOT.get()));
  public static final Tier PINK_GOLD = new ChemiTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 
    180, 10.0f, 3.5f, 25, 
    () -> Ingredient.of(ChemiItems.PINK_GOLD_INGOT.get()));

  public record ChemiTier(TagKey<Block> incorrectBlocksForDrops, int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient) implements Tier {
    @Override
    public int getUses() {
      return uses;
    }

    @Override
    public float getSpeed() {
      return speed;
    }

    @Override
    public float getAttackDamageBonus() {
      return attackDamageBonus;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
      return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
      return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
      return repairIngredient.get();
    }
}
}
