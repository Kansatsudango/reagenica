package kandango.reagenica.item;

import java.util.List;
import java.util.function.Function;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootTable;

public class Amulet extends Item{
  private final Function<RegistryAccess, LootTable> wish;
  public Amulet(Function<RegistryAccess, LootTable> item){
    super(new Item.Properties().stacksTo(1));
    this.wish = item;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(Component.translatable("tooltip.reagenica.amulet").withStyle(ChatFormatting.GRAY));
    //tooltipComponents.add(Component.translatable(wish.get().getDescriptionId()).withStyle(ChatFormatting.GOLD));
  }

  public LootTable getStoredItemStack(RegistryAccess rg){
    return wish.apply(rg);
  }
}
