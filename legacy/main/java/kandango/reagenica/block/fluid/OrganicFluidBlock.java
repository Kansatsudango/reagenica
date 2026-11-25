package kandango.reagenica.block.fluid;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class OrganicFluidBlock extends LiquidBlock{
  public OrganicFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties props){
    super(fluid,props);
  }

  @Override
  public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext context) {
    return true;
  }
}
