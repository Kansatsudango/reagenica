package kandango.reagenica.villager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

@AutoRegisterCapability
public class VisitCooldownCapability implements ICapabilityProvider,INBTSerializable<CompoundTag>{
  public static final Capability<StallVisitCooldown> STALL_VISIT_COOLDOWN = CapabilityManager.get(new CapabilityToken<>(){});

  private final StallVisitCooldown instance = new StallVisitCooldown();
  private final LazyOptional<StallVisitCooldown> optional = LazyOptional.of(() -> instance);

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == STALL_VISIT_COOLDOWN ? optional.cast() : LazyOptional.empty();
  }


  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    instance.save(tag);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    instance.load(nbt);
  }
  
}
