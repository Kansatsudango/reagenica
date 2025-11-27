package kandango.reagenica.screen.slots;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;

public record SlotPriorityRule(Predicate<ItemStack> matcher, int startSlot, int endSlot) {
  public static SlotPriorityRule single(Predicate<ItemStack> pred, int slot){
    return new SlotPriorityRule(pred,slot,slot+1);
  }
}
