package kandango.reagenica.world.retail;

import java.util.HashMap;
import java.util.Map;

import kandango.reagenica.recipes.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class SKU implements Recipe<Container> {
  private static final Map<Item, SKU> skuMap = new HashMap<>();
  public static final SKU EMPTY = new SKU(new ResourceLocation("reagenica", "null"), Ingredient.EMPTY, 0, 0, MerchandiseCategory.NULL);

  private final ResourceLocation id;
  private final Ingredient item;
  public Ingredient getItem(){return item;}
  private final int baseCost;
  public int getBaseCost(){return baseCost;}
  private final int basePrice;
  public int getBasePrice(){return basePrice;}
  private final MerchandiseCategory category;
  public MerchandiseCategory getCategory(){return category;}

  public SKU(ResourceLocation id, Ingredient item, int cost, int price, MerchandiseCategory cat){
    this.id = id;
    this.item = item;
    this.baseCost = cost;
    this.basePrice = price;
    this.category = cat;
  }

  public static SKU lookup(Level lv, ItemStack stack){
    if(lv==null)throw new NullPointerException();
    if(stack.isEmpty())return SKU.EMPTY;
    Item item = stack.getItem();
    return skuMap.computeIfAbsent(
        item, 
        i -> lv.getRecipeManager().getAllRecipesFor(ModRecipes.SKU_TYPE.get())
              .stream().filter(sku -> sku.item.test(stack)).findFirst().orElse(SKU.EMPTY)
      );
  }

  public static void refleshSKULookupCache(){
    skuMap.clear();
  }

  @Override
  public boolean matches(Container c, Level l) {
    return false;
  }

  @Override
  public ItemStack assemble(Container c, RegistryAccess rg) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int p, int q) {
    return true;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess rg) {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.SKU_SERIALIZER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.SKU_TYPE.get();
  }

}
