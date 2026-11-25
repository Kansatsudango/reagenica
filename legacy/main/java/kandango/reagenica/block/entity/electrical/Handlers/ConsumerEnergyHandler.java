package kandango.reagenica.block.entity.electrical.Handlers;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ConsumerEnergyHandler implements IEnergyStorage{
  private final EnergyStorage storage;
  public ConsumerEnergyHandler(EnergyStorage storage){
    this.storage = storage;
  }
  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return storage.receiveEnergy(maxReceive, simulate);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    return 0;
  }

  @Override
  public int getEnergyStored() {
    return storage.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored() {
    return storage.getMaxEnergyStored();
  }

  @Override
  public boolean canExtract() {
    return false;
  }

  @Override
  public boolean canReceive() {
    return true;
  }
  
}
