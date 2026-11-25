package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BlastFurnaceItemHandler implements IItemHandler {
  private final ItemStackHandler internal;
  private final Direction side;

  public BlastFurnaceItemHandler(ItemStackHandler internal, Direction side) {
    this.internal = internal;
    this.side = side;
  }

  @Override
  public int getSlots() {
    return internal.getSlots();
  }

  @Override
  public @Nonnull ItemStack getStackInSlot(int slot) {
    // 全スロットを見せても問題ない
    return internal.getStackInSlot(slot);
  }

  @Override
  public @Nonnull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    switch (side) {
      case UP -> {
        return (slot == 0) ? internal.insertItem(0, stack, simulate) : stack;
      }
      case DOWN -> {
        return stack; // 下からは挿入不可
      }
      default -> { // 横方向
        return (slot == 1) ? internal.insertItem(1, stack, simulate) : stack;
      }
    }
  }

  @Override
  public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) {
    return switch (side) {
      case UP -> ItemStack.EMPTY; // 上からは抽出不可
      case DOWN, NORTH, EAST, SOUTH, WEST -> (slot == 2 || slot == 3)
        ? internal.extractItem(slot, amount, simulate)
        : ItemStack.EMPTY;
      default -> ItemStack.EMPTY;
    };
  }

  @Override
  public int getSlotLimit(int slot) {
    return internal.getSlotLimit(slot);
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    // insertItem の判定だけで十分なら、常に true でもOK
    return true;
  }
}
