package kandango.reagenica.world;

import kandango.reagenica.world.task.IChainMiningData;
import kandango.reagenica.world.task.ISeedPlacingData;
import kandango.reagenica.world.task.IDelayedSoundData;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;

public class ChemiCapabilities {
  public static final Capability<IChainMiningData> CHAIN_MINING_DATA = CapabilityManager.get(new CapabilityToken<>(){});
  public static final Capability<ISeedPlacingData> SEED_PLACING_DATA = CapabilityManager.get(new CapabilityToken<>(){});
  public static final Capability<IDelayedSoundData> DELAYED_SOUND_DATA = CapabilityManager.get(new CapabilityToken<>(){});
}
