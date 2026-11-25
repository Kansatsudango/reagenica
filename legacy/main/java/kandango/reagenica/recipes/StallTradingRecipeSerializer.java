package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import kandango.reagenica.ChemistryMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class StallTradingRecipeSerializer implements RecipeSerializer<StallTradingRecipe> {

    @Override
    public StallTradingRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        ItemStack merchandise = RecipeJsonHelper.itemStackFromJsonRequired(json, "merchandise");
        ItemStack price = RecipeJsonHelper.itemStackFromJsonRequired(json, "price");
        if(merchandise.getCount() == 0)throw new JsonSyntaxException("Invalid Recipe: merchandise count is 0 in "+id.toString());
        if(merchandise.getCount()<price.getCount() && merchandise.getMaxStackSize()/merchandise.getCount()*price.getCount() > 64){
            ChemistryMod.LOGGER.warn("The price item may overflow in the recipe {}",id.toString());
        }
        return new StallTradingRecipe(id, merchandise, price);
    }

    @Override
    public StallTradingRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        ItemStack merchandise = buf.readItem();
        ItemStack price = buf.readItem();
        return new StallTradingRecipe(id, merchandise, price);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull StallTradingRecipe recipe) {
        buf.writeItemStack(recipe.getMerchandise(), false);
        buf.writeItemStack(recipe.getPrice(), false);
    }
}
