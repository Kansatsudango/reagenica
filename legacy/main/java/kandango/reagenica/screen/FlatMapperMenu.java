package kandango.reagenica.screen;

import java.util.List;

import kandango.reagenica.block.entity.FlatMapperBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityPredicates;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FlatMapperMenu extends ChemistryMenu<FlatMapperBlockEntity> {
  private boolean isToggleOn=false;
  public FlatMapperMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(FlatMapperBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public FlatMapperMenu(int id, Inventory inv, FlatMapperBlockEntity be){
    super(ModMenus.FLATMAPPER_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.isKeepEmptyBagFlagOn()?1:0;}
      @Override
      public void set(int value) {isToggleOn = (value!=0);}
    });
  }

  public boolean isToggleOn(){
    return this.isToggleOn;
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(stack -> stack.is(Items.BOWL),7),
      SlotPriorityRule.single(SlotPriorityPredicates.IsFuel, 6)
    );
    return rules;
  }

  public void processToggleButton(){
    blockEntity.toggleFlag();
  }

  @Override
  protected void internalSlots(FlatMapperBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 44, 35));
    this.addSlot(new SlotItemHandler(handler, 1, 44, 53));
    this.addSlot(new SlotItemHandler(handler, 2, 96, 35));
  }

  @Override
  protected int slotCount() {
    return 3;
  }
}
