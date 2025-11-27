package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.CookingPotBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CookingPotMenu extends ChemistryMenu<CookingPotBlockEntity> {
  public CookingPotMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(CookingPotBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public CookingPotMenu(int id, Inventory inv, CookingPotBlockEntity be){
    super(ModMenus.COOKING_POT_MENU.get(),id,inv,be);
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
      public int get() {return be.getMaxFuel();}
      @Override
      public void set(int value) {be.setMaxFuel(value);}
    });
  }

  public int getBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuel()).orElse(0);
  }
  public int getMaxBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxFuel()).orElse(0);
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(stack -> stack.is(Items.BOWL),7),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFuel, 6)
    );
    return rules;
  }

  @Override
  protected void internalSlots(CookingPotBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 27, 17));
    this.addSlot(new SlotItemHandler(handler, 1, 45, 17));
    this.addSlot(new SlotItemHandler(handler, 2, 63, 17));
    this.addSlot(new SlotItemHandler(handler, 3, 27, 35));
    this.addSlot(new SlotItemHandler(handler, 4, 45, 35));
    this.addSlot(new SlotItemHandler(handler, 5, 63, 35));
    this.addSlot(new SlotItemHandler(handler, 6, 28, 60));
    this.addSlot(new SlotItemHandler(handler, 7, 120, 59));
    this.addSlot(new SlotItemHandler(handler, 8, 87, 59));
    this.addSlot(new SlotItemHandler(handler, 9, 120, 33));
  }

  @Override
  protected int slotCount() {
    return 10;
  }
}
