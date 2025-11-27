package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.HeatGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.fluids.FluidStack;

public class HeatGeneratorMenu extends ChemistryMenu<HeatGeneratorBlockEntity> {
  public HeatGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(HeatGeneratorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public HeatGeneratorMenu(int id, Inventory inv, HeatGeneratorBlockEntity be){
    super(ModMenus.HEAT_GENERATOR_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public FluidStack getFluidInput(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getInputTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidStack getFluidOutput(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getOutputTank().getFluid()).orElse(FluidStack.EMPTY);
  }

  @Override
  protected void internalSlots(HeatGeneratorBlockEntity be) {
  }

  @Override
  protected int slotCount() {
    return 0;
  }
}
