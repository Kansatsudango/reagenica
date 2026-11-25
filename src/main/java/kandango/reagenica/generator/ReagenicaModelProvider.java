package kandango.reagenica.generator;

import kandango.reagenica.Reagenica;
import kandango.reagenica.registries.ReagenicaBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class ReagenicaModelProvider extends ModelProvider{
  public ReagenicaModelProvider(PackOutput output) {
    super(output, Reagenica.MODID);
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    blockModels.createTrivialCube(
        ReagenicaBlocks.INITIAL_BLOCK.get()
    );
  }

  public static void gatherData(GatherDataEvent.Client event) {
    event.createProvider(ReagenicaModelProvider::new);
  }
}
