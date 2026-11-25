package kandango.reagenica.worldgen.crater;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.worldgen.ChemiStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class MegaCraterPiece extends StructurePiece{
  private static final int RADIUS = 60;
  private final BlockPos center;

  public MegaCraterPiece(BlockPos center){
    super(ChemiStructures.MEGA_CRATER_PIECE.get(), 0, new BoundingBox(
                center.getX() - RADIUS,
                0,
                center.getZ() - RADIUS,
                center.getX() + RADIUS,
                256,
                center.getZ() + RADIUS));
    this.center = center;
  }
  public MegaCraterPiece(StructurePieceSerializationContext context, CompoundTag tag) {
    super(ChemiStructures.MEGA_CRATER_PIECE.get(), tag);
    this.center = BlockPos.of(tag.getLong("Center"));
  }

  @Override
  protected void addAdditionalSaveData(@Nonnull StructurePieceSerializationContext context, @Nonnull CompoundTag tag) {
    tag.putLong("Center", center.asLong());
  }

  @Override
  public void postProcess(@Nonnull WorldGenLevel level,
                        @Nonnull StructureManager manager,
                        @Nonnull ChunkGenerator generator,
                        @Nonnull RandomSource random,
                        @Nonnull BoundingBox box,
                        @Nonnull ChunkPos chunkPos,
                        @Nonnull BlockPos pivot) {
    final BlockPos cube_center = center.above(15);
    final int CENTRAL_PEAK_RADIUS = RADIUS/2;
    final int DIRT_LEVEL = RADIUS*2/3;
    for(int x=-RADIUS;x<=RADIUS;x++){
      for(int z=-RADIUS;z<=RADIUS;z++){
        if(x*x+z*z > RADIUS*RADIUS)continue;
        final int depth = (int) Math.sqrt(RADIUS * RADIUS - x * x - z * z);
        int finalDepth = depth;
        for (int y = -40; y <= depth; y++) {
          BlockPos pos = cube_center.offset(x, -y, z);
          if (!box.isInside(pos)) continue;
          if(y!=depth)level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
          else if(!level.getBlockState(pos.below()).isAir()) level.setBlock(pos, craterStone(random), 2);
        }
        if(x*x + z*z <= CENTRAL_PEAK_RADIUS*CENTRAL_PEAK_RADIUS){
          int peakHeight = (int)Math.sqrt(CENTRAL_PEAK_RADIUS*CENTRAL_PEAK_RADIUS - x*x - z*z);
          for(int y=depth;y>=depth-peakHeight;y--){
            BlockPos pos = cube_center.offset(x, -y, z);
            if (!box.isInside(pos)) continue;
            level.setBlock(pos, craterStone(random), 2);
          }
          finalDepth = depth-peakHeight;
        }
        if(finalDepth > DIRT_LEVEL){
          for(int y=finalDepth-1; y>=DIRT_LEVEL; y--){
            BlockPos pos = cube_center.offset(x,-y,z);
            if (!box.isInside(pos)) continue;
            if(y==DIRT_LEVEL){
              level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
              BlockPos above = pos.above();
              if(!box.isInside(above))continue;
              level.setBlock(above, dirtVegetation(random), 2);
            }else{
              level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            }
          }
        }
      }
    }
  }
  
  private BlockState craterStone(RandomSource rand){
    float roll = rand.nextFloat();
    if(roll<0.98){
      if(roll<0.3)return Blocks.DEEPSLATE.defaultBlockState();
      else if(roll<0.6)return Blocks.ANDESITE.defaultBlockState();
      else return Blocks.TUFF.defaultBlockState();
    }else{
      return ChemiBlocks.IRIDIUM_ORE.get().defaultBlockState();
    }
  }
  private BlockState dirtVegetation(RandomSource rand){
    float roll = rand.nextFloat();
    if(roll<0.2)return Blocks.GRASS_BLOCK.defaultBlockState();
    else if(roll<0.4)return Blocks.GRASS.defaultBlockState();
    else if(roll<0.5)return Blocks.FERN.defaultBlockState();
    else if(roll<0.7)return ChemiBlocks.ANCESTOR_ASTERACEAE.get().defaultBlockState();
    else return Blocks.AIR.defaultBlockState();
  }
}
