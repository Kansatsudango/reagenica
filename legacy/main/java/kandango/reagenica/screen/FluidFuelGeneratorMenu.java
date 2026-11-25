package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.FluidFuelGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FluidFuelGeneratorMenu extends ChemistryMenu<FluidFuelGeneratorBlockEntity> {
  public FluidFuelGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(FluidFuelGeneratorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public FluidFuelGeneratorMenu(int id, Inventory inv, FluidFuelGeneratorBlockEntity be){
    super(ModMenus.GENERATOR_FLUID_FUEL_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.isBurning() ? 1 : 0;}
      @Override
      public void set(int value) {be.setBurning(value!=0);}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public boolean getBurning(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.isBurning()).orElse(false);
  }
  public FluidStack getFuelFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuelFluid().getFluid()).orElse(FluidStack.EMPTY);
  }

  @Override
  protected void internalSlots(FluidFuelGeneratorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 24, 23));
    this.addSlot(new SlotItemHandler(handler, 1, 24, 55));
  }

  @Override
  protected int slotCount() {
    return 2;
  }
}
