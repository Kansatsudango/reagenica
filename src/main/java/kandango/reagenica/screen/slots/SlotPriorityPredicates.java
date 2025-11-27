package kandango.reagenica.screen.slots;

import java.util.function.Predicate;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;

public class SlotPriorityPredicates {
  public static final Predicate<ItemStack> IsFuel = stack -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) != 0;
  public static final Predicate<ItemStack> IsFluidcase = stack -> stack.getItem() == Items.BUCKET || stack.getItem() == ChemiItems.TESTTUBE.get();
  public static final Predicate<ItemStack> IsFluidContainer = stack -> !FluidItemConverter.getFluidstackFromItem(stack).isEmpty();

  public static Predicate<ItemStack> sameTypeTank(FluidStack fluid){
    return stack -> FluidItemConverter.getFluidstackFromItem(stack).isFluidEqual(fluid);
  }
}
