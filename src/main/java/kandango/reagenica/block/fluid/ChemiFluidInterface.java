package kandango.reagenica.block.fluid;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;

public interface ChemiFluidInterface {
  public FluidType getType();
  public Fluid getFluid();
  public Fluid getFlowingFluid();
  public LiquidBlock getBlock();
}
