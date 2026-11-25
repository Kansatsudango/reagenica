package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class ElectroLysisRecipeSerializer implements RecipeSerializer<ElectroLysisRecipe> {

    @Override
    public ElectroLysisRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
        ItemStack result_n = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result_n");
        ItemStack result_p = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result_p");
        ItemStack result_gas_n = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result_gas_n");
        ItemStack result_gas_p = RecipeJsonHelper.itemStackFromJsonifPresent(json, "result_gas_p");
        Ingredient cathode = RecipeJsonHelper.ingredientFromJsonRequired(json, "cathode");
        Ingredient anode = RecipeJsonHelper.ingredientFromJsonRequired(json, "anode");
        FluidStack influid = RecipeJsonHelper.fluidStackFromJsonRequired(json, "influid");
        FluidStack outfluid = RecipeJsonHelper.fluidStackFromJsonifPresent(json, "outfluid");
        boolean anodemelt = GsonHelper.getAsBoolean(json, "anode_oxidize", false);
        return new ElectroLysisRecipe(id, influid, cathode, anode, outfluid, result_n, result_p, result_gas_n, result_gas_p, anodemelt);
    }

    @Override
    public ElectroLysisRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
        ItemStack result_n = buf.readItem();
        ItemStack result_p = buf.readItem();
        ItemStack result_gas_n = buf.readItem();
        ItemStack result_gas_p = buf.readItem();
        Ingredient cathode = Ingredient.fromNetwork(buf);
        Ingredient anode = Ingredient.fromNetwork(buf);
        FluidStack influid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        FluidStack outfluid = RecipeJsonHelper.fluidStackFromFriendlyBuf(buf);
        boolean anodemelt = buf.readBoolean();
        return new ElectroLysisRecipe(id, influid, cathode, anode, outfluid, result_n, result_p, result_gas_n, result_gas_p, anodemelt);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buf,@Nonnull ElectroLysisRecipe recipe) {
        buf.writeItemStack(recipe.getOutputN(), false);
        buf.writeItemStack(recipe.getOutputP(), false);
        buf.writeItemStack(recipe.getOutputGasN(), false);
        buf.writeItemStack(recipe.getOutputGasP(), false);
        recipe.getElectrodeN().toNetwork(buf);
        recipe.getElectrodeP().toNetwork(buf);
        RecipeJsonHelper.sendFluidStack(buf, recipe.getFluidIn());
        RecipeJsonHelper.sendFluidStack(buf, recipe.getFluidOut());
      buf.writeBoolean(recipe.anodeMelts());
    }
}
