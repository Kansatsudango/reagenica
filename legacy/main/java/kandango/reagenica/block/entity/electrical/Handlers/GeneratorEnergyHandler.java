package kandango.reagenica.block.entity.electrical.Handlers;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class GeneratorEnergyHandler implements IEnergyStorage{
  private final EnergyStorage storage;
  public GeneratorEnergyHandler(EnergyStorage storage){
    this.storage = storage;
  }
  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return 0;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    return storage.extractEnergy(maxExtract, simulate);
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
    return true;
  }

  @Override
  public boolean canReceive() {
    return false;
  }
  
}
