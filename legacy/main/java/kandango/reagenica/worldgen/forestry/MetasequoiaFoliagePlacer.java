package kandango.reagenica.worldgen.forestry;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import kandango.reagenica.worldgen.ChemiFoliagePlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class MetasequoiaFoliagePlacer extends FoliagePlacer{
  public static final Codec<MetasequoiaFoliagePlacer> CODEC = 
        RecordCodecBuilder.create(
          instance -> foliagePlacerParts(instance)
            .and(Codec.INT.fieldOf("height").forGetter(p -> p.height))
            .apply(instance, MetasequoiaFoliagePlacer::new)
        );

  private final int height;
  public MetasequoiaFoliagePlacer(IntProvider radius, IntProvider offset, int height){
    super(radius, offset);
    this.height = height;
  }

  @Override
  protected FoliagePlacerType<?> type() {
    return ChemiFoliagePlacers.METASEQUOIA.get();
  }

  @Override
  protected void createFoliage(@Nonnull LevelSimulatedReader level, @Nonnull FoliageSetter setter, @Nonnull RandomSource random,
      @Nonnull TreeConfiguration config, int trunkHeight, @Nonnull FoliageAttachment attach, int foliageHeight, int foliageRadius,
      int offset) {
    BlockPos basePos = attach.pos().above(2);
    for(int y=0;y<foliageHeight;y++){
      float t = (float)y/(float)foliageHeight;
      int radius = Math.round(foliageRadius * shaped(t));
      BlockPos layerPos = basePos.above(offset-y);
      this.placeLeavesRow(level, setter, random, config, layerPos, radius, 0, false);
    }
  }

  @Override
  public int foliageHeight(@Nonnull RandomSource random, int trunkHeight, @Nonnull TreeConfiguration config) {
    return Math.min(trunkHeight-3, this.height);
  }

  @Override
  protected boolean shouldSkipLocation(@Nonnull RandomSource random, int dx, int y, int dz,
      int radius, boolean large) {
    return dx*dx + dz*dz > radius*radius;
  }
  
  private float shaped(float heightLv){
    if(heightLv < 0.9f){
      return heightLv*1.11f;
    }else{
      return (1-heightLv)*10f;
    }
  }
}
