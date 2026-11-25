package kandango.reagenica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kandango.reagenica.generator.ReagenicaModelProvider;
import kandango.reagenica.registries.ReagenicaBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Reagenica.MODID)
public class Reagenica {
  public static final String MODID = "reagenica";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

  public Reagenica(IEventBus modEventBus, ModContainer modContainer){
    LOGGER.info("Hello Neoforge!");
    ReagenicaBlocks.BLOCKS.register(modEventBus);
    ReagenicaBlocks.BLOCKITEMS.register(modEventBus);
    modEventBus.addListener(ReagenicaModelProvider::gatherData);
  }
}