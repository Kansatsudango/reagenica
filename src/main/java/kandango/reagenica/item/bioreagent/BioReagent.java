package kandango.reagenica.item.bioreagent;

import java.util.List;

import kandango.reagenica.ChemiComponents;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.utils.ComponentUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class BioReagent extends BioPlate{
  public final BioReagentTypes type;
  public BioReagent(BioReagentTypes type, int color){
    super(color);
    this.type=type;
  }

  public static void setStats(ItemStack stack, int speed, boolean sterile) {
    stack.set(ChemiComponents.EFFICIENCY, (byte)speed);
    stack.set(ChemiComponents.STERILE, sterile);
  }
  public static ItemStack getPlate(ItemStack stack, int speed, boolean sterile) {
    setStats(stack, speed, sterile);
    return stack;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(Component.translatable("tooltip.reagenica.scientific."+type.name()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
    final int speed = getSpeed(stack);
    if(!(stack.getItem() == ChemiItems.CONTAMINATED_PLATE.get())){
      if(speed<30){
        tooltipComponents.add(Component.literal("Efficiency: " + speed).withStyle(ChatFormatting.GRAY));
      }else{
        tooltipComponents.add(ComponentUtil.rainbowLine("Efficiency: MAX", context.level(), 15, 80));
      }
    }
  }
}
