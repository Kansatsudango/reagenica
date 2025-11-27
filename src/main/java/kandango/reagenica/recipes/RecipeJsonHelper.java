package kandango.reagenica.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeJsonHelper {
  public static FluidStack fluidStackFromJson(@Nonnull JsonObject json, String name){
    JsonObject resultfluidObj = json.getAsJsonObject(name);
    int amount = resultfluidObj.get("amount").getAsInt();
    if(amount==0)return FluidStack.EMPTY;
    String fluidId = resultfluidObj.get("fluid").getAsString();
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidId));
    if (fluid == null) {
      throw new JsonSyntaxException("Invalid fluid id: '" + fluidId + "' in '" + name + "'");
    }
    FluidStack result = new FluidStack(fluid, amount);
    return result;
  }

  public static FluidStack fluidStackFromJsonifPresent(@Nonnull JsonObject json, String name){
    return json.has(name) ? RecipeJsonHelper.fluidStackFromJson(json, name) : FluidStack.EMPTY;
  }
  public static FluidStack fluidStackFromJsonRequired(@Nonnull JsonObject json, String name){
    if(json.has(name)){
      return RecipeJsonHelper.fluidStackFromJson(json, name);
    }else{
      throw new JsonSyntaxException("Fluid "+name+" is required");
    }
  }

  public static ItemStack itemStackFromJsonifPresent(@Nonnull JsonObject json, String name){
    return json.has(name)?ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, name)):ItemStack.EMPTY;
  }
  public static ItemStack itemStackFromJsonRequired(@Nonnull JsonObject json, String name){
    if(!json.has(name)){
      throw new JsonSyntaxException("Itemstack "+name+" is required");
    }
    return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, name));
  }
  public static Ingredient ingredientFromJsonifPresent(@Nonnull JsonObject json, String name){
    return json.has(name)?Ingredient.fromJson(GsonHelper.getAsJsonObject(json, name)):Ingredient.EMPTY;
  }
  public static Ingredient ingredientFromJsonRequired(@Nonnull JsonObject json, String name){
    if(!json.has(name)){
      throw new JsonSyntaxException("Ingredient "+name+" is required");
    }
    return Ingredient.fromJson(GsonHelper.getAsJsonObject(json, name));
  }
  public static int intFromJson(@Nonnull JsonObject json, String name){
    if(!json.has(name)){
      throw new JsonSyntaxException("Integer "+name+" is required");
    }
    return json.get(name).getAsInt();
  }
  public static Optional<Integer> intFromJsonOptional(@Nonnull JsonObject json, String name){
    return Optional.ofNullable(json.get(name)).map(j -> j.getAsInt());
  }
  public static Optional<String> stringFromJson(@Nonnull JsonObject json, String name){
    return Optional.ofNullable(json.get(name)).map(j -> j.getAsString());
  }
  public static float floatFromJson(@Nonnull JsonObject json, String name){
    if(!json.has(name)){
      throw new JsonSyntaxException("Float "+name+" is required");
    }
    return json.get(name).getAsFloat();
  }

  public static <T> List<T> listFromJson(@Nonnull JsonObject json, String name, Function<JsonObject,T> reader){
    if(!json.has(name)){
      throw new JsonSyntaxException("Itemstacks "+name+" is required");
    }
    JsonArray itemsArray = GsonHelper.getAsJsonArray(json, name);
    List<T> items = new ArrayList<>();
    for(JsonElement elem : itemsArray){
      JsonObject obj = elem.getAsJsonObject();
      items.add(reader.apply(obj));
    }
    return items;
  }

  public static FluidStack fluidStackFromFriendlyBuf(@Nonnull FriendlyByteBuf buf){
    ResourceLocation fluidId = new ResourceLocation(buf.readUtf());
    int amount = buf.readInt();
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
    return new FluidStack(fluid, amount);
  }

  public static void sendFluidStack(@Nonnull FriendlyByteBuf buf, @Nonnull FluidStack fluid){
    buf.writeUtf(ForgeRegistries.FLUIDS.getKey(fluid.getFluid()).toString());
    buf.writeInt(fluid.getAmount());
  }

  public static List<Ingredient> listIngredientFromFrientlyBuf(@Nonnull FriendlyByteBuf buf){
    int count = buf.readVarInt();
    List<Ingredient> list = new ArrayList<>();
    for(int i=0;i<count;i++){
      list.add(Ingredient.fromNetwork(buf));
    }
    return list;
  }
  public static void sendIngredientList(@Nonnull FriendlyByteBuf buf, @Nonnull List<? extends Ingredient> list){
    buf.writeVarInt(list.size());
    for(Ingredient item : list){
      item.toNetwork(buf);
    }
  }
  public static <T> List<T> listFromFriendlyBuf(@Nonnull FriendlyByteBuf buf, Function<FriendlyByteBuf,T> reader){
    int count = buf.readVarInt();
    List<T> list = new ArrayList<>();
    for(int i=0;i<count;i++){
      list.add(reader.apply(buf));
    }
    return list;
  }
  public static <T> void sendList(@Nonnull FriendlyByteBuf buf, @Nonnull List<T> list,BiConsumer<FriendlyByteBuf,T> writer){
    buf.writeVarInt(list.size());
    for(T item : list){
      writer.accept(buf, item);
    }
  }
}
