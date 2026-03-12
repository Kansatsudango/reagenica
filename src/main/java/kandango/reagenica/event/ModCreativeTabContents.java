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
import kandango.reagenica.family.ArmorFamily;
import kandango.reagenica.family.CrystalFamily;
import kandango.reagenica.family.ToolFamily;
import kandango.reagenica.family.WoodFamily;

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
          WoodFamily.Woods.stream().flatMap(WoodFamily::blockItems).forEach(event::accept);
          CrystalFamily.Crystals.stream().flatMap(c -> c.crystalItems()).forEach(event::accept);
        }else if(event.getTab() == ModCreativeTabs.TOOLS.get()){
          ToolFamily.Tools.stream().flatMap(ToolFamily::toolItems).forEach(event::accept);
          ArmorFamily.Armors.stream().flatMap(ArmorFamily::armorItems).forEach(event::accept);
        }
    }
}
