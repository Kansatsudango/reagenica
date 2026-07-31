package kandango.reagenica.item;

import net.minecraft.world.item.ItemStack;

public interface IBagItem {
  public boolean canAutoStock();
  public boolean isValidItem(ItemStack item);
}