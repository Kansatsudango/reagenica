package kandango.reagenica.block.entity.electrical;

import kandango.reagenica.network.CableNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ElectricCableAbstract extends ElectricAbstract{

  public ElectricCableAbstract(BlockEntityType<? extends ElectricCableAbstract> type,BlockPos pos, BlockState state){
    super(type,pos,state);
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

  public double getResistance(){
    return 0.1;
  }
}
