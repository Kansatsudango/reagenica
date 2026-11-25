package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.FractionalDistillerBlockEntity;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FractionalDistillerMenu extends ChemistryMenu<FractionalDistillerBlockEntity> {
  public FractionalDistillerMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(FractionalDistillerBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public FractionalDistillerMenu(int id, Inventory inv, FractionalDistillerBlockEntity be){
    super(ModMenus.FRACTIONAL_DISTILLER_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
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
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(SlotPriorityPredicates.IsFuel, 0),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 2),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 6),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 4),
      SlotPriorityRule.single(stack -> FluidItemConverter.getFluidstackFromItem(stack).getFluid() == Fluids.WATER, 8)
    );
    return rules;
  }

  public FluidStack getFluidInput(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTankInput().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getFluidTop(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTankTop().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getFluidBottom(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTankBottom().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getFluidWater(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTankWater().getFluid()).orElse(FluidStack.EMPTY);
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public int getBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuel()).orElse(0);
  }
  public int getMaxBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuelMax()).orElse(0);
  }

  @Override
  protected void internalSlots(FractionalDistillerBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 8, 105));
    this.addSlot(new SlotItemHandler(handler, 1, 103, 101));
    this.addSlot(new SlotItemHandler(handler, 2, 8, 13));
    this.addSlot(new SlotItemHandler(handler, 3, 34, 39));
    this.addSlot(new SlotItemHandler(handler, 4, 148, 12));
    this.addSlot(new SlotItemHandler(handler, 5, 148, 37));
    this.addSlot(new SlotItemHandler(handler, 6, 148, 73));
    this.addSlot(new SlotItemHandler(handler, 7, 148, 98));
    this.addSlot(new SlotItemHandler(handler, 8, 62, 13));
    this.addSlot(new SlotItemHandler(handler, 9, 62, 38));
  }

  @Override
  protected int slotCount() {
    return 10;
  }

  @Override
  protected int inv_start(){
    return 124;
  }
}
