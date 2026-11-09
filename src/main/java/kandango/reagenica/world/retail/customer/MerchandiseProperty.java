package kandango.reagenica.world.retail.customer;

import kandango.reagenica.world.retail.SKU;
import net.minecraft.world.item.ItemStack;

public record MerchandiseProperty(ItemStack stack, SKU sku, int price) {
  
}
