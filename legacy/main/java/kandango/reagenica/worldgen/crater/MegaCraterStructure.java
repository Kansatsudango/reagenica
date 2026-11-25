package kandango.reagenica.worldgen.crater;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import kandango.reagenica.worldgen.ChemiStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class MegaCraterStructure extends Structure{
  public static final Codec<MegaCraterStructure> CODEC =
        simpleCodec(MegaCraterStructure::new);

  public MegaCraterStructure(StructureSettings settings) {
    super(settings);
  }
  @Override
  public StructureType<?> type() {
    return ChemiStructures.MEGA_CRATER.get();
  }
  @Override
  protected Optional<GenerationStub> findGenerationPoint(@Nonnull GenerationContext context) {
    ChunkPos chunkPos = context.chunkPos();
    int x = chunkPos.getMiddleBlockX();
    int z = chunkPos.getMiddleBlockZ();
    int y = context.chunkGenerator()
      .getFirstFreeHeight(x, z,
          Heightmap.Types.WORLD_SURFACE_WG,
          context.heightAccessor(),
          context.randomState());

    BlockPos center = new BlockPos(x, y, z);
    return Optional.of(new GenerationStub(center, builder ->
        builder.addPiece(new MegaCraterPiece(center))
    ));
  }
}
