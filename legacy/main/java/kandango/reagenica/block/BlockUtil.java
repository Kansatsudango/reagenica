package kandango.reagenica.block;

import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.ITickableBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockUtil {
  public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
    return level.isClientSide
    ? null
      : (lvl, pos, st, be) -> {
        if (be instanceof ITickableBlockEntity tickable) {
          try{
            tickable.serverTick();
          }catch(Exception e){
            ChemistryMod.LOGGER.error("Exception caught while ticking block entity!", e);
            level.setBlockAndUpdate(pos, ChemiBlocks.BUGGED_BLOCK.get().defaultBlockState());
          }
        }
      };
  }
  public static <T extends Comparable<T>> Optional<T> getStatus(BlockState state, Property<T> prop){
    if(state.hasProperty(prop)){
      return Optional.of(state.getValue(prop));
    }else{
      return Optional.empty();
    }
  }
}
