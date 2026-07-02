package kandango.reagenica.screen;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.AutoExperimenterBlockEntity;
import kandango.reagenica.screen.slots.GhostSlot;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import kandango.reagenica.screen.slots.WindowSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

public class AutoExperimenterMenu extends ChemistryMenu<AutoExperimenterBlockEntity> {
  private boolean recipeRequireHeat;
  private boolean heating;
    
  public AutoExperimenterMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(AutoExperimenterBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public AutoExperimenterMenu(int id, Inventory inv, AutoExperimenterBlockEntity be){
    super(ModMenus.AUTO_EXPERIMENTER_MENU.get(),id, inv, be);
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
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getHeatStatus();}
      @Override
      public void set(int value) {
        recipeRequireHeat = (value&1)!=0;
        heating = (value&2)!=0;
      }
    });
  }

  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public EnergyStorage getEnergyStorage(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> (EnergyStorage)x.getElectricStorage(0)).orElseGet(() -> new EnergyStorage(1000));
  }
  public boolean isRecipeBurnerOn(){
    return recipeRequireHeat;
  }
  public boolean isHeating(){
    return heating;
  }

  protected ItemStack quickMoveStackFunc(int slotcount, @Nonnull Player player, int index, List<SlotPriorityRule> rules) {
    ItemStack originalStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if(slot != null && slot.hasItem() && slot.mayPickup(player)){
      ItemStack currentStack = slot.getItem();
      originalStack = currentStack.copy();
      int containerSlotCount = slotcount;
      int playerInventoryStart = containerSlotCount;
      int playerInventoryEnd = playerInventoryStart + 27;
      int hotbarStart = playerInventoryEnd;
      int hotbarEnd = hotbarStart + 9;
      if(index < containerSlotCount){
        if(!moveItemStackTo(currentStack, playerInventoryStart, hotbarEnd, true)){
          return ItemStack.EMPTY;
        }
      }else{
        boolean moved = false;
        for(SlotPriorityRule rule : rules){
          if(rule.matcher().test(currentStack)){
            moved = moveItemStackTo(currentStack, rule.startSlot(), rule.endSlot(), false);
            if(moved)break;
          }
        }
        if(!moved && !moveItemStackTo(currentStack, 5, containerSlotCount, false)){
          return ItemStack.EMPTY;
        }
      }
      if(currentStack.isEmpty()){
        slot.set(ItemStack.EMPTY);
      }else{
        slot.setChanged();
      }
    }
    return originalStack;
  }
  @Override
  public void clicked(int slotId, int button, ClickType clickType, Player player) {
    if(0<=slotId && slotId<this.slots.size()){
      Slot slot = this.slots.get(slotId);
      if (slot instanceof GhostSlot ghost) {
        ItemStack carried = getCarried();
        if (!carried.isEmpty() && ghost.isValidItem(carried)) {
          ItemStack stackInSlot = ghost.getItem();
          if(stackInSlot.isEmpty()){
            ghost.set(ItemHandlerHelper.copyStackWithSize(carried, 1));
          }else if(ItemStack.isSameItemSameTags(carried, stackInSlot)){
            if(stackInSlot.getCount()+1 <= stackInSlot.getMaxStackSize()){
              stackInSlot.grow(1);
              ghost.setChanged();
            }
          }
        } else {
          ghost.set(ItemStack.EMPTY);
        }
        return;
      }else if(slot instanceof WindowSlot){
        return;
      }
    }

    super.clicked(slotId, button, clickType, player);
  }

  @Override
  protected void internalSlots(AutoExperimenterBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new GhostSlot(handler, 0, 46, 12));
    this.addSlot(new GhostSlot(handler, 1, 46, 48));
    this.addSlot(new GhostSlot(handler, 2, 87, 50));
    this.addSlot(new WindowSlot(handler, 3, 108, 13));
    this.addSlot(new WindowSlot(handler, 4, 108, 31));
    this.addSlot(new SlotItemHandler(handler, 5, 17, 74));
    this.addSlot(new SlotItemHandler(handler, 6, 35, 74));
    this.addSlot(new SlotItemHandler(handler, 7, 53, 74));
    this.addSlot(new SlotItemHandler(handler, 8, 125, 74));
    this.addSlot(new SlotItemHandler(handler, 9, 143, 74));
    for(int i=0;i<9;i++){
      this.addSlot(new SlotItemHandler(handler, 10+i, 8+18*i, 93));
    }
  }

  @Override
  protected int slotCount() {
    return 19;
  }
  
  protected int inv_start(){
    return 124;
  }
  
}
