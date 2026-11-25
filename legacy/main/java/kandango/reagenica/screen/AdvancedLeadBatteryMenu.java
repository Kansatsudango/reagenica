package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.AdvancedLeadBatteryBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class AdvancedLeadBatteryMenu extends ChemistryMenu<AdvancedLeadBatteryBlockEntity> {
  public AdvancedLeadBatteryMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(AdvancedLeadBatteryBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public AdvancedLeadBatteryMenu(int id, Inventory inv, AdvancedLeadBatteryBlockEntity be){
    super(ModMenus.ADVANCED_LEAD_BATTERY_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {// least 16bit
      @Override
      public int get() {return be.getEnergy()&0xFFFF;}
      @Override
      public void set(int value) {be.setEnergy((be.getEnergy() & 0xFFFF0000) | (value & 0xFFFF));}
    });
    this.addDataSlot(new DataSlot() {// most 16bit
      @Override
      public int get() {return (be.getEnergy() >> 16) & 0xFFFF;}
      @Override
      public void set(int value) {be.setEnergy((be.getEnergy() & 0xFFFF) | ((value & 0xFFFF) << 16));}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }

  @Override
  protected void internalSlots(AdvancedLeadBatteryBlockEntity be) {
  }

  @Override
  protected int slotCount() {
    return 0;
  }
}
