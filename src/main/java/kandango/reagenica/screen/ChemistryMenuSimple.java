package kandango.reagenica.screen;

import java.util.List;

import javax.annotation.Nonnull;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ChemistryMenuSimple extends AbstractContainerMenu {
  public ChemistryMenuSimple(MenuType<?> type, int id, Inventory inv){
    super(type,id);
  }
  
  @Override
  public boolean stillValid(@Nonnull Player player) {
    return true;
  }

  protected void initSlots(Inventory inv) {
    for (int row = 0; row < 3; ++row) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, inv_start() + row * 18));
        }
    }
    for (int col = 0; col < 9; ++col) {
        this.addSlot(new Slot(inv, col, 8 + col * 18, hotbar_start()));
    }
  }
  protected abstract int slotCount();

  @Override
  public final ItemStack quickMoveStack(@Nonnull Player player, int index) {
    return quickMoveStackFunc(slotCount(), player, index, quickMoveRules());
  }

  protected List<SlotPriorityRule> quickMoveRules(){
    return List.of();
  }
  protected ItemStack quickMoveStackFunc(int slotcount, @Nonnull Player player, int index, List<SlotPriorityRule> rules) {
    ItemStack originalStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if(slot != null && slot.hasItem()){
      ItemStack currentStack = slot.getItem();
      originalStack = currentStack.copy();
      int containerSlotCount = slotcount;
      int playerInventoryStart = containerSlotCount;
      int playerInventoryEnd = playerInventoryStart + 27;
      int hotbarStart = playerInventoryEnd;
      int hotbarEnd = hotbarStart + 9;
      if(index < containerSlotCount){
        if(!moveItemStackTo(currentStack, playerInventoryStart, hotbarEnd, true)){
          return ItemStack.EMPTY;
        }
      }else{
        boolean moved = false;
        for(SlotPriorityRule rule : rules){
          if(rule.matcher().test(currentStack)){
            moved = moveItemStackTo(currentStack, rule.startSlot(), rule.endSlot(), false);
            if(moved)break;
          }
        }
        if(!moved && !moveItemStackTo(currentStack, 0, containerSlotCount, false)){
          return ItemStack.EMPTY;
        }
      }
      if(currentStack.isEmpty()){
        slot.set(ItemStack.EMPTY);
      }else{
        slot.setChanged();
      }
    }
    return originalStack;
  }
  protected int inv_start(){
    return 84;
  }
  protected int hotbar_start(){
    return 142;
  }
}
