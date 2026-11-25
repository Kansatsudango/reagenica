package kandango.reagenica.item.bioreagent;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BioPlate extends Item{
  private static final String STERILE_KEY = "Sterile";

  public BioPlate(){
    super(new Item.Properties().stacksTo(64));
  }

  protected int getOrDefault(ItemStack stack, String key, int def) {
    CompoundTag tag = stack.getTag();
    return (tag != null && tag.contains(key)) ? tag.getInt(key) : def;
  }
  public static boolean isSterileOf(ItemStack stack){
    CompoundTag tag = stack.getTag();
    return tag!=null && tag.contains(STERILE_KEY) && tag.getBoolean(STERILE_KEY);
  }
  public boolean isSterile(ItemStack stack){
    CompoundTag tag = stack.getTag();
    return tag!=null && tag.contains(STERILE_KEY) && tag.getBoolean(STERILE_KEY);
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    if(isSterile(stack))tooltip.add(Component.translatable("tooltip.reagenica.sterile").withStyle(ChatFormatting.GREEN));
  }
}
