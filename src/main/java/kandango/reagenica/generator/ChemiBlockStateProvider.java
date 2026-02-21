package kandango.reagenica.generator;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.blockfamily.WoodFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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
  private void registerTrapdoorBlockWithItem(RegistryObject<? extends TrapDoorBlock> trapdoor, String name){
    trapdoorBlockWithRenderType(trapdoor.get(), modLoc("block/"+name+"_trapdoor"), true, "cutout");
    itemModels().withExistingParent(name+"_trapdoor", modLoc("block/"+name+"_trapdoor_bottom"));
  }
  private void registerDoorBlockWithItem(RegistryObject<? extends DoorBlock> door, String name){
    doorBlockWithRenderType(door.get(), modLoc("block/"+name+"_door_bottom"), modLoc("block/"+name+"_door_top"), "cutout");
  }
  private void registerSimpleBlockWithItem(RegistryObject<? extends Block> block){
    simpleBlockWithItem(block.get(), models().getExistingFile(blockTexture(block.get())));
  }
  private void registerCutoutSimpleBlockWithItem(RegistryObject<? extends Block> block, String name){
    simpleBlockWithItem(block.get(), models().cubeAll(name, modLoc("block/"+name)).renderType("cutout"));
  }
  private void registerCutoutCrossBlockWithItem(RegistryObject<? extends Block> block, String name){
    simpleBlockWithItem(block.get(), models().cross(name, modLoc("block/"+name)).renderType("cutout"));
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
  private void registerWoodThings(WoodFamily woodFamily){
    registerLogBlockWithItem(woodFamily.LOG);
    registerCutoutSimpleBlockWithItem(woodFamily.LEAVES, woodFamily.name+"_leaves");
    registerCutoutCrossBlockWithItem(woodFamily.SAPLING, woodFamily.name+"_sapling");
    registerSimpleBlockWithItem(woodFamily.PLANKS);
    registerStairBlockWithItem(woodFamily.STAIRS, woodFamily.PLANKS);
    registerSlabBlockWithItem(woodFamily.SLAB, woodFamily.name);
    registerLogBlockWithItem(woodFamily.STRIPPED_LOG);
    registerFenceBlockWithItem(woodFamily.FENCE, woodFamily.name);
    registerFenceGateBlockWithItem(woodFamily.FENCE_GATE, woodFamily.name);
    registerTrapdoorBlockWithItem(woodFamily.TRAPDOOR, woodFamily.name);
    registerDoorBlockWithItem(woodFamily.DOOR, woodFamily.name);
    registerButtonBlockWithItem(woodFamily.BUTTON, woodFamily.name);
    registerPressurePlateBlockWithItem(woodFamily.PRESSURE_PLATE, woodFamily.name);
  }
  
}
