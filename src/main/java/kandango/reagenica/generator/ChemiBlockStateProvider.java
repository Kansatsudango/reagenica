package kandango.reagenica.generator;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.blockfamily.WoodFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ChemiBlockStateProvider extends BlockStateProvider{

  public ChemiBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, ChemistryMod.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    registerWoodThings(ChemiBlocks.METASEQUOIA);
    registerWoodThings(ChemiBlocks.TAXODIUM);
    registerWoodThings(ChemiBlocks.GINKGO);
    registerWoodThings(ChemiBlocks.MAGNOLIA);
    registerWoodThings(ChemiBlocks.FICUS);
  }

  private void registerStairBlockWithItem(RegistryObject<? extends StairBlock> stairs, RegistryObject<? extends Block> base){
    stairsBlock(stairs.get(), blockTexture(base.get()));
    simpleBlockItem(stairs.get(), models().getExistingFile(blockTexture(stairs.get())));
  }
  private void registerSlabBlockWithItem(RegistryObject<? extends SlabBlock> slab, String name){
    slabBlock(slab.get(), modLoc("block/" + name + "_planks"), modLoc("block/" + name + "_planks"));
    simpleBlockItem(slab.get(), models().getExistingFile(blockTexture(slab.get())));
  }
  private void registerLogBlockWithItem(RegistryObject<? extends RotatedPillarBlock> log){
    logBlock(log.get());
    simpleBlockItem(log.get(), models().getExistingFile(blockTexture(log.get())));
  }
  private void registerWoodBlockWithItem(RegistryObject<? extends RotatedPillarBlock> wood, String name){
    axisBlock(wood.get(), modLoc("block/"+name+"_log"), modLoc("block/"+name+"_log"));
    simpleBlockItem(wood.get(), models().getExistingFile(blockTexture(wood.get())));
  }
  private void registerTrapdoorBlockWithItem(RegistryObject<? extends TrapDoorBlock> trapdoor, String name){
    trapdoorBlockWithRenderType(trapdoor.get(), modLoc("block/"+name+"_trapdoor"), true, "cutout");
    itemModels().withExistingParent(name+"_trapdoor", modLoc("block/"+name+"_trapdoor_bottom"));
  }
  private void registerDoorBlockWithItem(RegistryObject<? extends DoorBlock> door, String name){
    doorBlockWithRenderType(door.get(), modLoc("block/"+name+"_door_bottom"), modLoc("block/"+name+"_door_top"), "cutout");
    itemModels().withExistingParent(name+"_door", mcLoc("item/generated")).texture("layer0", modLoc("item/"+name+"_door"));
  }
  private void registerSimpleBlockWithItem(RegistryObject<? extends Block> block){
    simpleBlockWithItem(block.get(), cubeAll(block.get()));
  }
  private void registerLeavesBlockWithItem(RegistryObject<? extends Block> leaves, String name){
    ModelFile model = models().withExistingParent(name+"_leaves", mcLoc("block/leaves")).texture("all", modLoc("block/"+name+"_leaves")).renderType("cutout");
    getVariantBuilder(leaves.get())
            .partialState()
            .addModels(new ConfiguredModel(model));
    simpleBlockItem(leaves.get(), models().getExistingFile(blockTexture(leaves.get())));
  }
  private void registerCutoutCrossBlockWithItem(RegistryObject<? extends Block> block, String name){
    simpleBlock(block.get(), models().cross(name, modLoc("block/"+name)).renderType("cutout"));
    itemModels().withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("block/"+name));
  }
  private void registerButtonBlockWithItem(RegistryObject<? extends ButtonBlock> button, String name){
    buttonBlock(button.get(), modLoc("block/"+name+"_planks"));
    models().withExistingParent(name+"_button_inventory", mcLoc("block/button_inventory")).texture("texture", modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(name + "_button",modLoc("block/" + name + "_button_inventory"));
  }
  private void registerPressurePlateBlockWithItem(RegistryObject<? extends PressurePlateBlock> plate, String name){
    pressurePlateBlock(plate.get(), modLoc("block/"+name+"_planks"));
    simpleBlockItem(plate.get(), models().getExistingFile(blockTexture(plate.get())));
  }
  private void registerFenceBlockWithItem(RegistryObject<? extends FenceBlock> fence, String name){
    fenceBlock(fence.get(), modLoc("block/"+name+"_planks"));
    models().withExistingParent(name+"_fence_inventory", mcLoc("block/fence_inventory")).texture("texture", modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(
            name + "_fence",
            modLoc("block/" + name + "_fence_inventory")
    );
  }
  private void registerFenceGateBlockWithItem(RegistryObject<? extends FenceGateBlock> gate, String name){
    fenceGateBlock(gate.get(), modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(
            name + "_fence_gate",
            modLoc("block/" + name + "_fence_gate")
    );
  }
  private void registerSignBlocksWithItem(RegistryObject<? extends StandingSignBlock> sign, RegistryObject<? extends WallSignBlock> wallsign, String name){
    signBlock(sign.get(), wallsign.get(), modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(name+"_sign", mcLoc("item/generated")).texture("layer0", modLoc("item/"+name+"_sign"));
  }
  private void registerHangingSign(RegistryObject<? extends CeilingHangingSignBlock> hanging, RegistryObject<? extends WallHangingSignBlock> wall, String name) {
    ResourceLocation particle = modLoc("block/stripped_" + name + "_log");
    ModelFile model = models().getBuilder(name + "_hanging_sign")
            .texture("particle", particle);
    getVariantBuilder(hanging.get())
            .partialState()
            .addModels(new ConfiguredModel(model));
    getVariantBuilder(wall.get())
            .partialState()
            .addModels(new ConfiguredModel(model));
    itemModels().withExistingParent(name+"_hanging_sign", mcLoc("item/generated")).texture("layer0", modLoc("item/"+name+"_hanging_sign"));
}
  private void registerWoodThings(WoodFamily woodFamily){
    registerLogBlockWithItem(woodFamily.LOG);
    registerWoodBlockWithItem(woodFamily.WOOD, woodFamily.name);
    registerLeavesBlockWithItem(woodFamily.LEAVES, woodFamily.name);
    registerCutoutCrossBlockWithItem(woodFamily.SAPLING, woodFamily.name+"_sapling");
    registerSimpleBlockWithItem(woodFamily.PLANKS);
    registerStairBlockWithItem(woodFamily.STAIRS, woodFamily.PLANKS);
    registerSlabBlockWithItem(woodFamily.SLAB, woodFamily.name);
    registerLogBlockWithItem(woodFamily.STRIPPED_LOG);
    registerWoodBlockWithItem(woodFamily.STRIPPED_WOOD, "stripped_"+woodFamily.name);
    registerSignBlocksWithItem(woodFamily.STANDING_SIGN, woodFamily.WALL_SIGN, woodFamily.name);
    registerHangingSign(woodFamily.CEILING_HANGING_SIGN, woodFamily.WALL_HANGING_SIGN, woodFamily.name);
    registerFenceBlockWithItem(woodFamily.FENCE, woodFamily.name);
    registerFenceGateBlockWithItem(woodFamily.FENCE_GATE, woodFamily.name);
    registerTrapdoorBlockWithItem(woodFamily.TRAPDOOR, woodFamily.name);
    registerDoorBlockWithItem(woodFamily.DOOR, woodFamily.name);
    registerButtonBlockWithItem(woodFamily.BUTTON, woodFamily.name);
    registerPressurePlateBlockWithItem(woodFamily.PRESSURE_PLATE, woodFamily.name);
  }
  
}
