package kandango.reagenica.item;

import javax.annotation.Nonnull;

import kandango.reagenica.screen.OnsenDetecterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class OnsenDetecter extends Item{
  public OnsenDetecter(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level lv, @Nonnull Player player, @Nonnull InteractionHand hand) {
    if(!lv.isClientSide && player instanceof ServerPlayer sp){
      NetworkHooks.openScreen(sp, new SimpleMenuProvider(
        (id, inv, p) -> new OnsenDetecterMenu(id, lv, p.blockPosition()),
        Component.translatable("gui.reagenica.onsen_detecter")));
    }
    return InteractionResultHolder.success(player.getItemInHand(hand));
  }
}
