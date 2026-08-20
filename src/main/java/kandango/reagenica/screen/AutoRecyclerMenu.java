package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.AutoRecyclerBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoRecyclerMenu extends ChemistryMenu<AutoRecyclerBlockEntity> {
  private boolean recipeRequireHeat;
  private boolean heating;
    
  public AutoRecyclerMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(AutoRecyclerBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public AutoRecyclerMenu(int id, Inventory inv, AutoRecyclerBlockEntity be){
    super(ModMenus.AUTO_RECYCLER_MENU.get(),id, inv, be);
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

  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public EnergyStorage getEnergyStorage(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> (EnergyStorage)x.getElectricStorage(0)).orElseGet(() -> new EnergyStorage(1000));
  }
  public FluidTank getTank(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTank()).orElseGet(() -> new FluidTank(1000));
  }
  public boolean isRecipeBurnerOn(){
    return recipeRequireHeat;
  }
  public boolean isHeating(){
    return heating;
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(SlotPriorityPredicates.IsFluidContainer, 8)
    );
    return rules;
  }

  @Override
  protected void internalSlots(AutoRecyclerBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 56, 35));
    this.addSlot(new SlotItemHandler(handler, 1, 56, 53));
    this.addSlot(new SlotItemHandler(handler, 2, 108, 17));
    this.addSlot(new SlotItemHandler(handler, 3, 108, 35));
    this.addSlot(new SlotItemHandler(handler, 4, 108, 53));
    this.addSlot(new SlotItemHandler(handler, 5, 126, 17));
    this.addSlot(new SlotItemHandler(handler, 6, 126, 35));
    this.addSlot(new SlotItemHandler(handler, 7, 126, 53));
    this.addSlot(new SlotItemHandler(handler, 8, 8, 21));
    this.addSlot(new SlotItemHandler(handler, 9, 8, 53));
  }

  @Override
  protected int slotCount() {
    return 10;
  }
  
}
