package kandango.reagenica.generator;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.family.StoneFamily;
import kandango.reagenica.family.WoodFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiBlockStateProvider extends BlockStateProvider{

  public ChemiBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, ChemistryMod.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    WoodFamily.Woods.forEach(this::processWood);
    StoneFamily.Stones.forEach(this::processStone);
    processCrosses();
    processSimples();
    ChemiBlocks.listFlowerPots.forEach(rg -> pottedPlant(rg, rg.getId().getPath()));
  }

  private void registerStairBlockWithItem(DeferredHolder<? extends StairBlock> stairs, DeferredHolder<? extends Block> base){
    stairsBlock(stairs.get(), blockTexture(base.get()));
    simpleBlockItem(stairs.get(), models().getExistingFile(blockTexture(stairs.get())));
  }
  private void registerSlabBlockWithItem(DeferredHolder<? extends SlabBlock> slab, String name){
    slabBlock(slab.get(), modLoc("block/" + name), modLoc("block/" + name));
    simpleBlockItem(slab.get(), models().getExistingFile(blockTexture(slab.get())));
  }
  private void registerLogBlockWithItem(DeferredHolder<? extends RotatedPillarBlock> log){
    logBlock(log.get());
    simpleBlockItem(log.get(), models().getExistingFile(blockTexture(log.get())));
  }
  private void registerWoodBlockWithItem(DeferredHolder<? extends RotatedPillarBlock> wood, String name){
    axisBlock(wood.get(), modLoc("block/"+name+"_log"), modLoc("block/"+name+"_log"));
    simpleBlockItem(wood.get(), models().getExistingFile(blockTexture(wood.get())));
  }
  private void registerTrapdoorBlockWithItem(DeferredHolder<? extends TrapDoorBlock> trapdoor, String name){
    trapdoorBlockWithRenderType(trapdoor.get(), modLoc("block/"+name+"_trapdoor"), true, "cutout");
    itemModels().withExistingParent(name+"_trapdoor", modLoc("block/"+name+"_trapdoor_bottom"));
  }
  private void registerDoorBlockWithItem(DeferredHolder<? extends DoorBlock> door, String name){
    doorBlockWithRenderType(door.get(), modLoc("block/"+name+"_door_bottom"), modLoc("block/"+name+"_door_top"), "cutout");
    itemModels().withExistingParent(name+"_door", mcLoc("item/generated")).texture("layer0", modLoc("item/"+name+"_door"));
  }
  private void registerSimpleBlockWithItem(DeferredHolder<? extends Block> block){
    simpleBlockWithItem(block.get(), cubeAll(block.get()));
  }
  private void registerLeavesBlockWithItem(DeferredHolder<? extends Block> leaves, String name){
    ModelFile model = models().withExistingParent(name+"_leaves", mcLoc("block/leaves")).texture("all", modLoc("block/"+name+"_leaves")).renderType("cutout");
    getVariantBuilder(leaves.get())
            .partialState()
            .addModels(new ConfiguredModel(model));
    simpleBlockItem(leaves.get(), models().getExistingFile(blockTexture(leaves.get())));
  }
  private void registerCutoutCrossBlockWithItem(DeferredHolder<? extends Block> block, String name){
    simpleBlock(block.get(), models().cross(name, modLoc("block/"+name)).renderType("cutout"));
    itemModels().withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("block/"+name));
  }
  private void registerButtonBlockWithItem(DeferredHolder<? extends ButtonBlock> button, String name){
    buttonBlock(button.get(), modLoc("block/"+name+"_planks"));
    models().withExistingParent(name+"_button_inventory", mcLoc("block/button_inventory")).texture("texture", modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(name + "_button",modLoc("block/" + name + "_button_inventory"));
  }
  private void registerPressurePlateBlockWithItem(DeferredHolder<? extends PressurePlateBlock> plate, String name){
    pressurePlateBlock(plate.get(), modLoc("block/"+name+"_planks"));
    simpleBlockItem(plate.get(), models().getExistingFile(blockTexture(plate.get())));
  }
  private void registerFenceBlockWithItem(DeferredHolder<? extends FenceBlock> fence, String name){
    fenceBlock(fence.get(), modLoc("block/"+name+"_planks"));
    models().withExistingParent(name+"_fence_inventory", mcLoc("block/fence_inventory")).texture("texture", modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(
            name + "_fence",
            modLoc("block/" + name + "_fence_inventory")
    );
  }
  private void registerFenceGateBlockWithItem(DeferredHolder<? extends FenceGateBlock> gate, String name){
    fenceGateBlock(gate.get(), modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(
            name + "_fence_gate",
            modLoc("block/" + name + "_fence_gate")
    );
  }
  private void pottedPlant(DeferredHolder<? extends FlowerPotBlock> potted, String name) {
    simpleBlock(potted.get(),
      models().singleTexture(
        name,
        mcLoc("block/flower_pot_cross"),
        "plant",
        modLoc("block/" + ForgeRegistries.BLOCKS.getKey(potted.get().getContent()).getPath())).renderType(mcLoc("cutout"))
    );
}
  private void registerSignBlocksWithItem(DeferredHolder<? extends StandingSignBlock> sign, DeferredHolder<? extends WallSignBlock> wallsign, String name){
    signBlock(sign.get(), wallsign.get(), modLoc("block/"+name+"_planks"));
    itemModels().withExistingParent(name+"_sign", mcLoc("item/generated")).texture("layer0", modLoc("item/"+name+"_sign"));
  }
  private void registerHangingSign(DeferredHolder<? extends CeilingHangingSignBlock> hanging, DeferredHolder<? extends WallHangingSignBlock> wall, String name) {
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
  private void registerCrossBlock(DeferredHolder<Block> block){
    String name = block.getId().getPath();
    ModelFile model = models().withExistingParent(name, mcLoc("block/cross")).texture("cross", modLoc("block/"+name)).renderType("cutout");
    getVariantBuilder(block.get())
            .partialState()
            .addModels(new ConfiguredModel(model));
    itemModels().withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("block/"+name));
  }
  private void registerWallBlockWithItem(DeferredHolder<? extends WallBlock> wall, String name){
    wallBlock(wall.get(), modLoc("block/"+name));
    models().withExistingParent(name+"_wall_inventory", mcLoc("block/wall_inventory")).texture("wall", modLoc("block/"+name));
    itemModels().withExistingParent(
            name + "_wall",
            modLoc("block/" + name + "_wall_inventory")
    );
  }

  private void processWood(WoodFamily woodFamily){
    registerLogBlockWithItem(woodFamily.LOG);
    registerWoodBlockWithItem(woodFamily.WOOD, woodFamily.name);
    registerLeavesBlockWithItem(woodFamily.LEAVES, woodFamily.name);
    registerCutoutCrossBlockWithItem(woodFamily.SAPLING, woodFamily.name+"_sapling");
    registerSimpleBlockWithItem(woodFamily.PLANKS);
    registerStairBlockWithItem(woodFamily.STAIRS, woodFamily.PLANKS);
    registerSlabBlockWithItem(woodFamily.SLAB, woodFamily.name+"_planks");
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
    pottedPlant(woodFamily.POTTED_SAPLING, "potted_"+woodFamily.name+"_sapling");
  }
  private void processStone(StoneFamily stoneFamily){
    registerSimpleBlockWithItem(stoneFamily.STONE);
    registerStairBlockWithItem(stoneFamily.STAIRS, stoneFamily.STONE);
    registerSlabBlockWithItem(stoneFamily.SLAB, stoneFamily.name);
    registerWallBlockWithItem(stoneFamily.WALL, stoneFamily.name);
    registerSimpleBlockWithItem(stoneFamily.P_STONE);
    registerStairBlockWithItem(stoneFamily.P_STAIRS, stoneFamily.P_STONE);
    registerSlabBlockWithItem(stoneFamily.P_SLAB, "polished_"+stoneFamily.name);
  }
  private void processCrosses(){
    registerCrossBlock(ChemiBlocks.MUSHROOM_RED);
    registerCrossBlock(ChemiBlocks.MUSHROOM_BLUE);
    registerCrossBlock(ChemiBlocks.MUSHROOM_GREEN);
    registerCrossBlock(ChemiBlocks.MUSHROOM_PURPLE);
    registerCrossBlock(ChemiBlocks.MUSHROOM_GLOWING);
    registerCrossBlock(ChemiBlocks.BROWN_BISPORUS);
    registerCrossBlock(ChemiBlocks.WHITE_BISPORUS);
    registerCrossBlock(ChemiBlocks.GRIFOLA_FRONDOSA);
    registerCrossBlock(ChemiBlocks.TRICHOLOMA_MATSUTAKE);
  }
  private void processSimples(){
    registerSimpleBlockWithItem(ChemiBlocks.RAW_LEAD_BLOCK);
    registerSimpleBlockWithItem(ChemiBlocks.CHALCOPYRITE_BLOCK);
    registerSimpleBlockWithItem(ChemiBlocks.BAUXITE_BLOCK);
    registerSimpleBlockWithItem(ChemiBlocks.APATITE_BLOCK);
    registerSimpleBlockWithItem(ChemiBlocks.OILSAND_BLOCK);
  }
}
