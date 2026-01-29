package kandango.reagenica.worldgen.cave;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class CaveStreamFeature extends Feature<CaveStreamConfig>{

  public CaveStreamFeature(Codec<CaveStreamConfig> config) {
    super(config);
  }

  @Override
  public boolean place(@Nonnull FeaturePlaceContext<CaveStreamConfig> ctx) {
    WorldGenLevel lv = ctx.level();
    BlockPos origin = ctx.origin();
    RandomSource random = ctx.random();
    CaveStreamConfig config = ctx.config();

    BlockPos.MutableBlockPos pos = origin.mutable();
    final float rockiness = config.rockiness().sample(random);
    for(int x=-10;x<=10;x++){
      for(int z=-10;z<=10;z++){
        if(x*x + z*z > 100)continue;
        pos.set(origin.getX(), origin.getY(), origin.getZ());
        pos.move(x, 0, z);
        getFloorPos(lv, pos).filter(p -> isPlaceable(lv, p)).ifPresent(floor -> {
          if(random.nextFloat() < rockiness){
            if(random.nextFloat() < 0.4f*(1.1f-rockiness)){
              setBlock(lv, floor, config.ore().getState(random, floor));
            }else{
              setBlock(lv, floor, config.block().getState(random, floor));
            }
          }else{
            if(isWaterPlaceable(lv, floor)){
              setBlock(lv, floor, Blocks.WATER.defaultBlockState());
            }else{
              setBlock(lv, floor, config.block().getState(random, floor));
            }
          }
        });
      }
    }

    return true;
  }

  private boolean isPlaceable(WorldGenLevel lv, BlockPos pos){
    BlockState state = lv.getBlockState(pos);
    BlockState below = lv.getBlockState(pos.below());
    FluidState fstate = state.getFluidState();
    FluidState fbelow = below.getFluidState();
    if(!fstate.isEmpty() && !fstate.is(Fluids.WATER)){
      return false;
    }else if(fstate.is(Fluids.WATER) && fbelow.is(Fluids.WATER)){
      return false;
    }else{
      return true;
    }
  }

  private boolean isWaterPlaceable(WorldGenLevel lv, BlockPos pos){
    {
      BlockPos below = pos.below();
      BlockState bottom = lv.getBlockState(below);
      if(bottom.canBeReplaced(Fluids.WATER)){
        return false;
      }
    }
    for(Direction dir : Direction.Plane.HORIZONTAL){
      BlockPos rel = pos.relative(dir);
      BlockState neighbor = lv.getBlockState(rel);
      if(!neighbor.getFluidState().is(Fluids.WATER) && neighbor.canBeReplaced(Fluids.WATER)){
        return false;
      }
    }
    return true;
  }

  private Optional<BlockPos> getFloorPos(WorldGenLevel lv,BlockPos origin){
    if(lv.getBlockState(origin).isAir()){
      BlockPos.MutableBlockPos pos = origin.mutable();
      for(int i=0;i<10;i++){
        pos.move(0,-1,0);
        if(!lv.getBlockState(pos).isAir()) return Optional.of(pos.immutable());
      }
    }else{
      BlockPos.MutableBlockPos pos = origin.mutable();
      for(int i=0;i<10;i++){
        pos.move(0,1,0);
        if(lv.getBlockState(pos).isAir()) return Optional.of(pos.move(0,-1,0).immutable());
      }
    }
    return Optional.empty();
  }

}