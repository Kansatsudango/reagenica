package kandango.reagenica.block.entity.electrical;

import kandango.reagenica.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricCableCopperBlockEntity extends ElectricCableAbstract{

  public ElectricCableCopperBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.CABLE_COPPER.get(), pos, state);
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(400, 200,200);
  }
  
}
