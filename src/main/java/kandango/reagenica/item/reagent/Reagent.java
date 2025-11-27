package kandango.reagenica.item.reagent;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class Reagent extends Item{
  protected final ReagentProperties props;
  public Reagent(ReagentProperties rp,Properties properties){
    super(properties);
    this.props=rp;
  }

  public int getColor(){
    return props.color();
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.toxicity", props.toxic()).withStyle(ChatFormatting.BLUE));
    tooltip.add(Component.translatable("tooltip.reagenica.flamable", props.flamable()).withStyle(ChatFormatting.RED));
    tooltip.add(Component.translatable("tooltip.reagenica.unstable", props.unstable()).withStyle(ChatFormatting.YELLOW));
  }
}
