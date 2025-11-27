package kandango.reagenica.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OreBagMenu extends ChemistryMenuSimple {
  private final IItemHandler handler;

  public OreBagMenu(int id, Inventory playerInv, int slotid) {
    super(ModMenus.ORE_BAG_MENU.get(), id, playerInv);
    ItemStack bag = playerInv.getItem(slotid);
    this.handler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(() -> new IllegalStateException("Orebag inventory was null."));
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        this.addSlot(new SlotItemHandler(handler, col + row * 9, 8 + col * 18, 18 + row * 18));
      }
    }
    initSlots(playerInv);
  }

  public OreBagMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
    this(id, playerInv, buf.readVarInt());
  }

  public IItemHandler getItemHandler(){
    return handler;
  }

  @Override
  protected int slotCount() {
    return 27;
  }
}
