package kandango.reagenica.recipes.items;

import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import kandango.reagenica.recipes.RecipeJsonHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class ItemStackWithChance {
  public static final ItemStackWithChance EMPTY = new ItemStackWithChance(ItemStack.EMPTY, 0);
  private final ItemStack stack;
  private final float chance;
  public ItemStackWithChance(ItemStack stack, float chance){
    this.stack = stack;
    this.chance = chance;
  }
  public ItemStack get(){
    return stack.copy();
  }
  public ItemStack roll(Random rand){
    float random = rand.nextFloat();
    return random<chance ? stack.copy() : ItemStack.EMPTY;
  }
  public float getChance(){
    return chance;
  }

  public static ItemStackWithChance fromJson(@Nonnull JsonObject json){
    ItemStack stack = ShapedRecipe.itemStackFromJson(json);
    float chance = RecipeJsonHelper.floatFromJson(json, "chance");
    return new ItemStackWithChance(stack, chance);
  }
  public static ItemStackWithChance fromFriendlyBuf(@Nonnull FriendlyByteBuf buf){
    ItemStack stack = buf.readItem();
    float chance = buf.readFloat();
    return new ItemStackWithChance(stack, chance);
  }
  public static void toFriendlyBuf(@Nonnull FriendlyByteBuf buf, ItemStackWithChance stackchance){
    buf.writeItemStack(stackchance.stack, false);
    buf.writeFloat(stackchance.chance);
  }
}
