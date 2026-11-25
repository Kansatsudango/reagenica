package kandango.reagenica.block.entity.electrical;

import java.util.Set;

import javax.annotation.Nonnull;
import kandango.reagenica.block.entity.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ElectricMachineAbstract extends ElectricAbstract implements ITickableBlockEntity{
  public Set<BlockPos> connectedCables;
  protected final ElectricStorage energyStorage;

  public ElectricMachineAbstract(BlockEntityType<? extends ElectricMachineAbstract> type,BlockPos pos, BlockState state){
    super(type,pos,state);
    energyStorage = this.energyStorageProvider();
  }

  abstract protected ElectricStorage energyStorageProvider();

  public int getEnergy(){
    return energyStorage.getEnergyStored();
  }
  public void setEnergy(int e){
    this.energyStorage.setEnergy(e);
  }
  public int getMaxEnergy(){
    return energyStorage.getMaxEnergyStored();
  }
  public int consumeEnergy(int amount){
    return energyStorage.extractEnergy(amount, false);
  }
  public void giveEnergy(int amount){
    energyStorage.setEnergy(energyStorage.getEnergyStored() + amount);
  }
  public int getEnergySpace(){
    return this.getMaxEnergy() - this.getEnergy();
  }
  public int getMaxLegalExtract(){
    return energyStorage.extractEnergy(this.getEnergy(), true);
  }
  
  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    energyStorage.setfromtag(tag.getCompound("Electric"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Electric", energyStorage.serializetotag());
  }

  public ElectricStorage getElectricStorage(int index) {
    return energyStorage;
  }
  @Override
  public void reviveCaps(){//who am I?
    super.reviveCaps();
  }
}
