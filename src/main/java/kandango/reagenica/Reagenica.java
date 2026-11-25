package kandango.reagenica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoforged.fml.common.Mod;

@Mod(Reagenica.MODID)
public class Reagenica {
  public static final String MODID = "reagenica";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

  public Reagenica(){
    LOGGER.info("Hello Neoforge!");
  }
}