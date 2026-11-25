package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ReagenimartRecipeSerializer implements RecipeSerializer<ReagenimartRecipe> {

    @Override
    public ReagenimartRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        ItemStack merchandise = RecipeJsonHelper.itemStackFromJsonRequired(json, "merchandise");
        ItemStack price = RecipeJsonHelper.itemStackFromJsonRequired(json, "price");
        String type = RecipeJsonHelper.stringFromJson(json, "category").orElseThrow(() -> new JsonSyntaxException("category is required"));
        int order = RecipeJsonHelper.intFromJsonOptional(json, "order").orElse(10);
        return new ReagenimartRecipe(id, merchandise, price, type, order);
    }

    @Override
    public ReagenimartRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        ItemStack merchandise = buf.readItem();
        ItemStack price = buf.readItem();
        int cat = buf.readVarInt();
        int order = buf.readVarInt();
        return new ReagenimartRecipe(id, merchandise, price, cat, order);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull ReagenimartRecipe recipe) {
        buf.writeItemStack(recipe.getMerchandise(), false);
        buf.writeItemStack(recipe.getPrice(), false);
        buf.writeVarInt(recipe.getCategory().ordinal());
        buf.writeVarInt(recipe.getOrder());
    }
}
