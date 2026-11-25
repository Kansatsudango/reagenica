package kandango.reagenica.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kandango.reagenica.world.task.ChainMiningTaskManager;
import kandango.reagenica.world.task.IChainMiningData;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ChainMiningProvider implements ICapabilityProvider {
  private final IChainMiningData instance = new ChainMiningTaskManager();
  private final LazyOptional<IChainMiningData> optional = LazyOptional.of(() -> instance);

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == ChemiCapabilities.CHAIN_MINING_DATA ? optional.cast() : LazyOptional.empty();
  }
  
}
