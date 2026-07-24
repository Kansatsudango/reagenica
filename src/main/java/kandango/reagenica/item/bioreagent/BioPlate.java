package kandango.reagenica.item.bioreagent;

import java.util.List;

import kandango.reagenica.ChemiComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class BioPlate extends Item{
  protected final int default_color;
  public BioPlate(int color){
    super(new Item.Properties().stacksTo(64));
    this.default_color = color;
  }

  public static boolean isSterile(ItemStack stack){
    return stack.getOrDefault(ChemiComponents.STERILE, false);
  }
  public void setSterile(ItemStack stack){
    stack.set(ChemiComponents.STERILE, true);
  }
  public int getColor(ItemStack stack){
    return stack.getOrDefault(ChemiComponents.COLOR, default_color);
  }
  public static void setColor(ItemStack stack, int color) {
    stack.set(ChemiComponents.COLOR, color);
  }
  public static int getSpeed(ItemStack stack){
    return (int)stack.getOrDefault(ChemiComponents.EFFICIENCY, 0);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    if(isSterile(stack))tooltipComponents.add(Component.translatable("tooltip.reagenica.sterile").withStyle(ChatFormatting.GREEN));
  }
}
