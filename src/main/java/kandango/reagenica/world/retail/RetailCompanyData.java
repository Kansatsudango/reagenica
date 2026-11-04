package kandango.reagenica.world.retail;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class RetailCompanyData {
  private final int MAX_SAVED_TRANSACTIONS = 1000;
  private final Runnable update_callback;
  private long asset;
  private final UUID owner;
  private final Queue<Transaction> recentTransactions = new ArrayDeque<>();

  public RetailCompanyData(UUID owner, Runnable callback){
    this.update_callback = callback;
    this.owner = owner;
  }

  public long getFortune(){
    return asset;
  }
  public void addFortune(long amount){
    this.asset += amount;
    update_callback.run();
  }
  public void putTransaction(Transaction transaction){
    recentTransactions.offer(transaction);
    while (recentTransactions.size()>MAX_SAVED_TRANSACTIONS) {
      recentTransactions.poll();
    }
    update_callback.run();
  }

  public String head(int count){
    StringBuilder sb = new StringBuilder("Transactions:");
    int i=0;
    for(Transaction transaction : recentTransactions){
      i++;
      if(i>count)break;
      sb.append(transaction.toString());
    }
    return sb.toString();
  }

  CompoundTag encodeAsCompoundTag(){
    CompoundTag tag = new CompoundTag();
    tag.putLong("Asset", asset);
    tag.putUUID("Owner", owner);
    ListTag list = new ListTag();
    for(Transaction transaction : recentTransactions){
      list.add(transaction.toCompoundTag());
    }
    tag.put("Transactions", list);
    return tag;
  }

  public static RetailCompanyData loadFromCompoundTag(CompoundTag tag, Runnable callback){
    UUID owner = tag.getUUID("Owner");
    RetailCompanyData data = new RetailCompanyData(owner, callback);
    data.asset = tag.getLong("Asset");
    ListTag list = tag.getList("Transactions", Tag.TAG_COMPOUND);
    for(int i=0; i<list.size(); i++){
      CompoundTag entry = list.getCompound(i);
      Transaction transaction = Transaction.fromCompoundTag(entry);
      data.recentTransactions.offer(transaction);
    }
    return data;
  }
}
