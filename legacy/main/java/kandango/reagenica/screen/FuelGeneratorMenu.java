package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.FuelGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FuelGeneratorMenu extends ChemistryMenu<FuelGeneratorBlockEntity> {
  public FuelGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(FuelGeneratorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public FuelGeneratorMenu(int id, Inventory inv, FuelGeneratorBlockEntity be){
    super(ModMenus.GENERATOR_FUEL_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getBurnTime();}
      @Override
      public void set(int value) {be.setBurnTime(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getMaxburntime();}
      @Override
      public void set(int value) {be.setMaxburntime(value);}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public int getBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getBurnTime()).orElse(0);
  }

  public int getMaxburntime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxburntime()).orElse(0);
  }

  @Override
  protected void internalSlots(FuelGeneratorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 56, 42));
  }

  @Override
  protected int slotCount() {
    return 1;
  }
}
