package kandango.reagenica.world.retail;

import java.util.function.ToIntFunction;

import kandango.reagenica.world.retail.customer.MerchandiseProperty;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record SingleItem(ItemStack stack, int price, int cost) {
  public CompoundTag toCompoundTag(){
    CompoundTag tag = new CompoundTag();
    CompoundTag itemTag = new CompoundTag();
    stack.save(itemTag);
    tag.put("Item", itemTag);
    tag.putInt("Price", price);
    tag.putInt("Cost", cost);
    return tag;
  }
  public static SingleItem fromCompoundTag(CompoundTag tag){
    ItemStack stack = ItemStack.of(tag.getCompound("Item"));
    int price = tag.getInt("Price");
    int cost = tag.getInt("Cost");
    return new SingleItem(stack, price, cost);
  }

  public static SingleItem of(MerchandiseProperty mp, ToIntFunction<SKU> baseCostMapper){
    SKU sku = mp.sku();
    int price = mp.price();
    int cost = baseCostMapper.applyAsInt(sku);
    return new SingleItem(mp.stack() , price, cost);
  }
}