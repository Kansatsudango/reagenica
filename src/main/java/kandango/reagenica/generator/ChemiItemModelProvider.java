package kandango.reagenica.generator;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.family.ArmorFamily;
import kandango.reagenica.family.CrystalFamily;
import kandango.reagenica.family.ToolFamily;
import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.item.reagent.GasReagent;
import kandango.reagenica.item.reagent.LiquidReagent;
import kandango.reagenica.item.reagent.PowderReagent;
import kandango.reagenica.item.reagent.ReagentPowderIndustrial;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.Block;
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
    ArmorFamily.Armors.stream().flatMap(ArmorFamily::armorItems).forEach(this::register);
    ToolFamily.Tools.stream().flatMap(ToolFamily::toolItems).forEach(this::registerTools);
    ChemiBlocks.listBlocks.stream().map(b -> b.blockreg()).forEach(this::blockItemSafe);
    CrystalFamily.Crystals.stream().forEach(this::crystalFamily);
  }
  private void registerTools(RegistryObject<? extends TieredItem> item){
    handheldItem(item);
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
    }else if(!existingFileHelper.exists(modLoc("item/" + item.getId().getPath()), PackType.CLIENT_RESOURCES, ".json", "models")){
      simpleItem(item);
    }
  }
  private void simpleItem(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
            .texture("layer0", modLoc("item/" + item.getId().getPath()));
  }
  private void simpleItemInBlock(RegistryObject<? extends Item> item) {
    withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
            .texture("layer0", modLoc("block/" + item.getId().getPath()));
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
  private void blockItemSafe(RegistryObject<? extends Block> block) {
    ResourceLocation model = modLoc("block/" + block.getId().getPath());
    if (existingFileHelper.exists(model, PackType.CLIENT_RESOURCES, ".json", "models")) {
      withExistingParent(block.getId().getPath(), model);
      ChemistryMod.LOGGER.info("Created {} blockItem model automatically.",block.getId().getPath());
    }else{
      ChemistryMod.LOGGER.info("{} blockItem model was skipped.",block.getId().getPath());
    }
  }
  private void crystalFamily(CrystalFamily crystal){
    blockItemSafe(crystal.BLOCK);
    blockItemSafe(crystal.BUDDING_BLOCK);
    simpleItemInBlock(crystal.CRYSTAL_ITEM);
    simpleItemInBlock(crystal.CRYSTAL_BUD_ITEM);
    simpleItem(crystal.SHARD_ITEM);
  }
}
