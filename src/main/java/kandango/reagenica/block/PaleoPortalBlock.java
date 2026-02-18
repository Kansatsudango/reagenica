package kandango.reagenica.block;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.world.PaleoTeleporter;
import kandango.reagenica.worldgen.ChemiBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

// Yes, this is the "evil inheritance" we swore to avoid.
// Sometimes engineering reality beats pure object-oriented virtue.
// Sacrifice is an inevitable part of scientific progress.
public class PaleoPortalBlock extends NetherPortalBlock{
  public PaleoPortalBlock(Properties p) {
    super(p);
  }

  @Override
  public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity) {
    if (!level.isClientSide && entity instanceof ServerPlayer player) {
      if (!player.isOnPortalCooldown()) {
        player.setPortalCooldown(200);
        final boolean isXAxe = state.getOptionalValue(NetherPortalBlock.AXIS).map(ax -> ax == Direction.Axis.X).orElse(true);
        Optional.ofNullable(level.getServer()).map(server -> getDestinationLevel(server, level)).ifPresent(
          slv -> player.changeDimension(slv, new PaleoTeleporter(isXAxe))
        );
      }
    }
  }

  @Nullable
  private ServerLevel getDestinationLevel(MinecraftServer server, Level current){
    var dimension = current.dimension();
    if(dimension == Level.OVERWORLD){
      return server.getLevel(ChemiBiomes.PALEO_LEVEL);
    }else if(dimension == ChemiBiomes.PALEO_LEVEL){
      return server.getLevel(Level.OVERWORLD);
    }else{
      return null;
    }
  }
  
}
