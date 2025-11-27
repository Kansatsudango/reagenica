package kandango.reagenica.screen;

import kandango.reagenica.block.entity.TradingStallBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TradingStallMenu extends ChemistryMenu<TradingStallBlockEntity> {
  public TradingStallMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(TradingStallBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public TradingStallMenu(int id, Inventory inv, TradingStallBlockEntity be){
    super(ModMenus.TRADING_STALL_MENU.get(),id,inv,be);
  }

  @Override
  protected void internalSlots(TradingStallBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    for(int i=0;i<3;i++){
      for(int j=0;j<9;j++){
        this.addSlot(new SlotItemHandler(handler, j + i * 9, 8 + j * 18, 18 + i * 18));
      }
    }
  }

  @Override
  protected int slotCount() {
    return 27;
  }
}
