package kandango.reagenica.block.entity.electrical;

import kandango.reagenica.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricCableGoldBlockEntity extends ElectricCableAbstract{

  public ElectricCableGoldBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.CABLE_GOLD.get(), pos, state);
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(400, 200,200);
  }

  @Override
  public double getResistance() {
    return 0.04;
  }
  @Override
  public int getRestriction() {
    return 80;
  }
}
