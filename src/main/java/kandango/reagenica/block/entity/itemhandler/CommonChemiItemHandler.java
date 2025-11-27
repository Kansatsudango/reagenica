package kandango.reagenica.block.entity.itemhandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CommonChemiItemHandler implements IItemHandler{
  private final ItemStackHandler handler;
  private final List<SlotPriorityRule> rules;
  private final List<Integer> outputs;

  private CommonChemiItemHandler(ItemStackHandler h,List<SlotPriorityRule> r,List<Integer> o){
    this.handler = h;
    this.rules = r;
    this.outputs = o;
  }

  @Override
  public int getSlots() {// I'm frontdesk. Here, put your inserting item or take my output if it's there.
    return 2;
  }

  @Override
  public @NotNull ItemStack getStackInSlot(int slot) {
    if(slot==0)return extractItem(0, 64, true);
    else return ItemStack.EMPTY;
  }

  @Override
  public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    ItemStack remainedStack = stack.copy();
    Set<Integer> ruledSlots = new HashSet<>();
    for(SlotPriorityRule rule : rules){
      int slotstart = rule.startSlot();
      int slotend = rule.endSlot();
      for(int i=slotstart;i<slotend;i++){
        ruledSlots.add(i);
      }
      if(rule.matcher().test(stack)){
        for(int i = slotstart; i < slotend; i++){
          remainedStack = handler.insertItem(i, remainedStack, simulate);
          if(remainedStack.isEmpty())return ItemStack.EMPTY;
        }
      }
    }
    for(int i=0;i<handler.getSlots();i++){
      if(ruledSlots.contains(i))continue;
      remainedStack = handler.insertItem(i, remainedStack, simulate);
      if(remainedStack.isEmpty())break;
    }
    return remainedStack;
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    for(int outslot : outputs){
      if(!handler.getStackInSlot(outslot).isEmpty()){
        return handler.extractItem(outslot, amount, simulate);
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public int getSlotLimit(int slot) {
    return 64;
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    return true;
  }
  
  public static class Builder{
    private final ItemStackHandler internalhandler;
    private final List<SlotPriorityRule> internalrules = new ArrayList<>();
    private final List<Integer> internaloutputs = new ArrayList<>();
    private Builder(ItemStackHandler handler){
      this.internalhandler = handler;
    }

    public static Builder of(ItemStackHandler handler){
      return new Builder(handler);
    }

    public Builder fuelslot(int i){
      internalrules.add(SlotPriorityRule.single(SlotPriorityPredicates.IsFuel, i));
      return this;
    }
    public Builder anyfluidInputslot(int i){
      internalrules.add(SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, i));
      return this;
    }
    public Builder anyfluidOutputslot(int i){
      internalrules.add(SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, i));
      return this;
    }
    public Builder specificFluidInputSlot(Fluid fluid,int i){
      internalrules.add(SlotPriorityRule.single(stack -> FluidItemConverter.getFluidstackFromItem(stack).getFluid() == fluid, i));
      return this;
    }
    public Builder customInputRule(SlotPriorityRule rule){
      internalrules.add(rule);
      return this;
    }
    public Builder outputslot(int... slots){
      for(int i : slots){
        internaloutputs.add(i);
      }
      return this;
    }
    public CommonChemiItemHandler build(){
      return new CommonChemiItemHandler(internalhandler, internalrules, internaloutputs);
    }
  }
}
