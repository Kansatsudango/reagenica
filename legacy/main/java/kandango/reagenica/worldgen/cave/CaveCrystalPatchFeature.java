package kandango.reagenica.worldgen.cave;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

public class CaveCrystalPatchFeature extends Feature<CaveCrystalPatchConfig>{

  public CaveCrystalPatchFeature(Codec<CaveCrystalPatchConfig> config) {
    super(config);
  }

  @Override
  public boolean place(@Nonnull FeaturePlaceContext<CaveCrystalPatchConfig> ctx) {
    final WorldGenLevel lv = ctx.level();
    final BlockPos origin = ctx.origin();
    final RandomSource random = ctx.random();
    final CaveCrystalPatchConfig config = ctx.config();

    Set<BlockPos> crystalSpawns = new HashSet<>();
    for(int i=0;i<config.count().sample(random);i++){
      int dx=random.nextInt(21)-10;
      int dz=random.nextInt(21)-10;
      crystalSpawns.add(origin.offset(dx, 0, dz));
    }
    for(BlockPos startpos : crystalSpawns){
      getCrystalPos(lv, startpos).ifPresent(pos -> setCrystal(lv, pos, config.crystal().getState(random, pos)));
    }
    
    return true;
  }

  private void setCrystal(WorldGenLevel lv, BlockPos pos, BlockState crystal){
    if(lv.getBlockState(pos).getFluidState().is(Fluids.WATER) && crystal.hasProperty(BlockStateProperties.WATERLOGGED)){
      BlockState waterCrystal = crystal.setValue(BlockStateProperties.WATERLOGGED, true);
      setBlock(lv, pos, waterCrystal);
    }else{
      setBlock(lv, pos, crystal);
    }
  }

  private Optional<BlockPos> getCrystalPos(WorldGenLevel lv,BlockPos origin){
    if(lv.getBlockState(origin).isAir()){
      BlockPos.MutableBlockPos pos = origin.mutable();
      for(int i=0;i<10;i++){
        pos.move(0,-1,0);
        BlockState state = lv.getBlockState(pos);
        if(!isAirOrWater(state)){
          if(state.getFluidState().is(Fluids.LAVA))return Optional.empty();
          else return Optional.of(pos.move(0, 1, 0).immutable());
        }
      }
    }else{
      BlockPos.MutableBlockPos pos = origin.mutable();
      for(int i=0;i<10;i++){
        pos.move(0,1,0);
        BlockState state = lv.getBlockState(pos);
        if(state.getFluidState().is(Fluids.LAVA)){
          return Optional.empty();
        }
        if(isAirOrWater(state)){
          return Optional.of(pos.immutable());
        }
      }
    }
    return Optional.empty();
  }
  private boolean isAirOrWater(BlockState state){
    return state.isAir() || state.getFluidState().is(Fluids.WATER);
  }
}
