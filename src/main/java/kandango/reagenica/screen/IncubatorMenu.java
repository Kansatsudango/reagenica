package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.IncubatorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class IncubatorMenu extends ChemistryMenu<IncubatorBlockEntity> {
  public IncubatorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(IncubatorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public IncubatorMenu(int id, Inventory inv, IncubatorBlockEntity be){
    super(ModMenus.INCUBATOR_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }

  @Override
  protected void internalSlots(IncubatorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    for(int i=0;i<3;i++){
      for(int j=0;j<9;j++){
        if(i==2&&j==8)return;
        this.addSlot(new SlotItemHandler(handler, j + i * 9, 8 + j * 18, 18 + i * 18));
      }
    }
  }

  @Override
  protected int slotCount() {
    return 26;
  }
}
