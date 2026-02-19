package kandango.reagenica.event;

import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ModCreativeTabs;
import kandango.reagenica.ChemiItems.CreativeTabContent;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabContents {

    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeTabs.CHEMISTRY_TAB.get()) {
          for(int i=0;i!=ChemiItems.listCreativeTab.size();i++){
            CreativeTabContent content = ChemiItems.listCreativeTab.get(i);
            content.supplier().ifPresentOrElse(sup -> event.accept(sup),() -> content.stack().ifPresent(stack -> event.accept(stack.get(), TabVisibility.PARENT_AND_SEARCH_TABS)));
          }
          for(int i=0;i!=ChemiBlocks.listBlockItems.size();i++){
            event.accept(ChemiBlocks.listBlockItems.get(i));
          }
        }else if(event.getTab() == ModCreativeTabs.PALEO_TAB.get()){
          ChemiBlocks.METASEQUOIA.blockItems().forEach(event::accept);
        }
    }
}
