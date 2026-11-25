package kandango.reagenica.event;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.item.IBagItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID)
public class OreBagItemHandler {
  @SubscribeEvent
  public static void onItemPickup(EntityItemPickupEvent event){
    final Player player = event.getEntity();
    final ItemEntity itementity = event.getItem();
    final ItemStack item = itementity.getItem();
    Inventory inv = player.getInventory();
    for(ItemStack stack : inv.items){
      if(stack.getItem() instanceof IBagItem bag){
        if(bag.canAutoStock() && bag.isValidItem(item)){
          stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            boolean inserted = putInBag(handler, player, itementity, item);
            if(inserted) event.setCanceled(true);
          });
        }
      }
    }
  }
  private static boolean putInBag(IItemHandler bag, Player player, ItemEntity item, ItemStack stack){
    ItemStack remaining = insertItem(bag, stack);
    if(remaining.isEmpty()){
      player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F);
      item.discard();
      stack.setCount(0);
      return true;
    }else{
      stack.setCount(remaining.getCount());
      return false;
    }
  }

  private static ItemStack insertItem(IItemHandler handler, ItemStack stack){
    ItemStack remaining = stack.copy();
    for(int i=0;i<handler.getSlots();i++){
      ItemStack stackInSlot = handler.getStackInSlot(i);
      if(ItemStack.isSameItemSameTags(remaining, stackInSlot)){
        remaining = handler.insertItem(i, remaining, false);
      }
      if(remaining.isEmpty())return ItemStack.EMPTY;
    }
    for(int i=0;i<handler.getSlots();i++){
      remaining = handler.insertItem(i, remaining, false);
      if(remaining.isEmpty())return ItemStack.EMPTY;
    }
    return remaining;
  }
}
