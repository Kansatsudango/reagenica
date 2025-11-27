package kandango.reagenica.block.entity.util;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LitUtil {
  public static void setLit(boolean lit, @Nonnull Level level, @Nonnull BlockPos pos){
    BlockState state = level.getBlockState(pos);
    if(state.hasProperty(BlockStateProperties.LIT)){
      boolean currentlyLit = state.getValue(BlockStateProperties.LIT);
      if(currentlyLit!=lit){
        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, lit));
      }
    }else{
      throw new IllegalStateException("Block at "+pos.toString()+" does not have LIT flag!");
    }
  }
}
