package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.HydrogenReductorBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class HydrogenReductorMenu extends ChemistryMenu<HydrogenReductorBlockEntity> {
  public HydrogenReductorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(HydrogenReductorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public HydrogenReductorMenu(int id, Inventory inv, HydrogenReductorBlockEntity be){
    super(ModMenus.HYDROGEN_REDUCTOR_MENU.get(),id,inv,be);
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
      SlotPriorityRule.single(SlotPriorityPredicates.IsFuel, 1),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidcase, 6),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 4)
    );
    return rules;
  }

  public FluidTank getHydrogenTank(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getHydrogenTank()).orElseGet(() -> new FluidTank(100));
  }
  public EnergyStorage getEnergyStorage(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> (EnergyStorage)x.getElectricStorage(0)).orElseGet(() -> new EnergyStorage(1000));
  }

  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }

  @Override
  protected void internalSlots(HydrogenReductorBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 55, 34));
    this.addSlot(new SlotItemHandler(handler, 1, 108, 34));
    this.addSlot(new SlotItemHandler(handler, 2, 108,59));
    this.addSlot(new SlotItemHandler(handler, 3, 5, 30));
    this.addSlot(new SlotItemHandler(handler, 4, 5, 62));
  }

  @Override
  protected int slotCount() {
    return 5;
  }
}
