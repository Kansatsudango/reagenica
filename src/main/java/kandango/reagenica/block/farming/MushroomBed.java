package kandango.reagenica.block.farming;

import java.util.ArrayList;
import java.util.List;

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
    if (!level.isAreaLoaded(pos, 2)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
    if(random.nextInt(300)==0){
      int fertilized = state.getValue(FERTILIZED);
      if(fertilized>0){
        level.setBlock(pos, state.setValue(FERTILIZED, fertilized-1), 3);
      }
    }
    BlockState aboveState = level.getBlockState(pos.above());
    if(aboveState.isAir()){
      if(state.getValue(FERTILIZED)>0 || random.nextInt(4)==0){
        MushroomMutationManager manager = new MushroomMutationManager();
        for(int x=-2;x<=2;x++){
          for(int z=-2;z<=2;z++){
            BlockPos currentpos = pos.offset(x,1,z);
            if(currentpos.equals(pos))continue;
            BlockState mushroom = level.getBlockState(currentpos);
            manager.addMushroom(mushroom, -1<=x && x<=1 && -1<=z && z<=1);
          }
        }
        BlockState result = manager.getMutationResult(random);
        if (!result.is(Blocks.AIR)) {
          level.setBlock(pos.above(), result, 3);
        }
      }
    }

  }
  public static class MushroomMutationManager {
    private final List<BlockState> relativeMushrooms = new ArrayList<>();
    private final List<BlockState> effectiveAreaMushrooms = new ArrayList<>();
    public static final List<MutationCondition> mutationRules = new ArrayList<>();
    static{
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(Blocks.BROWN_MUSHROOM, 1)), List.of(), List.of(), Blocks.BROWN_MUSHROOM.defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(Blocks.RED_MUSHROOM, 1)), List.of(), List.of(), Blocks.RED_MUSHROOM.defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_RED.get(), 1)), List.of(), List.of(), ChemiBlocks.MUSHROOM_RED.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_GREEN.get(), 1)), List.of(), List.of(), ChemiBlocks.MUSHROOM_GREEN.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_BLUE.get(), 1)), List.of(), List.of(), ChemiBlocks.MUSHROOM_BLUE.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_PURPLE.get(), 1)), List.of(), List.of(), ChemiBlocks.MUSHROOM_PURPLE.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_GLOWING.get(), 1)), List.of(), List.of(), ChemiBlocks.MUSHROOM_GLOWING.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.BROWN_BISPORUS.get(), 1)), List.of(), List.of(), ChemiBlocks.BROWN_BISPORUS.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.WHITE_BISPORUS.get(), 1)), List.of(), List.of(), ChemiBlocks.WHITE_BISPORUS.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.GRIFOLA_FRONDOSA.get(), 1)), List.of(), List.of(), ChemiBlocks.GRIFOLA_FRONDOSA.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.TRICHOLOMA_MATSUTAKE.get(), 1)), List.of(), List.of(), ChemiBlocks.TRICHOLOMA_MATSUTAKE.get().defaultBlockState(), 10));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(Blocks.BROWN_MUSHROOM, 2)), List.of(), List.of(), ChemiBlocks.BROWN_BISPORUS.get().defaultBlockState(), 1));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(Blocks.BROWN_MUSHROOM, 1), new BlockCounts(ChemiBlocks.BROWN_BISPORUS.get(), 1)), List.of(), List.of(), ChemiBlocks.WHITE_BISPORUS.get().defaultBlockState(), 8));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.WHITE_BISPORUS.get(), 2), new BlockCounts(ChemiBlocks.BROWN_BISPORUS.get(), 2)), List.of(), List.of(), ChemiBlocks.GRIFOLA_FRONDOSA.get().defaultBlockState(), 4));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.GRIFOLA_FRONDOSA.get(), 2), new BlockCounts(ChemiBlocks.MUSHROOM_GLOWING.get(), 2)), List.of(), List.of(), ChemiBlocks.TRICHOLOMA_MATSUTAKE.get().defaultBlockState(), 4));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(Blocks.RED_MUSHROOM, 2)), List.of(), List.of(), ChemiBlocks.MUSHROOM_GLOWING.get().defaultBlockState(), 1));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_GLOWING.get(), 2), new BlockCounts(Blocks.RED_MUSHROOM, 2)), List.of(), List.of(), ChemiBlocks.MUSHROOM_RED.get().defaultBlockState(), 4));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_RED.get(), 2), new BlockCounts(ChemiBlocks.TRICHOLOMA_MATSUTAKE.get(), 2)), List.of(), List.of(), ChemiBlocks.MUSHROOM_GREEN.get().defaultBlockState(), 2));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_RED.get(), 2), new BlockCounts(ChemiBlocks.TRICHOLOMA_MATSUTAKE.get(), 2)), List.of(), List.of(), ChemiBlocks.MUSHROOM_BLUE.get().defaultBlockState(), 2));
      mutationRules.add(new MutationCondition(List.of(new BlockCounts(ChemiBlocks.MUSHROOM_RED.get(), 2), new BlockCounts(ChemiBlocks.TRICHOLOMA_MATSUTAKE.get(), 2)), List.of(), List.of(), ChemiBlocks.MUSHROOM_PURPLE.get().defaultBlockState(), 2));
    }

    public void addMushroom(BlockState mushroom, boolean isRelative) {
      if (isRelative) {
        relativeMushrooms.add(mushroom);
      }
      effectiveAreaMushrooms.add(mushroom);
    }
    public BlockState getMutationResult(RandomSource random) {
      List<BlockState> possibleResults = new ArrayList<>();
      mutationRules.stream().filter(rule -> rule.conditions.stream().allMatch(bc -> hasMushroomsMoreThan(relativeMushrooms, bc.block(), bc.count())))
        .forEach(c -> addToList(possibleResults, c.result(), c.weight()));
      return possibleResults.isEmpty() ? Blocks.AIR.defaultBlockState() : possibleResults.get(random.nextInt(possibleResults.size()));
    }
    private static void addToList(List<BlockState> list, BlockState state, int count) {
      for(int i=0;i<count;i++){
        list.add(state);
      }
    }

    public static boolean hasMushroomsMoreThan(List<BlockState> relative, Block block, int count) {
      return relative.stream().filter(s -> s.is(block)).count() >= count;
    }
    
    public static record MutationCondition(List<BlockCounts> conditions, List<BlockCounts> areaCondition, List<BlockCounts> areaDeny, BlockState result, int weight) {
    }
    public static record BlockCounts(Block block, int count) {
    }
  }
}
