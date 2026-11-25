package kandango.reagenica.screen;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.recipes.ReagenimartRecipe;
import kandango.reagenica.recipes.ReagenimartRecipe.ReagenimartCategory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ComputerMenu extends AbstractContainerMenu{
  private final Container insideSlot = new SimpleContainer(2);
  private ReagenimartRecipe selected = null;
  private final Map<ReagenimartCategory,List<ReagenimartRecipe>> trades;
  private final Level level;

  public ComputerMenu(int id, Inventory playerInv){
    super(ModMenus.COMPUTER_MENU.get(), id);
    this.addSlot(new Slot(insideSlot, 0, 105, 66){
      @Override
      public void setChanged(){
        super.setChanged();
        updateResultSlot();
      }
    });
    this.addSlot(new Slot(insideSlot, 1, 144, 66){
      @Override
      public boolean mayPlace(@Nonnull ItemStack stack){
        return false;
      }
      @Override
      public void onTake(@Nonnull Player player, @Nonnull ItemStack stack){
        super.onTake(player, stack);
        if(!player.level().isClientSide){
          buy();
        }
      }
    });
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 110 + row * 18));
      }
    }
    for (int col = 0; col < 9; ++col) {
      this.addSlot(new Slot(playerInv, col, 8 + col * 18, 168));
    }
    this.level = playerInv.player.level();
    if(level==null)throw new IllegalStateException("Player Level was null");
    trades = Map.of(
      ReagenimartCategory.BUILDING, ReagenimartRecipe.getRecipeCategorized(level, ReagenimartCategory.BUILDING),
      ReagenimartCategory.COMMODITY, ReagenimartRecipe.getRecipeCategorized(level, ReagenimartCategory.COMMODITY),
      ReagenimartCategory.MINERALS, ReagenimartRecipe.getRecipeCategorized(level, ReagenimartCategory.MINERALS),
      ReagenimartCategory.NATURALS, ReagenimartRecipe.getRecipeCategorized(level, ReagenimartCategory.NATURALS)
    );
  }

  @Override
  public void removed(@Nonnull Player player){
    super.removed(player);
    this.clearContainer(player, new SimpleContainer(insideSlot.getItem(0)));
  }

  @Override
  public ItemStack quickMoveStack(@Nonnull Player player, int index) {
    if (level.isClientSide) return ItemStack.EMPTY;//Client is not 
    ItemStack originalStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if(slot != null && slot.hasItem()){
      ItemStack currentStack = slot.getItem();
      originalStack = currentStack.copy();
      final int containerSlotCount = 2;
      final int playerInventoryStart = 2;
      final int playerInventoryEnd = playerInventoryStart + 27;
      final int hotbarStart = playerInventoryEnd;
      final int hotbarEnd = hotbarStart + 9;
      if(index < containerSlotCount){
        if(index==1){
          while (true) {
            if (!ItemStackUtil.isEnough(insideSlot.getItem(0), selected.getPrice())) break;
            
            ItemStack result = selected.getMerchandise().copy();
            if (this.moveItemStackTo(result, 2, this.slots.size(), true)) {
              buy();
            }else{
              break;
            }
          }
          currentStack=ItemStack.EMPTY;
        }
        if(!moveItemStackTo(currentStack, playerInventoryStart, hotbarEnd, true)){
          return ItemStack.EMPTY;
        }
      }else{
        if(!moveItemStackTo(currentStack, 0, containerSlotCount, false)){
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
  public boolean stillValid(@Nonnull Player player) {
    return true;
  }
  
  private boolean buy(){
    if(selected==null){
      ChemistryMod.LOGGER.error("Selected merchandise was null when buying: ",new IllegalStateException());
      return false;
    }
    ItemStack payment = insideSlot.getItem(0).copy();
    if(!ItemStackUtil.isEnough(payment, selected.getPrice())){
      return false;
    }
    payment.shrink(selected.getPrice().getCount());
    insideSlot.setItem(0, payment);
    updateResultSlot();
    return true;
  }
  public List<ReagenimartRecipe> recipes(ReagenimartCategory cat){
    return Objects.requireNonNull(trades.get(cat));
  }

  public void receiveClickAction(int clickid){
    if(clickid==-1){
      selected=null;
    }else{
      int index = clickid&0xffff;
      try{
        ReagenimartCategory cat = ReagenimartCategory.values()[clickid>>16];
        selected = this.recipes(cat).get(index);
      }catch(ArrayIndexOutOfBoundsException e){// Only happens when packet is falsified, 
        ChemistryMod.LOGGER.warn("Invalid trade id category packet recieved,"+clickid, new SecurityException());
      }catch(IndexOutOfBoundsException e){
        ChemistryMod.LOGGER.warn("Invalid trade id index packet recieved,"+clickid, new SecurityException());
      }
    }
    updateResultSlot();
  }

  private void updateResultSlot(){
    if(selected==null){
      insideSlot.setItem(1, ItemStack.EMPTY);
    }else{
      if(selected.isValidPayment(insideSlot.getItem(0))){
        insideSlot.setItem(1, selected.getMerchandise().copy());
      }else{
        insideSlot.setItem(1, ItemStack.EMPTY);
      }
    }
  }
}
