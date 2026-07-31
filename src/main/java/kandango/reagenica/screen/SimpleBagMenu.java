package kandango.reagenica.screen;

import java.util.UUID;

import javax.annotation.Nonnull;

import kandango.reagenica.item.CommonBag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SimpleBagMenu extends ChemistryMenuSimple {
  private final IItemHandlerModifiable handler;
  private final int inv_start;
  private final int slotid;
  private final UUID bagID;

  public SimpleBagMenu(MenuType<?> menu, int inv_start, int id, Inventory playerInv, int slotid, UUID bagID) {
    super(menu, id, playerInv);
    this.inv_start = inv_start;
    this.slotid = slotid;
    ItemStack bag = playerInv.getItem(slotid);
    IItemHandler foundHandler = bag.getCapability(Capabilities.ItemHandler.ITEM);
    if(foundHandler instanceof IItemHandlerModifiable modifiable){
      this.handler = modifiable;
    }else{
      throw new IllegalStateException("Bag item handler was not found or was not modifiable.");
    }
    for (int s = 0; s < handler.getSlots(); s++) {
      this.addSlot(new SlotItemHandler(handler, s, 8 + (s%9) * 18, 18 + (s/9) * 18));
    }
    this.bagID = bagID;
    initSlots(playerInv);
  }

  public SimpleBagMenu(MenuType<?> menu, int inv_start, int id, Inventory playerInv, FriendlyByteBuf buf) {
    this(menu, inv_start, id, playerInv, buf.readVarInt(), buf.readUUID());
  }

  public IItemHandlerModifiable getItemHandler(){
    return handler;
  }
  @Override
  protected Slot createPlayerSlot( Inventory inventory, int inventorySlot, int x, int y) {
    /*
     * Lock Original Bag Slot
     */
    if (inventorySlot == this.slotid) {
      return new Slot(inventory, inventorySlot, x, y) {
          @Override
          public boolean mayPickup(Player player) {
              return false;
          }

          @Override
          public boolean mayPlace(ItemStack stack) {
              return false;
          }
      };
    }

    return super.createPlayerSlot(inventory, inventorySlot, x, y);
  }

  @Override
  public ItemStack quickMoveStack(@Nonnull Player player, int index) {
    if (index == getBagMenuSlotIndex()) {
      return ItemStack.EMPTY;
    }
    return super.quickMoveStack(player, index);
  }

  private int getBagMenuSlotIndex() {
      int playerMenuStart = slotCount();
      if (this.slotid < 9) {
          /*
           * Inventoryの0～8はホットバー。
           * Menuでは通常インベントリ27スロットの後ろ。
           */
          return playerMenuStart
                  + 27
                  + this.slotid;
      }
      /*
       * Inventoryの9～35は通常インベントリ。
       * initSlotsでは9から順番に登録している。
       */
      return playerMenuStart
              + this.slotid
              - 9;
  }

  @Override
  public boolean stillValid(@Nonnull Player player) {
    Inventory playerInv = player.getInventory();
    ItemStack bag = playerInv.getItem(slotid);
    return CommonBag.getBagID(bag).map(id -> id.equals(this.bagID)).orElse(false);
  }

  @Override
  protected int slotCount() {
    return this.handler.getSlots();
  }
  protected int inv_start(){
    return inv_start;
  }
  protected int hotbar_start(){
    return inv_start()+58;
  }
}
