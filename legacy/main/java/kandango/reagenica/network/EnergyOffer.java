package kandango.reagenica.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record EnergyOffer(BlockPos origin, int strength, int restrict, int cost, Direction dir) {
  public EnergyOffer propagate(int restriction, int additionalcost, Direction direction){
    return new EnergyOffer(this.origin, this.strength-1, Math.min(this.restrict,restriction), this.cost + additionalcost, direction);
  }
}
