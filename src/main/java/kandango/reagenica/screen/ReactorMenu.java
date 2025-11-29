package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.ReactorBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ReactorMenu extends ChemistryMenu<ReactorBlockEntity> {
  public ReactorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(ReactorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public ReactorMenu(int id, Inventory inv, ReactorBlockEntity be){
    super(ModMenus.REACTOR_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.isSCRAMed() ? 1 : 0;}
      @Override
      public void set(int value) {be.setSCRAMed(value!=0);}
    });
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer,9)
    );
    return rules;
  }

  public FluidStack getFluidMain(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTankMain().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getFluidHeat(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTankHeat().getFluid()).orElse(FluidStack.EMPTY);
  }
  
  public int getTankCapacity(){
    return 4000;
  }
  public boolean isSCRAMed(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.isSCRAMed()).orElse(true);
  }

  @Override
  protected void internalSlots(ReactorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 62, 55));
    this.addSlot(new SlotItemHandler(handler, 1, 80, 55));
    this.addSlot(new SlotItemHandler(handler, 2, 98, 55));
    this.addSlot(new SlotItemHandler(handler, 3, 62, 73));
    this.addSlot(new SlotItemHandler(handler, 4, 80, 73));
    this.addSlot(new SlotItemHandler(handler, 5, 98, 73));
    this.addSlot(new SlotItemHandler(handler, 6, 62, 91));
    this.addSlot(new SlotItemHandler(handler, 7, 80, 91));
    this.addSlot(new SlotItemHandler(handler, 8, 98, 91));
    this.addSlot(new SlotItemHandler(handler, 9, 22, 34));
    this.addSlot(new SlotItemHandler(handler, 10, 22, 66));
  }

  @Override
  protected int slotCount() {
    return 11;
  }

  @Override
  protected int inv_start(){
    return 124;
  }
}
