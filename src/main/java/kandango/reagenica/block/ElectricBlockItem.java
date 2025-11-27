package kandango.reagenica.block;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ElectricBlockItem extends BlockItem{
  public ElectricBlockItem(Block block, Properties prop) {
    super(block, prop);
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);

    CompoundTag tag = stack.getTagElement("BlockEntityTag");
    if (tag != null) {
      if (tag.contains("Electric")) {
        int energy = tag.getCompound("Electric").getInt("Energy");
        tooltip.add(Component.literal("Saved Energy: " + energy + " EU").withStyle(ChatFormatting.YELLOW));
      }
    }
  }
}
