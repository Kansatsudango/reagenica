package kandango.reagenica.block.entity.fluidhandlers;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FillOnlyFluidHandler implements IFluidHandler {
    private final FluidTank targetTank;

    public FillOnlyFluidHandler(FluidTank tank) {
        this.targetTank = tank;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return targetTank.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return targetTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return targetTank.isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return targetTank.fill(resource, action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return FluidStack.EMPTY;
    }
}
