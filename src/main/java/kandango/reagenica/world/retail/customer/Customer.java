package kandango.reagenica.world.retail.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;
import kandango.reagenica.world.retail.MerchandiseCategory;
import kandango.reagenica.world.retail.SKU;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public class Customer {
  private static final Random rand = new Random();
  private final float AlternativeRate;
  private final float InstinctPurchaseRate;
  private static final int INSTINCT_PUIRCHASE_MAX = 5;
  private final List<SKU> shoppingList;

  private  Customer(List<SKU> shoppingList, float alt, float ins){
    this.shoppingList = List.copyOf(shoppingList);
    this.AlternativeRate = alt;
    this.InstinctPurchaseRate = ins;
  }

  public List<MerchandiseProperty> buyFrom(IItemHandler handler, ToIntFunction<SKU> priceProvider, Level lv){
    final List<MerchandiseProperty> shoppingBag = new ArrayList<>();
    int maxSlot = handler.getSlots();
    if(maxSlot==0)return List.of();
    purposeShopping: for(final SKU demand : shoppingList){
      final int demandItemPrice = priceProvider.applyAsInt(demand);
      for(int i=0; i<handler.getSlots(); i++){
        ItemStack stackInSlot = handler.getStackInSlot(i);
        if(demand.contains(stackInSlot)){
          ItemStack stack = handler.extractItem(i, 1, false);
          shoppingBag.add(new MerchandiseProperty(stack, demand, demandItemPrice));
          continue purposeShopping;
        }
      }

      // recalc because handler is mutable
      List<SKU> alternatives = getItemsAsList(handler).stream()
                                                .map(stack -> SKU.lookup(lv, stack)).filter(SKU::isPresent)
                                                .distinct().filter(demand::isSameCategory).toList();
      final int size = alternatives.size();
      alternative: for(int i=0; i<2; i++){
        if(size==0) break alternative;
        final SKU merchandiseInHand = alternatives.get(rand.nextInt(size));
        final int itemInHandPrice = priceProvider.applyAsInt(merchandiseInHand);
        float chance = purchaseChance(demandItemPrice, itemInHandPrice);
        if(rand.nextFloat() < chance){
          for(int j=0; j<handler.getSlots(); j++){
            ItemStack stack = handler.getStackInSlot(j);
            if(merchandiseInHand.contains(stack)){
              handler.extractItem(j, 1, false);
              shoppingBag.add(new MerchandiseProperty(stack, demand, itemInHandPrice));
              break alternative;
            }
          }
        }
      }
    }

    instinct: for(int i=0; i<INSTINCT_PUIRCHASE_MAX; i++){
      if(rand.nextFloat() < InstinctPurchaseRate){
        ItemStack stack = ItemStack.EMPTY;
        int selectedSlot=0;
        for(int j=0;j<10;j++){
          selectedSlot = rand.nextInt(maxSlot);
          stack = handler.extractItem(selectedSlot, 1, true);
          if(!stack.isEmpty())break;
        }
        final SKU merchandiseInHand = SKU.lookup(lv, stack);
        if(merchandiseInHand.isEmpty())continue instinct;
        final int itemInHandPrice = priceProvider.applyAsInt(merchandiseInHand);
        float chance = impulsePurchaceChance(itemInHandPrice);
        if(rand.nextFloat() < chance){
          handler.extractItem(selectedSlot, 1, false);
          shoppingBag.add(new MerchandiseProperty(stack, merchandiseInHand, itemInHandPrice));
        }
        continue instinct;
      }else{
        break instinct;
      }
    }
    return shoppingBag;
  }
  private float purchaseChance(int expected, int alternative){
    if(alternative<=0 || expected<=0) return 0.0f;
    if(alternative <= expected) return AlternativeRate;
    else return AlternativeRate * (float)Math.pow((float)expected / alternative, 2);
  }
  private float impulsePurchaceChance(int price){
    return Math.min(1.0f, Math.max(0.3f, (300-price)/100));
  }

  private ArrayList<ItemStack> getItemsAsList(IItemHandler handler){
    ArrayList<ItemStack> list = new ArrayList<>();
    for(int i=0; i<handler.getSlots(); i++){
      list.add(handler.getStackInSlot(i));
    }
    return list;
  }

  public static class Builder{
    private int shoppingListSize = 4;
    private float alternative_rate = 0.6f;
    private float instinct_rate = 0.3f;

    public static Builder of(){
      return new Builder();
    }
    public Builder shoppingListSize(int size){
      this.shoppingListSize = size;
      return this;
    }
    public Builder alternative(float rate){
      this.alternative_rate = rate;
      return this;
    }
    public Builder instinct(float rate){
      this.instinct_rate = rate;
      return this;
    }
    public Customer createWith(List<SKU> items){
      List<SKU> list = new ArrayList<>();
      for(int i=0; i<shoppingListSize; i++){
        MerchandiseCategory category = randomCategory();
        List<SKU> candidate = list.stream().filter(sku -> sku.getCategory().equals(category)).toList();
        final int max = candidate.size();
        if(max>0){
          list.add(items.get(rand.nextInt(max)));
        }
      }
      return new Customer(list, alternative_rate, instinct_rate);
    }

    private MerchandiseCategory randomCategory(){
      int seed = rand.nextInt(100);
      if(seed < 16) return MerchandiseCategory.PRODUCE;
      else if(seed < 32) return MerchandiseCategory.MEAT;
      else if(seed < 42) return MerchandiseCategory.FISH;
      else if(seed < 52) return MerchandiseCategory.DAILY;
      else if(seed < 70) return MerchandiseCategory.GROCERY;
      else if(seed < 95) return MerchandiseCategory.DELICA;
      else return MerchandiseCategory.NONFOODS;
    }
  }
}
