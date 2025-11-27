package kandango.reagenica.packet;

import net.minecraftforge.fluids.FluidStack;

public interface ISingleTankBlock {
  public void receivePacket(FluidStack fluid);
}
