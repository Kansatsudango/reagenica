package kandango.reagenica.item;

import java.util.List;

import kandango.reagenica.block.CableAbstract;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class CableItem extends BlockItem{
  private final double resistance;
  private final int restriction;

  public CableItem(CableAbstract cable, Properties props, double resistrance, int restriction) {
    super(cable, props);
    this.resistance = resistrance;
    this.restriction = restriction;
  }
  
  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(Component.translatable("tooltip.reagenica.resistance", resistance).withStyle(ChatFormatting.GRAY));
    tooltipComponents.add(Component.translatable("tooltip.reagenica.restriction", restriction).withStyle(ChatFormatting.GRAY));
  }
}
