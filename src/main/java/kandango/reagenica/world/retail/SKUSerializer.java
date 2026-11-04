package kandango.reagenica.world.retail;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import kandango.reagenica.recipes.RecipeJsonHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SKUSerializer implements RecipeSerializer<SKU> {

  @Override
  public SKU fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
    Ingredient item = RecipeJsonHelper.ingredientFromJsonRequired(json, "item");
    int cost = RecipeJsonHelper.intFromJson(json, "cost");
    int price = RecipeJsonHelper.intFromJson(json, "price");
    MerchandiseCategory category = RecipeJsonHelper.stringFromJson(json, "category").flatMap(MerchandiseCategory::fromString).orElseThrow(() -> new JsonSyntaxException("Key category is missing or invalid"));
    return new SKU(id, item, cost, price, category);
  }

  @Override
  public SKU fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
    Ingredient item = Ingredient.fromNetwork(buf);
    int cost = buf.readVarInt();
    int price = buf.readVarInt();
    MerchandiseCategory category = MerchandiseCategory.values()[buf.readByte()];
    return new SKU(id, item, cost, price, category);
  }

  @Override
  public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull SKU sku) {
    sku.getItem().toNetwork(buf);
    buf.writeVarInt(sku.getBaseCost());
    buf.writeVarInt(sku.getBasePrice());
    buf.writeByte(sku.getCategory().ordinal());
  }
}
