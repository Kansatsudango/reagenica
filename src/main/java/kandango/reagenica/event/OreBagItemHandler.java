package kandango.reagenica.event;

import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.item.OreBag;
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
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID)
public class OreBagItemHandler {
  @SubscribeEvent
  public static void onItemPickup(EntityItemPickupEvent event){
    Player player = event.getEntity();
    ItemEntity itementity = event.getItem();
    ItemStack item = itementity.getItem();
    if(!item.is(ChemiTags.Items.ORE_BAG_ACCEPT))return;

    Inventory inv = player.getInventory();
    for(ItemStack stack : inv.items){
      if(stack.getItem() instanceof OreBag){
        if(stack.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()){
          IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(() -> new IllegalStateException());
          if(handler instanceof ItemStackHandler itemHandler){
            ItemStack remaining = insertItem(itemHandler, item);
            if(remaining.isEmpty()){
              player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F);
              itementity.discard();
              event.setCanceled(true);
              break;
            }else{
              item.setCount(remaining.getCount());
            }
          }
        }
      }
    }
  }

  private static ItemStack insertItem(ItemStackHandler handler, ItemStack stack){
    ItemStack remaining = stack.copy();
    for(int i=0;i<handler.getSlots();i++){
      remaining = handler.insertItem(i, stack, false);
      if(remaining.isEmpty())break;
    }
    return remaining;
  }
}
