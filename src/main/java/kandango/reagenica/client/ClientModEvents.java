package kandango.reagenica.client;

import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.item.reagent.GasReagent;
import kandango.reagenica.item.reagent.LiquidReagent;
import kandango.reagenica.item.reagent.PowderReagent;
import kandango.reagenica.item.reagent.Reagent;
import kandango.reagenica.item.reagent.ReagentPowderIndustrial;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.ChemiItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@EventBusSubscriber(modid = ChemistryMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
  public static final ItemColor REAGENT_ITEM_COLOR = (stack, tintIndex)-> {
    if(tintIndex == 1 && stack.getItem() instanceof Reagent reagent){
      return reagent.getColor();
    }
    return 0xFFFFFFFF;
  };
  public static final ItemColor REAGENT_ITEM_COLOR_0 = (stack, tintIndex)-> {
    if(tintIndex == 0 && stack.getItem() instanceof Reagent reagent){
      return reagent.getColor();
    }
    return 0xFFFFFFFF;
  };
  public static final ItemColor BIO_ITEM_COLOR = (stack, tintIndex)-> {
    if(tintIndex == 1 && stack.getItem() instanceof BioReagent reagent){
      return reagent.getColor(stack);
    }
    return 0xFFFFFFFF;
  };

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    ItemColors colors = event.getItemColors();
    colors.register(REAGENT_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof LiquidReagent).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(REAGENT_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof PowderReagent).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(REAGENT_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof GasReagent).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(REAGENT_ITEM_COLOR_0, ChemiItems.listItems.stream().filter(x -> x.get() instanceof ReagentPowderIndustrial).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(BIO_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof BioReagent).filter(x -> x!=ChemiItems.MEDIUM_PLATE).map(RegistryObject::get).toArray(Item[]::new));
  }
}
