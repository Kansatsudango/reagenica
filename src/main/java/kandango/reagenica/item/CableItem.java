package kandango.reagenica.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.CableAbstract;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CableItem extends BlockItem{
  private final double resistance;
  private final int restriction;

  public CableItem(CableAbstract cable, Properties props, double resistrance, int restriction) {
    super(cable, props);
    this.resistance = resistrance;
    this.restriction = restriction;
  }
  
  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.resistance", resistance).withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("tooltip.reagenica.restriction", restriction).withStyle(ChatFormatting.GRAY));
  }
}
