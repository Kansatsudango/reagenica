package kandango.reagenica.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class MineWipe extends Item{

  public MineWipe() {
    super(new Item.Properties().stacksTo(64));
  }
  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.minewipe_lore").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("tooltip.reagenica.minewipe_lore_warn").withStyle(ChatFormatting.YELLOW));
  }
}
