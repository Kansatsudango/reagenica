package kandango.reagenica.recipes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.recipes.items.IngredientWithCount;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class RecipeHelper {
  public static boolean isAllMeets(@Nonnull Container container, @Nonnull List<? extends Ingredient> ingredients){
    ItemStackList inputs = new ItemStackList(container);
    for(Ingredient ingredient : ingredients){
      boolean has = inputs.findAndDrop(ingredient);
      if(!has)return false;
    }
    return true;
  }
  public static boolean isAllMeetsM(@Nonnull Container container, @Nonnull List<? extends IngredientWithCount> ingredients){
    ItemStackList inputs = new ItemStackList(container);
    for(IngredientWithCount ingredient : ingredients){
      boolean has = inputs.findAndDropM(ingredient);
      if(!has)return false;
    }
    return true;
  }
  private static class ItemStackList{
    List<ItemStack> itemlist;
    public ItemStackList(Container container){
      itemlist = new ArrayList<>(container.getContainerSize());
      for(int i=0;i<container.getContainerSize();i++){
        itemlist.add(container.getItem(i).copy());
      }
    }
    public boolean findAndDrop(Ingredient ing){
      for(int i=0;i<itemlist.size();i++){
        if(ing.test(itemlist.get(i))){
          itemlist.get(i).shrink(1);
          return true;
        }
      }
      return false;
    }
    public boolean findAndDropM(IngredientWithCount ing){
      for(int i=0;i<itemlist.size();i++){
        if(ing.test(itemlist.get(i))){
          itemlist.get(i).shrink(ing.getCount());
          return true;
        }
      }
      return false;
    }
  }
}
