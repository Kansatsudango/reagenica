package kandango.reagenica.item;

import kandango.reagenica.ChemistryMod;
import net.minecraft.world.item.ItemStack;

public interface IBagItem {
  public boolean canAutoStock();
  public boolean isValidItem(ItemStack item);
  public void save(ItemStack bag);
  default public void markDirty(ItemStack bag){
    ChemistryMod.LOGGER.debug("Bag Marked as Dirty.");
    save(bag);
  }
}