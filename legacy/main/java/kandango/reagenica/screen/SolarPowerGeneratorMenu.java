package kandango.reagenica.screen;

import javax.annotation.Nullable;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.SolarPowerGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.Level;

public class SolarPowerGeneratorMenu extends ChemistryMenu<SolarPowerGeneratorBlockEntity> {
  public SolarPowerGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(SolarPowerGeneratorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public SolarPowerGeneratorMenu(int id, Inventory inv, SolarPowerGeneratorBlockEntity be){
    super(ModMenus.GENERATOR_SOLAR_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.isBlocked()?1:0;}
      @Override
      public void set(int value) {be.setBlocked(value!=0);}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public boolean isBlocked(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.isBlocked()).orElse(false);
  }
  @Nullable public Level getLevel(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getLevel()).orElse(null);
  }

  @Override
  protected void internalSlots(SolarPowerGeneratorBlockEntity be) {
  }

  @Override
  protected int slotCount() {
    return 0;
  }
}
