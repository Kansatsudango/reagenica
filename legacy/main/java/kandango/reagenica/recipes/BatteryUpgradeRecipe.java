package kandango.reagenica.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class BatteryUpgradeRecipe extends ShapedRecipe{
  public BatteryUpgradeRecipe(ShapedRecipe recipe) {
    super(
        recipe.getId(),
        recipe.getGroup(),
        recipe.category(),
        recipe.getWidth(),
        recipe.getHeight(),
        recipe.getIngredients(),
        recipe.getResultItem(RegistryAccess.EMPTY),
        recipe.showNotification()
    );
}

  @Override
  public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
    ItemStack result = this.getResultItem(registryAccess);
    for(int i=0;i<inv.getContainerSize();i++){
      ItemStack stack = inv.getItem(i);
      CompoundTag tag = stack.getTag();
      if(tag!=null && tag.contains("BlockEntityTag")){
        result.getOrCreateTag().put("BlockEntityTag", tag.get("BlockEntityTag").copy());
        break;
      }
    }
    return result;
  }
  
}
