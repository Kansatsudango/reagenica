package kandango.reagenica.event;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiEntities;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.entity.renderer.SilverArrowRenderer;
import kandango.reagenica.item.LocationCompass;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid=ChemistryMod.MODID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEventHandler {
  @SuppressWarnings({ "deprecation" })
  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
        ItemBlockRenderTypes.setRenderLayer(ChemiBlocks.EXPERIMENT_BLOCK.get(), RenderType.cutout());
        ItemProperties.register(ChemiItems.SILVER_BOW.get(), ResourceLocation.fromNamespaceAndPath("pull"), (stack, level, entity, seed) -> {
            if (entity == null) return 0.0F;
            return entity.getUseItem() != stack ? 0.0F :
                    (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
        });

        ItemProperties.register(ChemiItems.SILVER_BOW.get(), ResourceLocation.fromNamespaceAndPath("pulling"), (stack, level, entity, seed) -> {
            return (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? 1.0F : 0.0F;
        });

        ItemProperties.register(ChemiItems.COAL_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.IRON_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.GOLD_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.LAPIS_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.REDSTONE_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.EMERALD_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.DIAMOND_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
        ItemProperties.register(ChemiItems.LEAD_COMPASS.get(), ResourceLocation.fromNamespaceAndPath("angle"), new CompassItemPropertyFunction(LocationCompass::getPos));
    });
  }

  @SubscribeEvent
  public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(ChemiEntities.SILVER_ARROW.get(), SilverArrowRenderer::new);
  }
}
