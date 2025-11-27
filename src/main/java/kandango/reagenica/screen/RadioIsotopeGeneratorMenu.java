package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.RadioIsotopeGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RadioIsotopeGeneratorMenu extends ChemistryMenu<RadioIsotopeGeneratorBlockEntity> {
  public RadioIsotopeGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(RadioIsotopeGeneratorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public RadioIsotopeGeneratorMenu(int id, Inventory inv, RadioIsotopeGeneratorBlockEntity be){
    super(ModMenus.GENERATOR_RI_MENU.get(),id,inv,be);
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
  protected void internalSlots(RadioIsotopeGeneratorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 62, 25));
    this.addSlot(new SlotItemHandler(handler, 1, 80, 25));
    this.addSlot(new SlotItemHandler(handler, 2, 98, 25));
    this.addSlot(new SlotItemHandler(handler, 3, 62, 43));
    this.addSlot(new SlotItemHandler(handler, 4, 80, 43));
    this.addSlot(new SlotItemHandler(handler, 5, 98, 43));
  }

  @Override
  protected int slotCount() {
    return 6;
  }
}
