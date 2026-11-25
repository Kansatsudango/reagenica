package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.ChemicalFermenterBlockEntity;
import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ChemicalFermenterMenu extends ChemistryMenu<ChemicalFermenterBlockEntity> {
  public ChemicalFermenterMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(ChemicalFermenterBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public ChemicalFermenterMenu(int id, Inventory inv, ChemicalFermenterBlockEntity be){
    super(ModMenus.CHEMICAL_FERMENTER_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getprogress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 8),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 6),
      SlotPriorityRule.single(stack -> stack.getItem() instanceof BioReagent, 4)
    );
    return rules;
  }

  public FluidStack getFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getInputFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getInputFluidTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  
  public int getTankCapacity(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTank().getCapacity()).orElse(0);
  }

  @Override
  protected void internalSlots(ChemicalFermenterBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 47, 18));
    this.addSlot(new SlotItemHandler(handler, 1, 65, 18));
    this.addSlot(new SlotItemHandler(handler, 2, 47, 36));
    this.addSlot(new SlotItemHandler(handler, 3, 65, 36));
    this.addSlot(new SlotItemHandler(handler, 4, 90, 18));
    this.addSlot(new SlotItemHandler(handler, 5, 110, 63));
    this.addSlot(new SlotItemHandler(handler, 6, 5, 23));
    this.addSlot(new SlotItemHandler(handler, 7, 5, 46));
    this.addSlot(new SlotItemHandler(handler, 8, 150, 23));
    this.addSlot(new SlotItemHandler(handler, 9, 150, 46));
  }

  @Override
  protected int slotCount() {
    return 10;
  }
}
