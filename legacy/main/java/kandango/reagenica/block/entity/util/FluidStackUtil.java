package kandango.reagenica.block.entity.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidStackUtil {
  public static boolean isEnoughFluid(FluidStack is, FluidStack forthisfluid){
    if(forthisfluid.isEmpty())return true;
    return is.isFluidEqual(forthisfluid) && is.getAmount() >= forthisfluid.getAmount();
  }
  public static boolean isEnoughFluidinTank(FluidTank is, FluidStack forthisfluid){
    if(is.isEmpty()){
      return forthisfluid.isEmpty();
    }
    else return isEnoughFluid(is.getFluid(), forthisfluid);
  }
  public static boolean canFullyInsertToTank(FluidStack is, FluidTank tank){
    if(is.isEmpty()) return true;
    FluidStack tankfluid = tank.getFluid();
    int subjectamount = is.getAmount();
    int tankcapacity = tank.getCapacity();
    if(tankfluid.isEmpty()){
      return subjectamount <= tankcapacity;
    }
    int tankfluidamount = tankfluid.getAmount();
    if(tankfluid.isFluidEqual(is)){
      return tankfluidamount + subjectamount <= tankcapacity;
    }
    return false;
  }
  public static void saveFluid(CompoundTag tag, String name, FluidTank tank){
    CompoundTag fluidTag = new CompoundTag();
    tank.writeToNBT(fluidTag);
    tag.put(name,fluidTag);
  }
}
