package kandango.reagenica.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CopperTankBlockEntity extends SimpleTankBlockEntity{
  public CopperTankBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.COPPER_TANK.get(),pos,state);
  }
  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.copper_tank");
  }

  @Override
  protected int tankSize() {
    return 32000;
  }
}
