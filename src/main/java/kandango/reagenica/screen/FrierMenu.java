package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.FrierBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FrierMenu extends ChemistryMenu<FrierBlockEntity> {
  public FrierMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(FrierBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public FrierMenu(int id, Inventory inv, FrierBlockEntity be){
    super(ModMenus.FRIER_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getFuel();}
      @Override
      public void set(int value) {be.setFuel(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getFuelMax();}
      @Override
      public void set(int value) {be.setFuelMax(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.isUsingEnergy() ? 1 : 0;}
      @Override
      public void set(int value) {be.setUsingEnergy(value!=0);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(SlotPriorityPredicates.IsFuel, 1),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 6),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 4)
    );
    return rules;
  }

  public FluidTank getOilStack(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getOilTank()).orElse(new FluidTank(1000));
  }

  public int getBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuel()).orElse(0);
  }
  public int getMaxBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuelMax()).orElse(0);
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public int getTemperature(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getTemperature()).orElse(0);
  }
  public EnergyStorage getEnergyStorage(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> (EnergyStorage)x.getElectricStorage(0)).orElseGet(() -> new EnergyStorage(1000));
  }
  public boolean isUsingEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.isUsingEnergy()).orElse(false);
  }

  @Override
  protected void internalSlots(FrierBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 11, 16 ));
    this.addSlot(new SlotItemHandler(handler, 1, 42, 47));
    this.addSlot(new SlotItemHandler(handler, 2, 60, 47));
    this.addSlot(new SlotItemHandler(handler, 3, 78, 47));
    this.addSlot(new SlotItemHandler(handler, 4, 44, 87));
    this.addSlot(new SlotItemHandler(handler, 5, 130, 65));
    this.addSlot(new SlotItemHandler(handler, 6, 148, 65));
    this.addSlot(new SlotItemHandler(handler, 7, 130, 40));
    this.addSlot(new SlotItemHandler(handler, 8, 148, 40));
    this.addSlot(new SlotItemHandler(handler, 9, 130, 15));
    this.addSlot(new SlotItemHandler(handler, 10, 148, 15));
    this.addSlot(new SlotItemHandler(handler, 11, 11, 43));
    this.addSlot(new SlotItemHandler(handler, 12, 11, 75));
  }

  @Override
  protected int slotCount() {
    return 13;
  }
  @Override
  protected int inv_start(){
    return 111;
  }
}
