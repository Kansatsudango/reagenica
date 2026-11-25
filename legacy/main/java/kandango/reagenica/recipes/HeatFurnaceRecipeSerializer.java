package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class HeatFurnaceRecipeSerializer implements RecipeSerializer<HeatFurnaceRecipe> {

    @Override
    public HeatFurnaceRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        Ingredient input = RecipeJsonHelper.ingredientFromJsonRequired(json, "input");
        ItemStack result = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result");
        ItemStack result2 = RecipeJsonHelper.itemStackFromJsonifPresent(json, "byproduct");
        FluidStack influid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "influid");
        FluidStack outfluid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "outfluid");
        return new HeatFurnaceRecipe(id, input, result, result2, influid, outfluid);
    }

    @Override
    public HeatFurnaceRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        ItemStack result2 = buf.readItem();
        FluidStack influid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        FluidStack outfluid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        return new HeatFurnaceRecipe(id, input, result, result2, influid, outfluid);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull HeatFurnaceRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        buf.writeItemStack(recipe.getOutput(), false);
        buf.writeItemStack(recipe.getByproduct(), false);
        RecipeJsonHelper.sendFluidStack(buf, recipe.getFluidIn());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getFluidOut());
    }
}
