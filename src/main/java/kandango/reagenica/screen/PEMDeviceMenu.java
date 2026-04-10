package kandango.reagenica.screen;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.PEMDeviceBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PEMDeviceMenu extends ChemistryMenu<PEMDeviceBlockEntity> {
  final FluidTank waterTank = new FluidTank(PEMDeviceBlockEntity.CAPACITY_UNIT);
  final FluidTank hydrogenTank = new FluidTank(PEMDeviceBlockEntity.CAPACITY_UNIT);
  final FluidTank oxygenTank = new FluidTank(PEMDeviceBlockEntity.CAPACITY_UNIT);
  public PEMDeviceMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(PEMDeviceBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public PEMDeviceMenu(int id, Inventory inv, PEMDeviceBlockEntity be){
    super(ModMenus.PEM_DEVICE_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getEnergy();}
      @Override
      public void set(int value) {be.setEnergy(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getProgress();}
      @Override
      public void set(int value) {be.setProgress(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getHydrogen().getAmount();}
      @Override
      public void set(int value) {setTank(ChemiFluids.HYDROGEN.getFluid(), value, hydrogenTank);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getOxygen().getAmount();}
      @Override
      public void set(int value) {setTank(ChemiFluids.OXYGEN.getFluid(), value, oxygenTank);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getWater().getAmount();}
      @Override
      public void set(int value) {setTank(ChemiFluids.DISTILLED_WATER.getFluid(), value, waterTank);}
    });
  }
  private void setTank(Fluid fluidType, int amount, FluidTank tank){
    tank.setFluid(new FluidStack(fluidType, amount));
  }

  public int getEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
  }
  public int getMaxEnergy(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public EnergyStorage getEnergyStorage(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> (EnergyStorage)x.getElectricStorage(0)).orElseGet(() -> new EnergyStorage(1000));
  }

  @Override
  protected void internalSlots(PEMDeviceBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 8, 30));
    this.addSlot(new SlotItemHandler(handler, 1, 8, 62));
    this.addSlot(new SlotItemHandler(handler, 2, 93, 30));
    this.addSlot(new SlotItemHandler(handler, 3, 93, 62));
    this.addSlot(new SlotItemHandler(handler, 4, 136, 30));
    this.addSlot(new SlotItemHandler(handler, 5, 136, 62));
  }

  @Override
  protected int slotCount() {
    return 6;
  }
}
