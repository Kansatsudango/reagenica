package kandango.reagenica.generator;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.item.reagent.GasReagent;
import kandango.reagenica.item.reagent.LiquidReagent;
import kandango.reagenica.item.reagent.PowderReagent;
import kandango.reagenica.item.reagent.ReagentPowderIndustrial;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ChemiItemModelProvider extends ItemModelProvider{
  public ChemiItemModelProvider(PackOutput output, ExistingFileHelper helper){
    super(output, ChemistryMod.MODID, helper);
  }

  @Override
  protected void registerModels() {
    ChemiItems.listItems.stream().forEach(this::register);
    ChemiItems.PLATINUM_ARMOR.armorItems().forEach(this::register);
    ChemiItems.IRIDIUM_ARMOR.armorItems().forEach(this::register);
  }
  private void register(RegistryObject<? extends Item> item){
    Item instance = item.get();
    if(instance instanceof LiquidReagent){
      liquidTesttubeItem(item);
    }else if(instance instanceof PowderReagent){
      powderReagentItem(item);
    }else if(instance instanceof GasReagent){
      gasTesttubeItem(item);
    }else if(instance instanceof ReagentPowderIndustrial){
      powderIndustrialItem(item);
    }else if(instance instanceof BioReagent){
      bioPlateItem(item);
    }else if(instance instanceof DiggerItem || instance instanceof SwordItem){
      handheldItem(item);
    }else{
      simpleItem(item);
    }
  }
  private void simpleItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + item.getId().getPath()));
  }
  private void handheldItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), mcLoc("item/handheld"))
            .texture("layer0", modLoc("item/" + item.getId().getPath()));
  }
  private void liquidTesttubeItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), modLoc("item/liquid_reagent"));
  }
  private void gasTesttubeItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), modLoc("item/gas_reagent"));
  }
  private void powderReagentItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), modLoc("item/powder_reagent"));
  }
  private void powderIndustrialItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), modLoc("item/industrial_powder"));
  }
  private void bioPlateItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), modLoc("item/microorganism"));
  }
}
