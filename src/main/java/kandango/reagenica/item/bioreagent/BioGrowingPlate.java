package kandango.reagenica.item.bioreagent;

import java.util.List;
import kandango.reagenica.ChemiComponents;
import kandango.reagenica.ChemiItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class BioGrowingPlate extends BioPlate{
  public BioGrowingPlate(int color){
    super(color);
  }

  public static void setStats(ItemStack stack, BioReagentTypes parentType, int parentSpeed, boolean sterile) {
    stack.set(ChemiComponents.BIO_TYPE, parentType);
    stack.set(ChemiComponents.EFFICIENCY, (byte)parentSpeed);
    stack.set(ChemiComponents.STERILE, sterile);
  }
  public static void setGrowth(ItemStack stack, int growth){
    stack.set(ChemiComponents.GROWTH, (byte)growth);
    stack.setDamageValue(320-growth*40);
  }
  public BioReagentTypes parentType(ItemStack stack){
    return stack.getOrDefault(ChemiComponents.BIO_TYPE, BioReagentTypes.UNKNOWN);
  }
  public int getGrowth(ItemStack stack){
    return (int)stack.getOrDefault(ChemiComponents.GROWTH, 0);
  }
  
  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(Component.translatable("tooltip.reagenica."+parentType(stack).getSerializedName()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
    tooltipComponents.add(Component.translatable("tooltip.reagenica.plate_growth", getGrowth(stack)).withStyle(ChatFormatting.GRAY));
  }

  public static ItemStack getPlate(BioReagentTypes parent, int speed, boolean sterile){
    ItemStack stack = new ItemStack(ChemiItems.GROWING_PLATE.get());
    BioGrowingPlate.setStats(stack, parent, speed, sterile);
    stack.setDamageValue(320);
    return stack;
  }
  public static ItemStack getPlateFromParent(ItemStack plate, ItemStack parent){
    ItemStack stack = new ItemStack(ChemiItems.GROWING_PLATE.get());
    if(parent.getItem() instanceof BioReagent bio){
      boolean sterile = isSterile(plate);
      BioReagentTypes parentType = bio.type;
      int parentSpeed = getSpeed(parent);
      BioGrowingPlate.setStats(stack, parentType, parentSpeed, sterile);
    }else{
      boolean sterile = isSterile(plate);
      BioGrowingPlate.setStats(stack, BioReagentTypes.CRUDE, 0, sterile);
    }
    stack.setDamageValue(320);
    return stack;
  }
}
