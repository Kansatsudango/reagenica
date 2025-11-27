package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.CrusherBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrusherMenu extends ChemistryMenu<CrusherBlockEntity> {
    
  public CrusherMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(CrusherBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public CrusherMenu(int id, Inventory inv, CrusherBlockEntity be){
    super(ModMenus.CRUSHER_MENU.get(),id, inv, be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
  }

  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }

  @Override
  protected void internalSlots(CrusherBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 56, 35));
    this.addSlot(new SlotItemHandler(handler, 1, 116, 35));
  }

  @Override
  protected int slotCount() {
    return 2;
  }
}
