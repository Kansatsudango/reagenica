package kandango.reagenica.event;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.fluid.ChemiFluidBurnMap;
import kandango.reagenica.family.WoodFamily;
import kandango.reagenica.packet.ModMessages;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "reagenica", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

  @SubscribeEvent
  public static void onCommonSetup(FMLCommonSetupEvent event) {
    ModMessages.register();
    ChemiFluidBurnMap.register();
    event.enqueueWork(() -> {
      try{
        ChemiBlocks.listFlowerPots.forEach(pot -> {
          ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(
                  ForgeRegistries.BLOCKS.getKey(pot.get().getContent()),
                  pot
            );
          }
        );
        WoodFamily.Woods.forEach(family -> {
          ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(
                  family.SAPLING.getId(),
                  family.POTTED_SAPLING
            );
          }
        );
      }catch(Exception e){
        ChemistryMod.LOGGER.error("Exception caught: {}", e);
      }
    });
  }
}
