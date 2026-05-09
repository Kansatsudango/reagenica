package kandango.reagenica.block.farming;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class MushroomBed extends Block{
  public static final IntegerProperty FERTILIZED = IntegerProperty.create("fertilized",0,3);
  public MushroomBed() {
    super(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).randomTicks().strength(0.6F).sound(SoundType.GRAVEL));
    this.registerDefaultState(this.defaultBlockState().setValue(FERTILIZED, 0));
  }
  
  @Override
  protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> state){
    super.createBlockStateDefinition(state);
    state.add(FERTILIZED);
  }

  @Override
  public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
    PlantType type = plantable.getPlantType(world, pos.relative(facing));
    if (PlantType.CAVE.equals(type)) {
      return state.is(Blocks.FARMLAND) || state.is(ChemiBlocks.ADVANCED_FARMLAND.get());
    }else if(PlantType.PLAINS.equals(type)) {
      return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND) || state.is(ChemiBlocks.ADVANCED_FARMLAND.get());
    }
    return super.canSustainPlant(state, world, pos, facing, plantable);
  }
  public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    super.randomTick(state, level, pos, random);
    if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
  }
}
