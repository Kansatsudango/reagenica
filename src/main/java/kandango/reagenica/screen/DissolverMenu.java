package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.DissolverBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DissolverMenu extends ChemistryMenu<DissolverBlockEntity> {
  public DissolverMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(DissolverBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public DissolverMenu(int id, Inventory inv, DissolverBlockEntity be){
    super(ModMenus.DISSOLVER_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
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
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 2),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 4)
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

  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }

  @Override
  protected void internalSlots(DissolverBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 70, 29));
    this.addSlot(new SlotItemHandler(handler, 1, 95, 63));
    this.addSlot(new SlotItemHandler(handler, 2, 23, 30));
    this.addSlot(new SlotItemHandler(handler, 3, 23, 62));
    this.addSlot(new SlotItemHandler(handler, 4, 136, 30));
    this.addSlot(new SlotItemHandler(handler, 5, 136, 62));
  }

  @Override
  protected int slotCount() {
    return 6;
  }
}
