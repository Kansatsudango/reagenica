package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.FiltrationDeviceBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FiltrationDeviceMenu extends ChemistryMenu<FiltrationDeviceBlockEntity> {
  private int progress;

  public FiltrationDeviceMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(FiltrationDeviceBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public FiltrationDeviceMenu(int id, Inventory inv, FiltrationDeviceBlockEntity be){
    super(ModMenus.FILTRAION_DEVICE.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {progress = value;}
    });
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(stack -> stack.is(ChemiItems.CARBON_FILTER.get()), 0),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 1),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 3)
    );
    return rules;
  }

  public FluidTank getInputTank(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getInputTank()).orElseGet(() -> new FluidTank(100));
  }
  public FluidTank getOutputTank(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getOutputTank()).orElseGet(() -> new FluidTank(100));
  }
  public int getProgress(){
    return this.progress;
  }

  @Override
  protected void internalSlots(FiltrationDeviceBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 80, 55));
    this.addSlot(new SlotItemHandler(handler, 1, 24, 23));
    this.addSlot(new SlotItemHandler(handler, 2, 24,55));
    this.addSlot(new SlotItemHandler(handler, 3, 136, 23));
    this.addSlot(new SlotItemHandler(handler, 4, 136, 55));
  }

  @Override
  protected int slotCount() {
    return 5;
  }
}
