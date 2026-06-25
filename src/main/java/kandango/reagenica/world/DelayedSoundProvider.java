package kandango.reagenica.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kandango.reagenica.world.task.DelayedSoundTaskManager;
import kandango.reagenica.world.task.IDelayedSoundData;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class DelayedSoundProvider implements ICapabilityProvider {
  private final IDelayedSoundData instance = new DelayedSoundTaskManager();
  private final LazyOptional<IDelayedSoundData> optional = LazyOptional.of(() -> instance);

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == ChemiCapabilities.DELAYED_SOUND_DATA ? optional.cast() : LazyOptional.empty();
  }
  
}
