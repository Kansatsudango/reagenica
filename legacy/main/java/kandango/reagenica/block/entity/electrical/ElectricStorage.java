package kandango.reagenica.block.entity.electrical;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.EnergyStorage;

public class ElectricStorage extends EnergyStorage{
  protected int energybuffer=0;

  public ElectricStorage(int capacity, int maxTransfer) {
      super(capacity, maxTransfer);
  }
  public ElectricStorage(int capacity, int maxin, int maxout) {
      super(capacity, maxin,maxout);
  }

  public void setEnergy(int energy) {
      this.energy = Math.min(energy, capacity);
  }

  public boolean isFull(){
    return this.energy >= this.capacity;
  }

  public void setfromtag(CompoundTag tag){
    this.energy = tag.getInt("Energy");
  }

  public CompoundTag serializetotag(){
    CompoundTag tag = new CompoundTag();
    tag.putInt("Energy", this.energy);
    return tag;
  }
}
