package kandango.reagenica.worldgen.mushroom;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SmallMushroomFeature extends Feature<SmallMushroomConfig>{

  public SmallMushroomFeature(Codec<SmallMushroomConfig> codec) {
    super(codec);
  }

  @Override
  public boolean place(@Nonnull FeaturePlaceContext<SmallMushroomConfig> ctx) {
    WorldGenLevel lv = ctx.level();
    BlockPos origin = ctx.origin();
    RandomSource random = ctx.random();
    SmallMushroomConfig config = ctx.config();

    setBlock(lv, origin, config.stem().getState(random, origin));

    for(int x=-1;x<=1;x++){
      for(int z=-1;z<=1;z++){
        BlockPos pos = origin.offset(x, 1, z);
        if(x==0 && z==0){
          setBlock(lv, pos, config.inside().getState(random, pos));
        }else{
          setBlock(lv, pos, config.surface().getState(random, pos));
        }
      }
    }

    BlockPos above = origin.above(2);
    setBlock(lv, above, config.surface().getState(random, above));
    for(Direction dir : Direction.Plane.HORIZONTAL){
      BlockPos pos = above.relative(dir);
      setBlock(lv, pos, config.surface().getState(random, pos));
    }
    return true;
  }
}
