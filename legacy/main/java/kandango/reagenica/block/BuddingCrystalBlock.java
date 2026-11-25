package kandango.reagenica.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BuddingCrystalBlock extends Block{
  private final Supplier<Block> bud;
  private final Supplier<Block> crystal;
  public BuddingCrystalBlock(Properties p, Supplier<Block> bud, Supplier<Block> crystal) {
    super(p);
    this.bud = bud;
    this.crystal = crystal;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    super.randomTick(state, level, pos, random);
    if (!level.isAreaLoaded(pos, 1)) return;
    List<Direction> crystalable = new ArrayList<>();
    for(Direction dir : Direction.values()){
      BlockState nbs = level.getBlockState(pos.relative(dir));
      if(nbs.isAir() || nbs.is(bud.get())){
        crystalable.add(dir);
      }
    }
    if(!crystalable.isEmpty()){
      int count = crystalable.size();
      if(random.nextInt(20) < 10-count){
        Direction growing = crystalable.get(random.nextInt(crystalable.size()));
        BlockPos growpos = pos.relative(growing);
        BlockState current = level.getBlockState(growpos);
        if(current.isAir()){
          BlockState budstate = getCrystal(bud.get(), growing);
          level.setBlockAndUpdate(growpos, budstate);
        }else{
          BlockState crystalState = getCrystal(crystal.get(), growing);
          level.setBlockAndUpdate(growpos, crystalState);
        }
      }
    }
  }
  private BlockState getCrystal(Block block, Direction dir){
    BlockState crystal = block.defaultBlockState();
    if(crystal.hasProperty(BlockStateProperties.FACING));
    BlockState rotated = crystal.setValue(BlockStateProperties.FACING, dir);
    return rotated;
  }
}
