package kandango.reagenica.screen.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ChamberInSlot extends SlotItemHandler{
  public ChamberInSlot(IItemHandler itemHandler, int index, int x, int y){
    super(itemHandler,index,x,y);
  }

  @Override
  public boolean mayPlace(ItemStack stack){
    return false;
  }

  @Override
  public boolean mayPickup(Player player){
    return false;
  }
}
