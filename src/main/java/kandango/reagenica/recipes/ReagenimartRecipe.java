package kandango.reagenica.recipes;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonSyntaxException;

import kandango.reagenica.block.entity.util.ItemStackUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ReagenimartRecipe implements Recipe<Container> {
  private final ResourceLocation id;
  private final ItemStack merchandise;
  public ItemStack getMerchandise() {
    return merchandise;
  }
  private final ItemStack price;
  public ItemStack getPrice() {
    return price;
  }
  private final ReagenimartCategory category;
  public ReagenimartCategory getCategory(){
    return category;
  }
  private final int order;
  public int getOrder(){
    return order;
  }
  
  public ReagenimartRecipe(ResourceLocation id, ItemStack m, ItemStack p, String cat, int ord){
    this.id=id;
    this.merchandise = m;
    this.price = p;
    this.category = tocat(cat);
    this.order = ord;
  }
  public ReagenimartRecipe(ResourceLocation id, ItemStack m, ItemStack p, int cat, int ord){
    this.id=id;
    this.merchandise = m;
    this.price = p;
    this.category = ReagenimartCategory.values()[cat];
    this.order = ord;
  }

  @Override
  public boolean matches(@Nonnull Container container, @Nonnull Level level){
    return ItemStackUtil.isEnough(container.getItem(0),this.merchandise);
  }
  public boolean isValidPayment(@Nonnull ItemStack stack){
    return ItemStackUtil.isEnough(stack, this.price);
  }

  public static List<ReagenimartRecipe> getAllRecipe(@Nonnull Level lv){
    return lv.getRecipeManager().getAllRecipesFor(ModRecipes.REAGENIMART_TYPE.get());
  }
  public static List<ReagenimartRecipe> getRecipeCategorized(@Nonnull Level lv, ReagenimartCategory cat){
    return getAllRecipe(lv).stream().filter(r -> r.getCategory().equals(cat)).sorted(Comparator.comparingInt((ReagenimartRecipe r) -> r.order).thenComparing(r -> r.id.toString())).toList();
  }

  @Override public boolean canCraftInDimensions(int width, int height) { return true; }
  @Override public ItemStack getResultItem(@Nonnull RegistryAccess access) { return price; }
  @Override public ResourceLocation getId() { return id; }
  @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.REAGENIMART_SERIALIZER.get(); }
  @Override public RecipeType<?> getType() { return ModRecipes.REAGENIMART_TYPE.get(); }

  @Override
  public ItemStack assemble(@Nonnull Container p_44001_, @Nonnull RegistryAccess p_267165_) {
    return price.copy();
  }
  private ReagenimartCategory tocat(String key){
    switch (key) {
      case "building":
        return ReagenimartCategory.BUILDING;
      case "commodity":
        return ReagenimartCategory.COMMODITY;
      case "minerals":
        return ReagenimartCategory.MINERALS;
      case "naturals":
        return ReagenimartCategory.NATURALS;
      default:
        throw new JsonSyntaxException("[" + key + "]" + " is not valid category");
    }
  }
  public static enum ReagenimartCategory{
    BUILDING,
    COMMODITY,
    MINERALS,
    NATURALS
  }
}
