package kandango.reagenica.recipes.items;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import kandango.reagenica.recipes.RecipeJsonHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientWithCount{
  private final Ingredient ingredient;
  private final int count;
  public IngredientWithCount(Ingredient ing, int count){
    this.ingredient=ing;
    this.count=count;
  }

  public Ingredient getIngredient(){
    return ingredient;
  }
  public int getCount(){
    return count;
  }
  public boolean test(ItemStack stack){
    return ingredient.test(stack) && stack.getCount()>=this.count;
  }

  public ItemStack toItemStack(int index){
    return new ItemStack(ingredient.getItems()[index].getItem(), count);
  }

  public static IngredientWithCount fromJson(@Nonnull JsonObject json){
    Ingredient item = Ingredient.fromJson(json);
    int count = RecipeJsonHelper.intFromJsonOptional(json, "count").orElse(1);
    return new IngredientWithCount(item, count);
  }
  public static IngredientWithCount fromFriendlyBuf(@Nonnull FriendlyByteBuf buf){
    Ingredient stack = Ingredient.fromNetwork(buf);
    int count = buf.readVarInt();
    return new IngredientWithCount(stack, count);
  }
  public static void toFriendlyBuf(@Nonnull FriendlyByteBuf buf, IngredientWithCount ingcount){
    ingcount.ingredient.toNetwork(buf);
    buf.writeVarInt(ingcount.count);
  }
}
