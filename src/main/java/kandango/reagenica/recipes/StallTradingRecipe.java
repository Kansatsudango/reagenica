package kandango.reagenica.recipes;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.util.ItemStackUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class StallTradingRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final ItemStack merchandise;
  public ItemStack getMerchandise() {
    return merchandise;
  }
  private final ItemStack price;
  public ItemStack getPrice() {
    return price;
  }
  
  public StallTradingRecipe(ResourceLocation id, ItemStack m, ItemStack p){
    this.id=id;
    this.merchandise = m;
    this.price = p;
  }
  public StallTradingRecipe(ResourceLocation id, ItemStack m, ItemStack p, String profession){
    this.id=id;
    this.merchandise = m;
    this.price = p;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return ItemStackUtil.isEnough(container.getItem(0),this.merchandise);
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return price; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.STALL_TRADING_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.STALL_TRADING_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return price.copy();
  }
}
