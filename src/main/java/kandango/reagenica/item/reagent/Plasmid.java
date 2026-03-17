package kandango.reagenica.item.reagent;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class Plasmid extends LiquidReagent{
  public Plasmid(ReagentProperties rp){
    super(rp, new Item.Properties().rarity(Rarity.UNCOMMON));
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    tooltip.add(Component.translatable("tooltip.reagenica.plasmid").withStyle(ChatFormatting.GRAY));
  }
}
