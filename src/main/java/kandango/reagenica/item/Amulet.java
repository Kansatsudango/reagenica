package kandango.reagenica.item;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class Amulet extends Item{
  private final Supplier<Enchantment> wish;
  private final NumberProvider lvl;
  public Amulet(Supplier<Enchantment> ench, NumberProvider lvl){
    super(new Item.Properties().stacksTo(1));
    this.wish = ench;
    this.lvl = lvl;
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.amulet").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable(wish.get().getDescriptionId()).withStyle(ChatFormatting.GOLD));
  }

  public Enchantment getStoredEnchantment(){
    return wish.get();
  }
  public NumberProvider getEnchLevelProvider(){
    return lvl;
  }
}
