package kandango.reagenica.item.bioreagent;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BioGrowingPlate extends BioReagent{
  private static final String PARENT_TYPE = "ParentType";
  private static final String PARENT_SPEED_KEY = "ParentEfficiency";
  private static final String PARENT_VITALITY = "ParentVitality";
  private static final String STERILE_KEY = "Sterile";
  private static final String GROWTH = "Growth";
  public BioGrowingPlate(BioProperties prop){
    super(prop);
  }

  public static void setStats(ItemStack stack, String parent, int parentVitality, int speed, boolean sterile) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString(PARENT_TYPE, parent);
    tag.putInt(PARENT_VITALITY, parentVitality);
    tag.putInt(PARENT_SPEED_KEY, speed);
    tag.putBoolean(STERILE_KEY, sterile);
  }
  public static void setGrowth(ItemStack stack, int growth){
    CompoundTag tag = stack.getOrCreateTag();
    tag.putInt(GROWTH, growth);
    stack.setDamageValue(320-growth*40);
  }
  @Override
  public int getSpeed(ItemStack stack){
    return getOrDefault(stack, PARENT_SPEED_KEY, 0);
  }
  @Override
  public boolean isSterile(ItemStack stack){
    CompoundTag tag = stack.getTag();
    return tag!=null && tag.contains(STERILE_KEY) && tag.getBoolean(STERILE_KEY);
  }
  public String parentType(ItemStack stack){
    CompoundTag tag = stack.getTag();
    return tag!=null ? tag.contains(PARENT_TYPE) ? tag.getString(PARENT_TYPE):"":"";
  }
  public int getGrowth(ItemStack stack){
    return getOrDefault(stack, GROWTH, 0);
  }
  
  @Override
  public int getColor(ItemStack stack){
    if(props.color()==0){
      return getOrDefault(stack, COLOR_KEY, 0xFFFFFFFF & ((0x01000000<<getGrowth(stack))|0xffffff));
    }
    return props.color() & ((0x01000000<<getGrowth(stack))|0xffffff);
  }
  
  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    superAppendHoverText(stack, level, tooltip, flag);
    if(!parentType(stack).isEmpty())tooltip.add(Component.translatable(typeToTranslatable(parentType(stack))).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("tooltip.reagenica.plate_growth", getGrowth(stack)).withStyle(ChatFormatting.GRAY));
    if(isSterile(stack))tooltip.add(Component.translatable("tooltip.reagenica.sterile").withStyle(ChatFormatting.GREEN));
  }
  private String typeToTranslatable(String name){
    Objects.requireNonNull(name);
    if(name.equals("Crude")) return "tooltip.reagenica.crude";
    else if(name.equals("Yeast")) return "tooltip.reagenica.yeast";
    else if(name.equals("Oryzae")) return "tooltip.reagenica.oryzae";
    else if(name.equals("Acetobacter")) return "tooltip.reagenica.acetobacter";
    else return "tooltip.reagenica.unknown";
  }

  public static ItemStack getPlate(String name, int speed, boolean sterile){
    ItemStack stack = new ItemStack(ChemiItems.GROWING_PLATE.get());
    BioGrowingPlate.setStats(stack, name, 320, speed, sterile);
    stack.setDamageValue(320);
    return stack;
  }
  public static ItemStack getPlateFromParent(ItemStack plate, ItemStack parent){
    ItemStack stack = new ItemStack(ChemiItems.GROWING_PLATE.get());
    if(plate.getItem() == ChemiItems.MEDIUM_PLATE.get() && parent.getItem() instanceof BioReagent bio){
      boolean sterile = bio.isSterile(plate);
      String parentType;
      if(parent.getItem() == ChemiItems.YEAST.get())parentType="Yeast";
      else if(parent.getItem() == ChemiItems.ORYZAE.get())parentType="Oryzae";
      else if(parent.getItem() == ChemiItems.ACETOBACTER.get())parentType="Acetobacter";
      else parentType="Unknown";
      int parentSpeed = bio.getSpeed(parent);
      int parentVitality = 320-bio.getDamage(parent);
      BioGrowingPlate.setStats(stack, parentType, parentVitality, parentSpeed, sterile);
    }else if(plate.getItem() == ChemiItems.MEDIUM_PLATE.get() &&  plate.getItem() instanceof BioReagent bio){
      boolean sterile = bio.isSterile(plate);
      BioGrowingPlate.setStats(stack, "Crude", 320, 0, sterile);
    }else{
      throw new IllegalArgumentException("Given ItemStacks was not valid plates.");
    }
    stack.setDamageValue(320);
    return stack;
  }
}
