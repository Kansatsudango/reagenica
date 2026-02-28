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
    final BlockPos portalPos = findClosestPortal(destLevel, origin).orElseGet(() -> createPortal(destLevel, origin));
    return new PortalInfo(toVec3(portalPos), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
  }

  private Vec3 toVec3(BlockPos pos){
    return new Vec3(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
  }
}
