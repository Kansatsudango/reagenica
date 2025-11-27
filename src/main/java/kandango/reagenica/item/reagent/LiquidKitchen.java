package kandango.reagenica.item.reagent;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

public class LiquidKitchen extends LiquidReagent{
  public LiquidKitchen(ReagentProperties rp, Properties properties){
    super(rp,properties);
  }
  public LiquidKitchen(ReagentProperties rp, Properties properties, Supplier<Fluid> fluid){
    super(rp,properties,fluid);
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    tooltip.add(Component.translatable("tooltip.reagenica.seasoning").withStyle(ChatFormatting.GOLD));
  }
}
