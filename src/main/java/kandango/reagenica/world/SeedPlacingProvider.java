package kandango.reagenica.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kandango.reagenica.world.task.SeedPlacingTaskManager;
import kandango.reagenica.world.task.ISeedPlacingData;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SeedPlacingProvider implements ICapabilityProvider {
  private final ISeedPlacingData instance = new SeedPlacingTaskManager();
  private final LazyOptional<ISeedPlacingData> optional = LazyOptional.of(() -> instance);

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == ChemiCapabilities.SEED_PLACING_DATA ? optional.cast() : LazyOptional.empty();
  }
  
}
