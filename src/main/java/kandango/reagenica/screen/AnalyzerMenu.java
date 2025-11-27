package kandango.reagenica.screen;

import kandango.reagenica.block.entity.AnalyzerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AnalyzerMenu extends ChemistryMenu<AnalyzerBlockEntity> {
  public AnalyzerMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(AnalyzerBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public AnalyzerMenu(int id, Inventory inv, AnalyzerBlockEntity be){
    super(ModMenus.ANALYZER_MENU.get(),id,inv,be);
  }
  
  public AnalyzerBlockEntity getBlockEntity(){
    return blockEntity;
  }

  @Override
  protected void internalSlots(AnalyzerBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    // ブロックのスロット
    this.addSlot(new SlotItemHandler(handler, 0, 56, 35));
    this.addSlot(new SlotItemHandler(handler, 1, 56, 53));
    this.addSlot(new SlotItemHandler(handler, 2, 108, 17));
    this.addSlot(new SlotItemHandler(handler, 3, 108, 35));
    this.addSlot(new SlotItemHandler(handler, 4, 108, 53));
    this.addSlot(new SlotItemHandler(handler, 5, 126, 17));
    this.addSlot(new SlotItemHandler(handler, 6, 126, 35));
    this.addSlot(new SlotItemHandler(handler, 7, 126, 53));
  }

  @Override
  protected int slotCount() {
    return 8;
  }
}
