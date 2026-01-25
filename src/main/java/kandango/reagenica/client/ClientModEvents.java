package kandango.reagenica.client;

import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.item.reagent.*;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.client.particle.*;
import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiParticles;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@EventBusSubscriber(modid = ChemistryMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
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
  public static final BlockColor LEAVES_COLOR = (state, level, pos, tintIndex) -> {
    if (level == null || pos == null) {
      return FoliageColor.getDefaultColor();
    }
    return BiomeColors.getAverageFoliageColor(level, pos);
  };
  public static final ItemColor LEAVES_ITEM_COLOR = (stack, tintIndex) -> FoliageColor.getDefaultColor();

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    ItemColors colors = event.getItemColors();
    colors.register(REAGENT_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof LiquidReagent).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(REAGENT_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof PowderReagent).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(REAGENT_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof GasReagent).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(REAGENT_ITEM_COLOR_0, ChemiItems.listItems.stream().filter(x -> x.get() instanceof ReagentPowderIndustrial).map(RegistryObject::get).toArray(Item[]::new));
    colors.register(BIO_ITEM_COLOR, ChemiItems.listItems.stream().filter(x -> x.get() instanceof BioReagent).filter(x -> x!=ChemiItems.MEDIUM_PLATE).map(RegistryObject::get).toArray(Item[]::new));
    event.register(LEAVES_ITEM_COLOR, ChemiBlocks.METASEQUOIA_LEAVES_ITEM.get());
    event.register(LEAVES_ITEM_COLOR, ChemiBlocks.TAXODIUM_LEAVES_ITEM.get());
  }

  @SubscribeEvent
  public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register(LEAVES_COLOR, ChemiBlocks.METASEQUOIA_LEAVES.get());
    event.register(LEAVES_COLOR, ChemiBlocks.TAXODIUM_LEAVES.get());
  }

  @SubscribeEvent
  public static void registerParticles(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(ChemiParticles.GLOWING_SPORE.get(), GlowingSpore.Provider::new);
    event.registerSpriteSet(ChemiParticles.AMBIENT_SPORE.get(), AmbientSpore.Provider::new);
  }
}
