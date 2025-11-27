package kandango.reagenica.screen;

import java.util.List;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.DraftChamberBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ExperimentMenu extends ChemistryMenu<DraftChamberBlockEntity> {
  public ExperimentMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(DraftChamberBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public ExperimentMenu(int id, Inventory inv, DraftChamberBlockEntity be) {
    super(ModMenus.EXPERIMENT_MENU.get(), id, inv, be);
  }

  public boolean isBurning(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.hasActiveFire()).orElse(false);
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(stack -> stack.getItem() == ChemiItems.ALCHOHOL_LAMP.get(), 11)
    );
    return rules;
  }

  @Override
  protected void internalSlots(DraftChamberBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 10, 19));
    this.addSlot(new SlotItemHandler(handler, 1, 28, 19));
    this.addSlot(new SlotItemHandler(handler, 2, 10, 37));
    this.addSlot(new SlotItemHandler(handler, 3, 28, 37));
    this.addSlot(new SlotItemHandler(handler, 4, 10, 55));
    this.addSlot(new SlotItemHandler(handler, 5, 28, 55));
    this.addSlot(new SlotItemHandler(handler, 6, 149, 14));
    this.addSlot(new SlotItemHandler(handler, 7, 149, 32));
    this.addSlot(new SlotItemHandler(handler, 8, 149, 50));
    this.addSlot(new SlotItemHandler(handler, 9, 56, 17));
    this.addSlot(new SlotItemHandler(handler, 10, 56, 53));
    this.addSlot(new SlotItemHandler(handler, 11, 79, 55));
    this.addSlot(new SlotItemHandler(handler, 12, 97, 55));
  }

  @Override
  protected int slotCount() {
    return 13;
  }
}
