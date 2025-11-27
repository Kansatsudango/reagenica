package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.HeatFurnaceBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class HeatFurnaceMenu extends ChemistryMenu<HeatFurnaceBlockEntity> {
  public HeatFurnaceMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(HeatFurnaceBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public HeatFurnaceMenu(int id, Inventory inv, HeatFurnaceBlockEntity be){
    super(ModMenus.HEAT_FURNACE_MENU.get(),id,inv,be);
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

  public FluidStack getFluidInput(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getInputTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getFluidOutput(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getOutputTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  
  public int getTankCapacity(){
    return 8000;
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
  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public boolean isUsingEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.isUsingEnergy()).orElse(false);
  }

  @Override
  protected void internalSlots(HeatFurnaceBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 56, 23));
    this.addSlot(new SlotItemHandler(handler, 1, 56, 59));
    this.addSlot(new SlotItemHandler(handler, 2, 109, 24));
    this.addSlot(new SlotItemHandler(handler, 3, 109, 48));
    this.addSlot(new SlotItemHandler(handler, 4, 5, 30));
    this.addSlot(new SlotItemHandler(handler, 5, 5, 62));
    this.addSlot(new SlotItemHandler(handler, 6, 155, 30));
    this.addSlot(new SlotItemHandler(handler, 7, 155, 62));
  }

  @Override
  protected int slotCount() {
    return 8;
  }
}
