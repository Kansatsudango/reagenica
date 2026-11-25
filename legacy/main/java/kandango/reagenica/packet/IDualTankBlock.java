package kandango.reagenica.packet;

import net.minecraftforge.fluids.FluidStack;

public interface IDualTankBlock {
  public void receivePacket(FluidStack fluid1, FluidStack fluid2);
}
