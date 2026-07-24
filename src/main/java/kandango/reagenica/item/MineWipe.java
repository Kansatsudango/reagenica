package kandango.reagenica.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class MineWipe extends Item{

  public MineWipe() {
    super(new Item.Properties().stacksTo(64));
  }
  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(Component.translatable("tooltip.reagenica.minewipe_lore").withStyle(ChatFormatting.GRAY));
    tooltipComponents.add(Component.translatable("tooltip.reagenica.minewipe_lore_warn").withStyle(ChatFormatting.YELLOW));
  }
}
