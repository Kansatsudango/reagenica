package kandango.reagenica.screen.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class WindowSlot extends SlotItemHandler{
  public WindowSlot(IItemHandler handler, int index, int x, int y){
    super(handler, index, x, y);
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