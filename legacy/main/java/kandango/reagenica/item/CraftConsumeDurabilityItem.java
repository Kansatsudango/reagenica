package kandango.reagenica.item;

import kandango.reagenica.block.entity.util.ItemStackUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftConsumeDurabilityItem extends Item{
  private final ItemStack onBreak;
  public CraftConsumeDurabilityItem(Item.Properties props, ItemStack onBreak) {
    super(props);
    this.onBreak = onBreak;
  }

  @Override
  public boolean hasCraftingRemainingItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack stack) {
    return ItemStackUtil.getDamagedItem(stack, 1, () -> this.onBreak.copy());
  }
}
