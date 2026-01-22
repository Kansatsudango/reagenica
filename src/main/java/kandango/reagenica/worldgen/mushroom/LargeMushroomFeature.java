package kandango.reagenica.worldgen.mushroom;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LargeMushroomFeature extends Feature<LargeMushroomConfig>{

  public LargeMushroomFeature(Codec<LargeMushroomConfig> codec) {
    super(codec);
  }

  @Override
  public boolean place(@Nonnull FeaturePlaceContext<LargeMushroomConfig> ctx) {
    WorldGenLevel lv = ctx.level();
    BlockPos origin = ctx.origin();
    RandomSource random = ctx.random();
    LargeMushroomConfig config = ctx.config();

    final int height = config.height().sample(random);

    for(int y=0 ; y<height ; y++){
      BlockPos pos = origin.above(y);
      setBlock(lv, pos, config.stem().getState(random, pos));
      for(Direction dir : Direction.Plane.HORIZONTAL){
        setBlock(lv, pos.relative(dir), config.stem().getState(random, pos));
      }
    }
    final int capHeight = height/2+2;
    for(int y=0 ; y<capHeight ; y++){
      boolean isLowest = (y==capHeight-1);
      int offset = height-y+1;
      int radius = isLowest ? y : y+1;
      BlockPos center = origin.above(offset);
      for(int dx=-radius;dx<=radius;dx++){
        for(int dz=-radius;dz<=radius;dz++){
          final BlockPos pos = center.offset(dx,0,dz);
          getStateForPlace(dx, dz, radius, pos, random, config, isLowest).ifPresent(state -> {
            setBlock(lv, pos, state);
          });
        }
      }
    }
    return true;
  }
  private Optional<BlockState> getStateForPlace(int dx, int dz, int radius, BlockPos pos, RandomSource random, LargeMushroomConfig cfg, boolean isFinalLevel){
    int sqDistance = dx*dx+dz*dz;
    int sqRad = radius*radius;
    int sqUpRad = (radius-1)*(radius-1);
    if(sqDistance <= sqUpRad){
      if(!isFinalLevel){
        return Optional.of(cfg.inside().getState(random, pos));
      }else{
        return Optional.empty();
      }
    }else if(sqDistance <= sqRad){
      return Optional.of(cfg.surface().getState(random, pos));
    }else{
      return Optional.empty();
    }
  }
}
