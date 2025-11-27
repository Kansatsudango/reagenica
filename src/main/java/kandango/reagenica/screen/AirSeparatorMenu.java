package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.AirSeparatorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AirSeparatorMenu extends ChemistryMenu<AirSeparatorBlockEntity> {
  public AirSeparatorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(AirSeparatorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public AirSeparatorMenu(int id, Inventory inv, AirSeparatorBlockEntity be){
    super(ModMenus.AIR_SEPARATOR_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public FluidStack getNitrogen(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getNitrogen()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getOxygen(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getOxygen()).orElse(FluidStack.EMPTY);
  }
  public int getCapacity(){
    return 10000;
  }

  @Override
  protected void internalSlots(AirSeparatorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 30, 31));
    this.addSlot(new SlotItemHandler(handler, 1, 94, 30));
    this.addSlot(new SlotItemHandler(handler, 2, 94, 62));
    this.addSlot(new SlotItemHandler(handler, 3, 136, 30));
    this.addSlot(new SlotItemHandler(handler, 4, 136, 62));
  }

  @Override
  protected int slotCount() {
    return 5;
  }
}
