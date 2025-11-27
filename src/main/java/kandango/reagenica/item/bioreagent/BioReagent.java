package kandango.reagenica.item.bioreagent;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BioReagent extends Item{
  private static final String SPEED_KEY = "Efficiency";
  private static final String STERILE_KEY = "Sterile";
  protected static final String COLOR_KEY = "Color";

  protected final BioProperties props;
  public BioReagent(BioProperties prop){
    super(new Item.Properties().stacksTo(1).durability(320));
    this.props=prop;
  }
  public BioReagent(BioProperties prop, int stackcount){
    super(new Item.Properties().stacksTo(stackcount));
    this.props=prop;
  }
  public int getColor(ItemStack stack){
    if(props.color()==0){
      return getOrDefault(stack, COLOR_KEY, 0xFFFFFFFF);
    }
    return props.color();
  }

  protected int getOrDefault(ItemStack stack, String key, int def) {
    CompoundTag tag = stack.getTag();
    return (tag != null && tag.contains(key)) ? tag.getInt(key) : def;
  }

  public static void setStats(ItemStack stack, int speed,boolean sterile) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putInt(SPEED_KEY, speed);
    tag.putBoolean(STERILE_KEY, sterile);
  }
  public static void setColor(ItemStack stack, int color) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putInt(COLOR_KEY, color);
  }
  public int getSpeed(ItemStack stack){
    return getOrDefault(stack, SPEED_KEY, 0);
  }
  public boolean isSterile(ItemStack stack){
    CompoundTag tag = stack.getTag();
    return tag!=null && tag.contains(STERILE_KEY) && tag.getBoolean(STERILE_KEY);
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    if(!props.scientific_name().isEmpty())tooltip.add(Component.literal(props.scientific_name()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
    if(!(stack.getItem() == ChemiItems.CONTAMINATED_PLATE.get()))tooltip.add(Component.literal("Efficiency: " + getSpeed(stack)).withStyle(ChatFormatting.GRAY));
    if(isSterile(stack))tooltip.add(Component.translatable("tooltip.reagenica.sterile").withStyle(ChatFormatting.GREEN));
  }

  protected void superAppendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag){
    super.appendHoverText(stack, level, tooltip, flag);
  }
}
