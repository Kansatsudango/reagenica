package kandango.reagenica.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FluidJar extends Item{
  public FluidJar(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    CompoundTag tag = stack.getTag();
    if (tag != null && tag.contains("Fluid")) {
      FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid"));
      tooltip.add(Component.literal(fluid.getAmount() + "mb of " + fluid.getDisplayName().getString()));
    } else {
      tooltip.add(Component.literal("Empty"));
    }
  }

  public static ItemStack getItemStack(FluidStack fluid){
    ItemStack stack = new ItemStack(ChemiItems.FLUIDJAR.get());
    CompoundTag tag = new CompoundTag();
    fluid.writeToNBT(tag);
    stack.addTagElement("Fluid", tag);
    return stack;
  }
  public static FluidStack getFluidStack(ItemStack jar){
    CompoundTag tag = jar.getTag();
    if(tag!=null && tag.contains("Fluid")){
      FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid"));
      return fluid;
    }else{
      return FluidStack.EMPTY;
    }
  }
}
