package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nullable;

import kandango.reagenica.block.entity.electrical.Handlers.ConsumerEnergyHandler;
import kandango.reagenica.network.CableNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class ElectricConsumerAbstract extends ElectricMachineAbstract{
  private final LazyOptional<IEnergyStorage> energyInLazyOptional = LazyOptional.of(() -> new ConsumerEnergyHandler(energyStorage));
  public ElectricConsumerAbstract(BlockEntityType<? extends ElectricConsumerAbstract> type,BlockPos pos, BlockState state){
    super(type,pos,state);
  }
  
  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return energyInLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }
  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    energyInLazyOptional.invalidate();
  }

  @Override
  public void onLoad(){
    if(level instanceof ServerLevel slv){
      CableNetworkManager.requestUpdate(slv, worldPosition);
    }
    super.onLoad();
  }

  @Override
  public void setRemoved(){
    if(level instanceof ServerLevel slv){
      CableNetworkManager.requestUpdate(slv, worldPosition);
    }
    super.setRemoved();
  }
}
