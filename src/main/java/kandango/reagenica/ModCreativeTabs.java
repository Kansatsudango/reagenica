// ModCreativeTabs.java
package kandango.reagenica;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChemistryMod.MODID);

    public static final RegistryObject<CreativeModeTab> CHEMISTRY_TAB =
        TABS.register("chemistry", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.reagenica.chemistry"))
            .icon(() -> new ItemStack(ChemiItems.HYDROCHLORIC_ACID.get()))
            .build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
