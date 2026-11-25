package kandango.reagenica.item;

import kandango.reagenica.block.entity.util.ItemStackUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class FilletKnifeItem extends SwordItem{

  public FilletKnifeItem(Tier tier, int atk, float spd, Properties props) {
    super(tier, atk, spd, props);
  }

  @Override
  public boolean hasCraftingRemainingItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack stack) {
    return ItemStackUtil.getDamagedItem(stack, 1, () -> ItemStack.EMPTY);
  }
  
}
