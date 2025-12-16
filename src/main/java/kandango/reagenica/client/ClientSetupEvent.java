package kandango.reagenica.client;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.fluid.ChemiFluidInterface;
import kandango.reagenica.client.renderer.DissolverFluidRenderer;
import kandango.reagenica.client.renderer.ElectrolysisFluidRenderer;
import kandango.reagenica.client.renderer.FluidPipeRenderer;
import kandango.reagenica.client.renderer.HeatFurnaceFluidRenderer;
import kandango.reagenica.client.renderer.LargeTankRenderer;
import kandango.reagenica.client.renderer.LeadBatteryFluidRenderer;
import kandango.reagenica.client.renderer.OnsenFillerFluidRenderer;
import kandango.reagenica.client.renderer.SmallTankRenderer;
import kandango.reagenica.client.renderer.StackLampRenderer;
import kandango.reagenica.client.renderer.TradingStallRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ChemistryMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvent {
  @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            fluidTranslucent(ChemiFluids.BIOETHANOL);
            fluidTranslucent(ChemiFluids.SULFURIC_ACID);
            fluidTranslucent(ChemiFluids.DILUTE_SULFURIC_ACID);
            fluidTranslucent(ChemiFluids.SODIUM_HYDROXIDE);
            fluidTranslucent(ChemiFluids.ETHANOL);
            fluidTranslucent(ChemiFluids.PHOSPHORIC_ACID);
            fluidTranslucent(ChemiFluids.COPPER_SULFATE);
            //SOY SAUCE
            fluidTranslucent(ChemiFluids.SALT_WATER);
            fluidTranslucent(ChemiFluids.DISTILLED_WATER);
            fluidTranslucent(ChemiFluids.HEATED_WATER);
            fluidTranslucent(ChemiFluids.AQUA_REGIA);
            fluidTranslucent(ChemiFluids.GOLD_SOLUTION);
            fluidTranslucent(ChemiFluids.SILVER_SOLUTION);
            fluidTranslucent(ChemiFluids.PLATINUM_SOLUTION);
            fluidTranslucent(ChemiFluids.COOKING_SAKE);
            fluidTranslucent(ChemiFluids.MIRIN);
            //CRUDE OIL
            fluidTranslucent(ChemiFluids.DIESEL_FUEL);
            fluidTranslucent(ChemiFluids.NAPHTHA);
            fluidTranslucent(ChemiFluids.ETHYLENE);
            fluidTranslucent(ChemiFluids.BENZENE);
            fluidTranslucent(ChemiFluids.AMMONIA);
            fluidTranslucent(ChemiFluids.SIMPLE_HOTSPRING);
            fluidTranslucent(ChemiFluids.SULFUR_HOTSPRING);
            fluidTranslucent(ChemiFluids.CHLORIDE_HOTSPRING);
            fluidTranslucent(ChemiFluids.IRON_HOTSPRING);
            fluidTranslucent(ChemiFluids.RADIOACTIVE_HOTSPRING);

            BlockEntityRenderers.register(ModBlockEntities.TRADING_STALL.get(), TradingStallRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.PIPE_COPPER.get(), FluidPipeRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.PIPE_GOLD.get(), FluidPipeRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.PIPE_PVC.get(), FluidPipeRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.ELECTROLYSIS_DEVICE.get(), ElectrolysisFluidRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.COPPER_TANK.get(), SmallTankRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.HEAT_FURNACE.get(), HeatFurnaceFluidRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.STACK_LAMP.get(), StackLampRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.DISSOLVER.get(), DissolverFluidRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.LEAD_BATTERY.get(), LeadBatteryFluidRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.LARGE_TANK_CORE.get(), LargeTankRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.ONSEN_FILLER.get(), OnsenFillerFluidRenderer::new);

            ItemProperties.register(ChemiItems.ALCHOHOL_LAMP.get(), new ResourceLocation("empty"), (stack, level, entity, seed) -> stack.getDamageValue() >= stack.getMaxDamage() ? 1.0F : 0.0F);
        });
    }

    private static void fluidTranslucent(ChemiFluidInterface fluid){
        fluid.getType().initializeClient(client -> {});
        ItemBlockRenderTypes.setRenderLayer(fluid.getFluid(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(fluid.getFlowingFluid(), RenderType.translucent());
    }
}
