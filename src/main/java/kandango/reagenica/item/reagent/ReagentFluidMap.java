package kandango.reagenica.item.reagent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ReagentFluidMap {
  public static final Map<Fluid, Item> fluidItemMap = new HashMap<>();
  public static final Map<Item, Fluid> itemFluidMap = new HashMap<>();
  public static void register(@Nonnull Fluid fluid, @Nonnull Item continerItem, Level lv){
    if(fluidItemMap.containsKey(fluid)){
      throw new IllegalArgumentException("dupricate fluid map registry: " + ForgeRegistries.ITEMS.getKey(continerItem));
    }
    //fluidItemMap.put(fluid, continerItem);
    itemFluidMap.put(continerItem, fluid);
    ResourceLocation fluidLocation = ForgeRegistries.FLUIDS.getKey(fluid);
    TagKey<Fluid> tag = fluidLocation.getNamespace().equals("minecraft")
                ? TagKey.create(Registries.FLUID, fluidLocation)
                : TagKey.create(Registries.FLUID, new ResourceLocation("forge", fluidLocation.getPath()));
    Registry<Fluid> registry = lv.registryAccess().registryOrThrow(Registries.FLUID);
    registry.getTag(tag).ifPresent(named -> named.stream().map(Holder::value).forEach(f -> fluidItemMap.put(f, continerItem)));
  }
  public static void registerAll(List<RegistryObject<? extends Item>> itemlist, Level lv){
    itemFluidMap.clear();
    fluidItemMap.clear();
    for(RegistryObject<? extends Item> itemobj : itemlist){
      Item item = itemobj.get();
      if(item instanceof Reagent reagent){
        reagent.getRelativeFluid().ifPresent(fluid -> ReagentFluidMap.register(fluid, reagent, lv));
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
