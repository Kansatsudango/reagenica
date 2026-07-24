package kandango.reagenica.block;

import java.util.List;

import kandango.reagenica.item.util.CustomNBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class ElectricBlockItem extends BlockItem{
  public ElectricBlockItem(Block block, Properties prop) {
    super(block, prop);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

    int electric = CustomNBTUtil.readData(stack, tag -> tag.getCompound("BlockEntityTag").getCompound("Electric").getInt("Energy")).orElse(0);
    if (electric!=0) {
      tooltipComponents.add(Component.literal("Saved Energy: " + electric + " EU").withStyle(ChatFormatting.YELLOW));
    }
  }
}
