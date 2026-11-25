package kandango.reagenica.generator;

import java.util.Map;

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
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChemiItemModelProvider extends ItemModelProvider{
  public ChemiItemModelProvider(PackOutput output, ExistingFileHelper helper){
    super(output, ChemistryMod.MODID, helper);
  }

  @Override
  protected void registerModels() {
    ChemiItems.listItems.stream().forEach(this::register);
    ArmorFamily.Armors.stream().forEach(this::registerArmor);
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
  private static final Map<String, Float> VANILLA_TRIM_MATERIALS = Map.ofEntries(
    Map.entry("quartz", 0.1F),
    Map.entry("iron", 0.2F),
    Map.entry("netherite", 0.3F),
    Map.entry("redstone", 0.4F),
    Map.entry("copper", 0.5F),
    Map.entry("gold", 0.6F),
    Map.entry("emerald", 0.7F),
    Map.entry("diamond", 0.8F),
    Map.entry("lapis", 0.9F),
    Map.entry("amethyst", 1.0F)
  );
  private void registerArmor(ArmorFamily family){
    registerTrimmedArmorItem(family.HELMET, "helmet");
    registerTrimmedArmorItem(family.CHESTPLATE, "chestplate");
    registerTrimmedArmorItem(family.LEGGINGS, "leggings");
    registerTrimmedArmorItem(family.BOOTS, "boots");
  }
  private void registerTrimmedArmorItem(RegistryObject<? extends Item> armorItem, String armorSlot){
    ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(armorItem.get());

    ModelFile.UncheckedModelFile generated = new ModelFile.UncheckedModelFile("minecraft:item/generated");

    ItemModelBuilder baseModel = getBuilder(itemId.getPath()).parent(generated)
        .texture("layer0", modLoc("item/" + itemId.getPath()));

    VANILLA_TRIM_MATERIALS.entrySet().stream().sorted(Map.Entry.comparingByValue())
      .forEach(entry -> { 
        String material = entry.getKey();
        float index = entry.getValue();
        ResourceLocation trimModel = modLoc("item/" + itemId.getPath() + "_" + material + "_trim");

        ResourceLocation trimTexture = mcLoc(
            "trims/items/" + armorSlot + "_trim_" + material
        );
        existingFileHelper.trackGenerated(trimTexture, PackType.CLIENT_RESOURCES, ".png", "textures");

        getBuilder(itemId.getPath() + "_" + material + "_trim")
            .parent(generated)
            .texture("layer0", modLoc("item/" + itemId.getPath()))
            .texture("layer1", trimTexture);

        baseModel.override()
            .predicate(mcLoc("trim_type"), index)
            .model(new ModelFile.UncheckedModelFile(trimModel))
            .end();
    });
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
