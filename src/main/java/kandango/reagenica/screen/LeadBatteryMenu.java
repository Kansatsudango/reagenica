package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.LeadBatteryBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class LeadBatteryMenu extends ChemistryMenu<LeadBatteryBlockEntity> {
  public LeadBatteryMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(LeadBatteryBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public LeadBatteryMenu(int id, Inventory inv, LeadBatteryBlockEntity be){
    super(ModMenus.LEAD_BATTERY_MENU.get(),id,inv,be);
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
  protected void internalSlots(LeadBatteryBlockEntity be) {
  }

  @Override
  protected int slotCount() {
    return 0;
  }
}
