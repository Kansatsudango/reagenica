package kandango.reagenica.item.reagent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ReagentFluidMap {
  public static final Map<Fluid, Item> fluidItemMap = new HashMap<>();
  public static final Map<Item, Fluid> itemFluidMap = new HashMap<>();
  private static void register(Reagent reagent, RegistryAccess rg){
    Fluid fluid = reagent.getRelativeFluid().orElse(null);
    if(fluid==null)return;
    itemFluidMap.put(reagent, fluid);
    TagKey<Fluid> tag = TagKey.create(Registries.FLUID, reagent.getRelativeFluidTag().orElse(new ResourceLocation("minecraft", "empty")));
    Registry<Fluid> registry = rg.registryOrThrow(Registries.FLUID);
    registry.getTag(tag).ifPresent(named -> named.stream().map(Holder::value).forEach(f -> fluidItemMap.put(f, reagent)));
  }
  public static void registerAll(List<RegistryObject<? extends Item>> itemlist, RegistryAccess rg){
    itemFluidMap.clear();
    fluidItemMap.clear();
    for(RegistryObject<? extends Item> itemobj : itemlist){
      Item item = itemobj.get();
      if(item instanceof Reagent reagent){
        ReagentFluidMap.register(reagent, rg);
      }
    }
    ChemistryMod.LOGGER.info("FluidMap initialized.");
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
