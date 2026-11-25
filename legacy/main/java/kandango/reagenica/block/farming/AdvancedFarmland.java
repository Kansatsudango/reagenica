package kandango.reagenica.block.farming;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.BlockUtil;
import kandango.reagenica.block.farming.crop.AdvancedCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class AdvancedFarmland extends FarmBlock{
  public static final IntegerProperty FERTILIZED = IntegerProperty.create("fertilized",0,3);
  public AdvancedFarmland() {
    super(BlockBehaviour.Properties.copy(Blocks.FARMLAND));
    this.registerDefaultState(this.defaultBlockState().setValue(MOISTURE, 0).setValue(FERTILIZED, 3));
  }
  
  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> state){
    super.createBlockStateDefinition(state);
    state.add(FERTILIZED);
  }

  @Override
  public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
    PlantType type = plantable.getPlantType(world, pos.relative(facing));
    if (PlantType.CROP.equals(type)) {
      return state.is(Blocks.FARMLAND) || state.is(ChemiBlocks.ADVANCED_FARMLAND.get());
    }else if(PlantType.PLAINS.equals(type)) {
         return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND) || state.is(ChemiBlocks.ADVANCED_FARMLAND.get());
    }
    return super.canSustainPlant(state, world, pos, facing, plantable);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    super.randomTick(state, level, pos, random);
    if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
    int chance = 1;
    BlockState abovestate = level.getBlockState(pos.above());
    if(abovestate.getBlock() instanceof AdvancedCropBlock){
      int age = BlockUtil.getStatus(abovestate, CropBlock.AGE).orElse(0);
      if(age!=7)chance = 6;
    }
    if(random.nextInt(2000) < chance){
      int fertilizer = BlockUtil.getStatus(state, FERTILIZED).orElse(0);
      int moisture = BlockUtil.getStatus(state, MOISTURE).orElse(0);
      fertilizer -= 1;
      if(fertilizer<0){
        level.setBlock(pos, Blocks.FARMLAND.defaultBlockState().setValue(MOISTURE, moisture), 3);
      }else{
        level.setBlock(pos, ChemiBlocks.ADVANCED_FARMLAND.get().defaultBlockState().setValue(FERTILIZED, fertilizer).setValue(MOISTURE, moisture), 2);
      }
    }
  }
}
