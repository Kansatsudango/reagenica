package kandango.reagenica;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ChemiItemRoles {
  public static void addRole(@Nonnull List<Component> tooltip, String key){
    tooltip.add(Component.translatable("tooltip.reagenica."+key).withStyle(ChatFormatting.GRAY));
  }
  public static void addWarn(@Nonnull List<Component> tooltip, String key){
    tooltip.add(Component.translatable("tooltip.reagenica."+key).withStyle(ChatFormatting.YELLOW));
  }
  public static void addSeriousWarn(@Nonnull List<Component> tooltip, String key){
    tooltip.add(Component.translatable("tooltip.reagenica."+key).withStyle(ChatFormatting.RED));
  }
  public static void addGreen(@Nonnull List<Component> tooltip, String key){
    tooltip.add(Component.translatable("tooltip.reagenica."+key).withStyle(ChatFormatting.GREEN));
  }
  public static void addRaw(@Nonnull List<Component> tooltip, String raw){
    tooltip.add(Component.literal(raw).withStyle(ChatFormatting.GRAY));
  }

  public static void airFilters(@Nonnull List<Component> tooltip){
    addRole(tooltip, "air_filter");
  }
  public static void waterFilters(@Nonnull List<Component> tooltip){
    addRole(tooltip, "water_filter");
  }
  public static Consumer<List<Component>> singleRole(String name){
    return tooltip -> addRole(tooltip, name);
  }
  public static void mineWipe(@Nonnull List<Component> tooltip){
    addRole(tooltip, "minewipe_lore");
    addWarn(tooltip, "minewipe_lore_warn");
  }
}
