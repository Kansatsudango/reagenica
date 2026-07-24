package kandango.reagenica.item.reagent;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

public class Plasmid extends LiquidReagent{
  public Plasmid(ReagentProperties rp){
    super(rp, new Item.Properties().rarity(Rarity.UNCOMMON));
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    tooltipComponents.add(Component.translatable("tooltip.reagenica.plasmid").withStyle(ChatFormatting.GRAY));
  }
}
