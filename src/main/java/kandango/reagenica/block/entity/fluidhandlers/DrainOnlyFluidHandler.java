package kandango.reagenica.block.entity.fluidhandlers;

import org.jetbrains.annotations.NotNull;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class DrainOnlyFluidHandler implements IFluidHandler{
  private final FluidTank tankin;

  public DrainOnlyFluidHandler(FluidTank tank){
    this.tankin = tank;
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Override
  public @NotNull FluidStack getFluidInTank(int tank) {
    return tankin.getFluid();
  }

  @Override
  public int getTankCapacity(int tank) {
    return tankin.getCapacity();
  }

  @Override
  public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
    return tankin.isFluidValid(stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return 0;//Disable!
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    return tankin.drain(resource, action);
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    return tankin.drain(maxDrain, action);
  }

  
}
