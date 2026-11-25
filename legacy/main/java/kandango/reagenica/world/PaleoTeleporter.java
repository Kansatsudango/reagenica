package kandango.reagenica.world;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.PaleoPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class PaleoTeleporter implements ITeleporter{
  private static final Supplier<BlockState> PORTAL_FRAME = () -> Blocks.OBSIDIAN.defaultBlockState();
  private static final Supplier<BlockState> PORTAL_INSIDE = () -> ChemiBlocks.PALEO_PORTAL.get().defaultBlockState();
  private final boolean isXAxe;

  public PaleoTeleporter(boolean isXAxe){
    this.isXAxe = isXAxe;
  }

  private Optional<BlockPos> findClosestPortal(ServerLevel destWorld, BlockPos origin){
    PoiManager manager = destWorld.getPoiManager();
    return manager.findClosest(
        poi -> poi.is(ChemiPOIs.PALEO_PORTAL_POI.getKey()), 
        origin, 64, PoiManager.Occupancy.ANY)
      .or(() -> findPortalVertically(manager, origin));
  }
  private Optional<BlockPos> findPortalVertically(PoiManager manager, BlockPos origin){
    BlockPos.MutableBlockPos pos = origin.mutable();
    for(int y=-64;y<=320;y+=16){
      BlockPos portal = manager.findClosest(
        poi -> poi.is(ChemiPOIs.PALEO_PORTAL_POI.getKey()), 
        pos.setY(y), 64, PoiManager.Occupancy.ANY).orElse(null);
      if(portal!=null){
        return Optional.of(portal);
      }
    }
    return Optional.empty();
  }

  private BlockPos createPortal(ServerLevel lv, BlockPos origin){
    lv.getChunkAt(origin);
    BlockPos base = lv.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, origin);
    ChemistryMod.LOGGER.debug("Creating portal at : " + base.toShortString());
    for(int y=0;y<5;y++){
      for(int x=0;x<4;x++){
        BlockPos pos = base.offset(isXAxe?x:0, y, isXAxe?0:x);
        if(x==0 || x==3 || y==0 || y==4){
          lv.setBlock(pos, PORTAL_FRAME.get(), 3);
        }
      }
    }
    BlockState state;
    if(isXAxe)state = PORTAL_INSIDE.get().setValue(PaleoPortalBlock.AXIS, Direction.Axis.X);
    else state = PORTAL_INSIDE.get().setValue(PaleoPortalBlock.AXIS, Direction.Axis.Z);
    for(int y=1;y<4;y++){
      for(int x=1;x<3;x++){
        BlockPos pos = base.offset(isXAxe?x:0, y, isXAxe?0:x);
        lv.setBlock(pos, state, 18);
      }
    }
    return base.offset(1, 1, 0);
  }

  @Override
  @Nullable
  public PortalInfo getPortalInfo(Entity entity, ServerLevel destLevel, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
    final BlockPos origin = entity.blockPosition();
    if(entity instanceof ServerPlayer){
      final BlockPos portalPos = findClosestPortal(destLevel, origin).orElseGet(() -> createPortal(destLevel, origin));
      return new PortalInfo(toVec3(getPortalOrigin(destLevel, portalPos)), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
    }else{
      return findClosestPortal(destLevel, origin)
        .map(pos -> getPortalOrigin(destLevel, pos))
        .map(pos -> new PortalInfo(toVec3(pos), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot()))
        .orElse(null);
    }
  }

  private Vec3 toVec3(PosAndAxe pa){
    return new Vec3(pa.pos.getX() + (pa.isXax?1:0.5), pa.pos.getY(), pa.pos.getZ()+ (pa.isXax?0.5:1));
  }
  private PosAndAxe getPortalOrigin(ServerLevel destLevel, BlockPos start){
    BlockPos pos = start;
    boolean isXAxe = destLevel.getBlockState(start).getOptionalValue(PaleoPortalBlock.AXIS)
                              .map(ax -> ax == Direction.Axis.X).orElse(this.isXAxe);
    for(int i=0;i<=2;i++){
      BlockPos below = start.below(i);
      if(destLevel.getBlockState(below).is(ChemiBlocks.PALEO_PORTAL.get())){
        pos = below;
      }else{
        break;
      }
    }
    BlockPos bottom = pos;
    for(int i=0;i<=1;i++){
      BlockPos younger = isXAxe ? bottom.offset(-i, 0,0) : bottom.offset(0, 0, -i);
      if(destLevel.getBlockState(younger).is(ChemiBlocks.PALEO_PORTAL.get())){
        pos = younger;
      }
    }
    return new PosAndAxe(pos, isXAxe);
  }
  private record PosAndAxe(BlockPos pos, boolean isXax) {
  }
}
