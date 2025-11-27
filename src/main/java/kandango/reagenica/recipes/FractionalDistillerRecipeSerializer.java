package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class FractionalDistillerRecipeSerializer implements RecipeSerializer<FractionalDistillerRecipe> {

    @Override
    public FractionalDistillerRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        FluidStack influid = RecipeJsonHelper.fluidStackFromJsonRequired(json, "influid");
        FluidStack topfluid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "topfluid");
        FluidStack bottomfluid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "bottomfluid");
        ItemStack residual = RecipeJsonHelper.itemStackFromJsonifPresent(json, "residual");
        return new FractionalDistillerRecipe(id, influid, topfluid, bottomfluid, residual);
    }

    @Override
    public FractionalDistillerRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        FluidStack influid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        FluidStack topfluid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        FluidStack bottomfluid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        ItemStack residual = buf.readItem();
        return new FractionalDistillerRecipe(id, influid, topfluid, bottomfluid, residual);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull FractionalDistillerRecipe recipe) {
        RecipeJsonHelper.sendFluidStack(buf, recipe.getInput());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getTop());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getBottom());
        buf.writeItemStack(recipe.getResidual(), false);
    }
}
