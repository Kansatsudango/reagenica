package kandango.reagenica.generator;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event){
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

    generator.addProvider(event.includeServer(), new ChemiLootTableProvider(packOutput));
    generator.addProvider(event.includeServer(), new ChemiBlocktagsProvider(packOutput, lookupProvider, existingFileHelper));
    generator.addProvider(event.includeClient(), new ChemiBlockStateProvider(packOutput, existingFileHelper));
  }
}
