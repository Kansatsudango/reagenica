package kandango.reagenica.screen;

import javax.annotation.Nullable;

import kandango.reagenica.block.entity.StoreManagerComputerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class StoreManagerComputerMenu extends AbstractContainerMenu{
  @Nullable StoreManagerComputerBlockEntity be;

  protected StoreManagerComputerMenu(MenuType<?> type, int id) {
    super(type, id);
  }
  
  public StoreManagerComputerMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(StoreManagerComputerBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public StoreManagerComputerMenu(int id, Inventory inv, StoreManagerComputerBlockEntity be){
    super(ModMenus.STOREMANAGER_COMPUTER_MENU.get(), id);
    this.be = be;
  }

  public StoreManagerComputerBlockEntity getBlockEntity(){
    return this.be;
  }

  public void setNames(String shopName, String companyName){
    be.setNames(shopName, companyName);
  }

  @Override
  public ItemStack quickMoveStack(Player p, int p_38942_) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player p) {
    return true;
  }
  
}
