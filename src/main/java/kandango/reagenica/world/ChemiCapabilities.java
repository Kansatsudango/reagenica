package kandango.reagenica.world;

import kandango.reagenica.world.task.IChainMiningData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ChemiCapabilities {
  public static final Capability<IChainMiningData> CHAIN_MINING_DATA = CapabilityManager.get(new CapabilityToken<>(){});
}
