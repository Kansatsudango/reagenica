package kandango.reagenica.block.entity.fluidhandlers;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class SimpleIOFluidHandler implements IFluidHandler{
  private final FluidTank tankin;
  private final FluidTank tankout;

  public SimpleIOFluidHandler(FluidTank tankin, FluidTank tankout){
    this.tankin = tankin;
    this.tankout = tankout;
  }

  @Override
  public int getTanks() {
    return 2;
  }

  @Override
  public @Nonnull FluidStack getFluidInTank(int tank) {
    return getTank(tank).getFluid();
  }

  @Override
  public int getTankCapacity(int tank) {
    return getTank(tank).getCapacity();
  }

  @Override
  public boolean isFluidValid(int tank, FluidStack stack) {
    return getTank(tank).isFluidValid(stack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return tankin.fill(resource, action);
  }

  @Override
  public @Nonnull FluidStack drain(FluidStack resource, FluidAction action) {
    return tankout.drain(resource, action);
  }

  @Override
  public @Nonnull FluidStack drain(int maxDrain, FluidAction action) {
    return tankout.drain(maxDrain, action);
  }

  private FluidTank getTank(int index){
    if(index==0)return this.tankin;
    else if(index==1)return this.tankout;
    else throw new IndexOutOfBoundsException();
  }
}
