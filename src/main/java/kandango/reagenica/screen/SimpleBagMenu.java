package kandango.reagenica.screen;

import java.util.UUID;

import javax.annotation.Nonnull;

import kandango.reagenica.item.CommonBag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SimpleBagMenu extends ChemistryMenuSimple {
  private final IItemHandler handler;
  private final int inv_start;
  private final int slotid;
  private final UUID bagID;

  public SimpleBagMenu(MenuType<?> menu, int inv_start, int id, Inventory playerInv, int slotid, UUID bagID) {
    super(menu, id, playerInv);
    this.inv_start = inv_start;
    this.slotid = slotid;
    ItemStack bag = playerInv.getItem(slotid);
    this.handler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(() -> new IllegalStateException("Container not found."));
    for (int s = 0; s < handler.getSlots(); s++) {
      this.addSlot(new SlotItemHandler(handler, s, 8 + (s%9) * 18, 18 + (s/9) * 18));
    }
    this.bagID = bagID;
    initSlots(playerInv);
  }

  public SimpleBagMenu(MenuType<?> menu, int inv_start, int id, Inventory playerInv, FriendlyByteBuf buf) {
    this(menu, inv_start, id, playerInv, buf.readVarInt(), buf.readUUID());
  }

  public IItemHandler getItemHandler(){
    return handler;
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
