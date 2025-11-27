package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.HaberBoschBlockEntity;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class HaberBoschMenu extends ChemistryMenu<HaberBoschBlockEntity> {
  public HaberBoschMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(HaberBoschBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public HaberBoschMenu(int id, Inventory inv, HaberBoschBlockEntity be){
    super(ModMenus.HABER_BOSCH_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getNitroFluid().getAmount();}
      @Override
      public void set(int value) {be.setNitroAmount(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getHydroFluid().getAmount();}
      @Override
      public void set(int value) {be.setHydroAmount(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getOutputFluid().getAmount();}
      @Override
      public void set(int value) {be.setOutputAmount(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getBurntime();}
      @Override
      public void set(int value) {be.setBurntime(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getMaxBurnTime();}
      @Override
      public void set(int value) {be.setMaxBurnTime(value);}
    });
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 5),
      SlotPriorityRule.single(stack -> FluidItemConverter.getFluidstackFromItem(stack).getFluid().isSame(ChemiFluids.HYDROGEN.getFluid()), 1),
      SlotPriorityRule.single(stack -> FluidItemConverter.getFluidstackFromItem(stack).getFluid().isSame(ChemiFluids.OXYGEN.getFluid()), 3)
    );
    return rules;
  }

  public FluidStack getOutputFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getOutputFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getHydroInputFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getHydroFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getNitroInputFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getNitroFluid()).orElse(FluidStack.EMPTY);
  }
  public int getTankCapacity(){
    return 6000;
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public int getBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getBurntime()).orElse(0);
  }
  public int getMaxBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxBurnTime()).orElse(0);
  }

  @Override
  protected void internalSlots(HaberBoschBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 93, 62));
    this.addSlot(new SlotItemHandler(handler, 1, 29, 30));
    this.addSlot(new SlotItemHandler(handler, 2, 29, 62));
    this.addSlot(new SlotItemHandler(handler, 3, 71, 30));
    this.addSlot(new SlotItemHandler(handler, 4, 71, 62));
    this.addSlot(new SlotItemHandler(handler, 5, 136, 30));
    this.addSlot(new SlotItemHandler(handler, 6, 136, 62));
  }

  @Override
  protected int slotCount() {
    return 7;
  }
}
