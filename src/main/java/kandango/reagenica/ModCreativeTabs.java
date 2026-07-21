// ModCreativeTabs.java
package kandango.reagenica;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;

public class ModCreativeTabs {
  public static final DeferredRegister<CreativeModeTab> TABS =
    DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChemistryMod.MODID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHEMISTRY_TAB =
    TABS.register("chemistry", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.reagenica.chemistry"))
      .icon(() -> new ItemStack(ChemiItems.HYDROCHLORIC_ACID.get()))
      .build());
  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PALEO_TAB =
    TABS.register("paleo_world", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.reagenica.paleo"))
      .icon(() -> new ItemStack(ChemiBlocks.METASEQUOIA.LOG.get()))
      .build());
  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS =
    TABS.register("tools", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.reagenica.tools"))
      .icon(() -> new ItemStack(ChemiItems.IRIDIUM_TOOLS.AXE.get()))
      .build());

  public static void register(IEventBus bus) {
    TABS.register(bus);
  }
}
