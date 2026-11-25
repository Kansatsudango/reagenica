package kandango.reagenica.block.entity.util;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.item.FluidJar;
import kandango.reagenica.item.reagent.Reagent;
import kandango.reagenica.item.reagent.ReagentFluidMap;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class FluidItemConverter {
  private static final int TESTTUBE_VOLUME = 100;
  public static ItemStack getItemstackFromFluid(FluidStack fluid, ItemStack in){
    if(in.getItem() == Items.BUCKET){
      if(fluid.getAmount() < 1000){
        return ItemStack.EMPTY;
      }else{
        return FluidUtil.getFilledBucket(fluid);
      }
    }else if(in.getItem() == ChemiItems.TESTTUBE.get()){
      if(fluid.getAmount() < TESTTUBE_VOLUME){
        return ItemStack.EMPTY;
      }else{
        return ReagentFluidMap.getItemfromFluid(fluid.getFluid()).map(item -> new ItemStack(item)).orElse(ItemStack.EMPTY);
      }
    }else{
      return ItemStack.EMPTY;
    }
  }
  public static FluidStack getFluidstackFromItem(ItemStack item){
    if(item.getItem() == ChemiItems.FLUIDJAR.get()){
      return FluidJar.getFluidStack(item);
    }
    return FluidUtil.getFluidContained(item)
      .orElseGet(() -> ReagentFluidMap.getFluidfromItem(item.getItem()).map(fluid -> new FluidStack(fluid, TESTTUBE_VOLUME)).orElse(FluidStack.EMPTY));
  }

  public static ItemStack drainToItemFromTank(FluidTank tank, ItemStack in){
    ItemStack item = FluidItemConverter.getItemstackFromFluid(tank.getFluid(), in);
    if(!item.isEmpty()){
      if(in.getItem() == Items.BUCKET){
        tank.drain(1000, FluidAction.EXECUTE);
      }else if(in.getItem() == ChemiItems.TESTTUBE.get()){
        tank.drain(TESTTUBE_VOLUME, FluidAction.EXECUTE);
      }
    }
    return item;
  }
  public static boolean draintoItem(ItemStackHandler handler, int inputindex, int outputindex, FluidTank tank){
    ItemStack inslot = handler.getStackInSlot(inputindex);
    ItemStack outslot = handler.getStackInSlot(outputindex);
    if(inslot.getItem() == ChemiItems.MINEWIPE.get()){
      if(!tank.isEmpty() && tank.getFluidAmount() < TESTTUBE_VOLUME){
        tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        handler.getStackInSlot(inputindex).shrink(1);
        return true;
      }else{
        return false;
      }
    }
    ItemStack item = FluidItemConverter.getItemstackFromFluid(tank.getFluid(), inslot);
    if(!item.isEmpty() && ItemStackUtil.canAddStack(outslot, item)){
      FluidItemConverter.drainToItemFromTank(tank, inslot);
      handler.setStackInSlot(outputindex, ItemStackUtil.addStack(outslot, item));
      handler.getStackInSlot(inputindex).shrink(1);
      return true;
    }
    return false;
  }
  public static boolean draintoItem(ItemStackHandler handler, int inputindex, FluidTank tank){
    return draintoItem(handler, inputindex, inputindex+1, tank);
  }
  public static boolean drainfromItem(ItemStackHandler handler, int inputindex, int outputindex, FluidTank tank){
    ItemStack inslot = handler.getStackInSlot(inputindex);
    ItemStack outslot = handler.getStackInSlot(outputindex);
    FluidStack fluid = FluidItemConverter.getFluidstackFromItem(inslot);
    if(inslot.getItem() instanceof Reagent && !fluid.isEmpty()){
      int draining = tank.fill(fluid, FluidAction.SIMULATE);
      if(draining == TESTTUBE_VOLUME && ItemStackUtil.canAddStack(outslot.copy(), new ItemStack(ChemiItems.TESTTUBE.get(),1))){
        tank.fill(fluid,FluidAction.EXECUTE);
        inslot.shrink(1);
        handler.setStackInSlot(outputindex, ItemStackUtil.addStack(outslot.copy(), new ItemStack(ChemiItems.TESTTUBE.get(),1)));
        return true;
      }
    }else if(!fluid.isEmpty()){
      if(inslot.getItem() instanceof BucketItem){
        int draining = tank.fill(fluid, FluidAction.SIMULATE);
        if(draining == 1000 && ItemStackUtil.canAddStack(outslot, new ItemStack(Items.BUCKET))){
          tank.fill(fluid, FluidAction.EXECUTE);
          inslot.shrink(1);
          handler.setStackInSlot(outputindex, ItemStackUtil.addStack(outslot.copy(), new ItemStack(Items.BUCKET)));
          return true;
        }
      }else if(inslot.getItem() == ChemiItems.FLUIDJAR.get()){
        if(FluidStackUtil.canFullyInsertToTank(fluid, tank)){
          tank.fill(fluid, FluidAction.EXECUTE);
          inslot.shrink(1);
          return true;
        }
      }
    }
    return false;
  }
  public static boolean drainfromItem(ItemStackHandler handler, int inputindex, FluidTank tank){
    return drainfromItem(handler, inputindex, inputindex+1, tank);
  }
}
