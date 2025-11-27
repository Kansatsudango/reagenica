package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class DissolverRecipeSerializer implements RecipeSerializer<DissolverRecipe> {

    @Override
    public DissolverRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        FluidStack influid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "influid");
        FluidStack outfluid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "outfluid");
        ItemStack outitem = RecipeJsonHelper.itemStackFromJsonifPresent(json, "itemresult");
        return new DissolverRecipe(id, input, influid, outfluid, outitem);
    }

    @Override
    public DissolverRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        FluidStack influid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        FluidStack outfluid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        ItemStack outitem = buf.readItem();
        return new DissolverRecipe(id, input, influid, outfluid, outitem);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull DissolverRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        RecipeJsonHelper.sendFluidStack(buf, recipe.getFluidIn());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getFluidOut());
        buf.writeItemStack(recipe.getOutputItem(), false);
    }
}
