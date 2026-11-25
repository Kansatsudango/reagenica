package kandango.reagenica.block.entity.util;

import java.util.Random;

import kandango.reagenica.item.FluidJar;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

public class DestroyHelper {
  public static void dropItems(IItemHandler handler, Level lv, BlockPos pos){
    for(int i=0;i < handler.getSlots();i++){
      ItemStack stack = handler.getStackInSlot(i);
      if(!stack.isEmpty()){
        Containers.dropItemStack(lv, pos.getX(), pos.getY(), pos.getZ(), stack);
      }
    }
  }

  public static void dropFluid(FluidStack stack, Level lv, BlockPos pos){
    int amount = stack.getAmount();
    Random rand = new Random();
    int rate = 60+rand.nextInt(40);
    amount = amount*rate/100;
    while (amount > 0) {
      ItemStack item;
      if(amount>=1000){
        item = FluidJar.getItemStack(new FluidStack(stack.getFluid(), 1000));
        amount-=1000;
      }else{
        item = FluidJar.getItemStack(new FluidStack(stack.getFluid(), amount));
        amount=0;
      }
      Containers.dropItemStack(lv, pos.getX(), pos.getY(), pos.getZ(), item);
    }
  }

  public static void dropFluidLargeAmount(FluidStack stack, Level lv, BlockPos pos){
    int amount = stack.getAmount();
    Random rand = new Random();
    int rate = 60+rand.nextInt(40);
    amount = amount*rate/100;
    while (amount > 0) {
      ItemStack item;
      if(amount>=10000){
        item = FluidJar.getItemStack(new FluidStack(stack.getFluid(), 10000));
        amount-=10000;
      }else{
        item = FluidJar.getItemStack(new FluidStack(stack.getFluid(), amount));
        amount=0;
      }
      Containers.dropItemStack(lv, pos.getX(), pos.getY(), pos.getZ(), item);
    }
  }
}
