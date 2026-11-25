package kandango.reagenica.worldgen.forestry;

import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import kandango.reagenica.worldgen.ChemiFoliagePlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class TaxodiumTrunkPlacer extends TrunkPlacer{
  public static final Codec<TaxodiumTrunkPlacer> CODEC = 
          RecordCodecBuilder.create(instance -> trunkPlacerParts(instance).apply(instance, TaxodiumTrunkPlacer::new));

  public TaxodiumTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
    super(baseHeight,heightRandA,heightRandB);
  }

  @Override
  protected TrunkPlacerType<?> type() {
    return ChemiFoliagePlacers.TAXODIUM_TRUNK.get();
  }

  @Override
  public List<FoliageAttachment> placeTrunk(@Nonnull LevelSimulatedReader level,
        @Nonnull BiConsumer<BlockPos, BlockState> blockSetter,
        @Nonnull RandomSource random,
        int height,
        @Nonnull BlockPos startPos,
        @Nonnull TreeConfiguration config) {
    BlockPos start = startPos;
    for(int y=0;y<height;y++){
      BlockPos pos = start.above(y);
      placeLog(level, blockSetter, random, pos, config);
      if(y<Math.max(2,height/10)){
        for(Direction dir : Direction.Plane.HORIZONTAL){
          placeLog(level, blockSetter, random, pos.relative(dir), config);
        }
      }
    }
    return List.of(new FoliagePlacer.FoliageAttachment(start.above(height), 0, false));
  }
  
}
