package kandango.reagenica.item.reagent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ReagentFluidMap {
  public static Map<Fluid, Item> fluidItemMap = new HashMap<>();
  public static Map<Item, Fluid> itemFluidMap = new HashMap<>();
  public static void register(@Nonnull Fluid fluid, @Nonnull Item continerItem){
    if(fluidItemMap.containsKey(fluid)){
      throw new IllegalArgumentException("dupricate fluid map registry: " + ForgeRegistries.ITEMS.getKey(continerItem));
    }
    fluidItemMap.put(fluid, continerItem);
    itemFluidMap.put(continerItem, fluid);
  }
  public static void registerAll(List<RegistryObject<? extends Item>> itemlist){
    for(RegistryObject<? extends Item> itemobj : itemlist){
      Item item = itemobj.get();
      if(item instanceof LiquidReagent reagent){
        reagent.getRelativeFluid().ifPresent(x -> ReagentFluidMap.register(x, reagent));
      }else if(item instanceof GasReagent reagent){
        reagent.getRelativeFluid().ifPresent(x -> ReagentFluidMap.register(x, reagent));
      }
    }
  }
  public static void printMap(){
      ChemistryMod.LOGGER.debug("=== Fluid <-> Item Map Contents ===");

      for (Map.Entry<Fluid, Item> entry : fluidItemMap.entrySet()) {
        Fluid fluid = entry.getKey();
        Item item = entry.getValue();

        ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(fluid);
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);

        String fluidName = fluidId != null ? fluidId.toString() : "unknown_fluid";
        String itemName = itemId != null ? itemId.toString() : "unknown_item";

        ChemistryMod.LOGGER.debug("Fluid {} = Item {}", fluidName, itemName);
    }
  }
  public static Optional<Item> getItemfromFluid(Fluid fluid){
    return Optional.ofNullable(ReagentFluidMap.fluidItemMap.get(fluid));
  }
  public static Optional<Fluid> getFluidfromItem(Item item){
    return Optional.ofNullable(ReagentFluidMap.itemFluidMap.get(item));
  }
}
