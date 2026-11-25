package kandango.reagenica.block.entity.itemhandler;

import kandango.reagenica.block.entity.util.ItemStackUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ChemiItemHandler extends ItemStackHandler{
  public ChemiItemHandler(int slots){
    super(slots);
  }

  public int consume(int slot,int count){
    validateSlotIndex(slot);
    ItemStack stack = this.stacks.get(slot).copy();
    int current = stack.getCount();
    if(current>count){
      stack.shrink(count);
      setStackInSlot(slot, stack);
      return count;
    }else{
      setStackInSlot(slot, ItemStack.EMPTY);
      return count;
    }
  }

  public void addStack(int slot,ItemStack stack){
    validateSlotIndex(slot);
    ItemStack merged = ItemStackUtil.addStack(this.getStackInSlot(slot), stack);
    setStackInSlot(slot, merged);
  }
  public boolean addStackifPossible(int slot, ItemStack stack){
    ItemStack stackinhandler = this.getStackInSlot(slot);
    if(ItemStackUtil.canAddStack(stackinhandler, stack)){
      ItemStack merged = ItemStackUtil.addStack(stackinhandler, stack);
      this.setStackInSlot(slot, merged);
      return true;
    }
    return false;
  }
}
