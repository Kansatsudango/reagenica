package kandango.reagenica.world.retail;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class Transaction {
  private final long time;
  private final String shopName;
  private final List<SingleItem> items;

  public Transaction(String shopName, long time, List<SingleItem> items){
    this.time = time;
    this.shopName = shopName;
    this.items = List.copyOf(items);
  };

  public long getTime(){
    return time;
  }
  public String getShopName(){
    return shopName;
  }
  public List<SingleItem> getItems(){
    return items;
  }

  public CompoundTag toCompoundTag(){
    CompoundTag tag = new CompoundTag();
    ListTag list = new ListTag();
    tag.putString("ShopName", shopName);
    tag.putLong("Time", time);
    items.forEach(item -> list.add(item.toCompoundTag()));
    tag.put("Items", list);
    return tag;
  }
  public static Transaction fromCompoundTag(CompoundTag tag){
    long time = tag.getLong("Time");
    String name = tag.getString("ShopName");
    ListTag transactionsTag = tag.getList("Items", Tag.TAG_COMPOUND);
    List<SingleItem> itemsList = new ArrayList<>();
    for(int i=0; i<transactionsTag.size(); i++){
      CompoundTag entry = transactionsTag.getCompound(i);
      SingleItem single = SingleItem.fromCompoundTag(entry);
      itemsList.add(single);
    }
    return new Transaction(name, time, itemsList);
  }

  @Override 
  public String toString(){
    StringBuilder builder = new StringBuilder("[Shop:"+this.shopName+", Time:"+this.time+", Items:");
    for(SingleItem item : this.items){
      builder.append("{Item:{");
      builder.append(item.stack.toString());
      builder.append("}, Price:");
      builder.append(item.price);
      builder.append(", Cost:");
      builder.append(item.cost);
      builder.append("},");
    }
    builder.append("]");
    return builder.toString();
  }

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
  }
}
