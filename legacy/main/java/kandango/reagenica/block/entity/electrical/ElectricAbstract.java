package kandango.reagenica.block.entity.electrical;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ElectricAbstract extends BlockEntity{
  public ElectricAbstract(BlockEntityType<? extends ElectricAbstract> type,BlockPos pos, BlockState state){
    super(type,pos,state);
  }

  abstract protected ElectricStorage energyStorageProvider();
}
