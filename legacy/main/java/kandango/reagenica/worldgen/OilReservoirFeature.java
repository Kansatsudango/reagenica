package kandango.reagenica.worldgen;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class OilReservoirFeature extends Feature<NoneFeatureConfiguration>{
  public OilReservoirFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel lv = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    int radius = 8 + random.nextInt(8);

    for(int x = -radius; x <= radius ; x++){
      for(int y = -radius; y <= radius ; y++){
        for(int z = -radius; z <= radius ; z++){
          double powdist = x*x+y*y+z*z;
          BlockPos target = pos.offset(x,y,z);

          if(powdist <= radius*radius){
            if (powdist < (radius - 1) * (radius - 1)) {
              if(y<radius/2){
                lv.setBlock(target, ChemiFluids.CRUDE_OIL.getBlock().defaultBlockState(), 2);
              }else{
                lv.setBlock(target, Blocks.AIR.defaultBlockState(), 2);
              }
            }else{
              if(random.nextInt(100)<20){
                lv.setBlock(target, ChemiBlocks.OILSAND_DEEPSLATE_ORE.get().defaultBlockState(), 2);
              }else{
                lv.setBlock(target, Blocks.DEEPSLATE.defaultBlockState(), 2);
              }
            }
          }
        }
      }
    }

    return true;
  }
}
