package kandango.reagenica.packet;

import net.neoforged.neoforge.fluids.FluidStack;

public interface ISingleTankBlock {
  public void receivePacket(FluidStack fluid);
}
