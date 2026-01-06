package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.ElectroLysisBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElectroLysisMenu extends ChemistryMenu<ElectroLysisBlockEntity> {
  public ElectroLysisMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(ElectroLysisBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public ElectroLysisMenu(int id, Inventory inv, ElectroLysisBlockEntity be){
    super(ModMenus.ELECTROLYSIS_MENU.get(),id,inv,be);
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
      new SlotPriorityRule(stack -> stack.is(ChemiTags.Items.ELECTRODES),0,2),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, ElectroLysisBlockEntity.OUTPUT_FLUID_SLOT),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, ElectroLysisBlockEntity.INPUT_FLUID_SLOT),
      SlotPriorityRule.single(stack -> stack.getItem() == ChemiItems.TESTTUBE.get(), 3),
      SlotPriorityRule.single(stack -> stack.getItem() == ChemiItems.TESTTUBE.get(), 5)
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
    return 4000;
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
  protected void internalSlots(ElectroLysisBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 49, 57));
    this.addSlot(new SlotItemHandler(handler, 1, 87, 57));
    this.addSlot(new SlotItemHandler(handler, 2, 49, 81));
    this.addSlot(new SlotItemHandler(handler, 3, 23, 55));
    this.addSlot(new SlotItemHandler(handler, 4, 23, 32));
    this.addSlot(new SlotItemHandler(handler, 5, 113, 55));
    this.addSlot(new SlotItemHandler(handler, 6, 113, 32));
    this.addSlot(new SlotItemHandler(handler, 7, 153, 51));
    this.addSlot(new SlotItemHandler(handler, 8, 153, 74));
    this.addSlot(new SlotItemHandler(handler, 9, 4, 62));
    this.addSlot(new SlotItemHandler(handler, 10, 4, 85));
    this.addSlot(new SlotItemHandler(handler, 11, 79, 79));
  }

  @Override
  protected int slotCount() {
    return 12;
  }

  @Override
  protected int inv_start(){
    return 110;
  }
}
