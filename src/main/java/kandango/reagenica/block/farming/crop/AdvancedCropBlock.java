package kandango.reagenica.block.farming.crop;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import kandango.reagenica.block.farming.AdvancedFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class AdvancedCropBlock extends CropBlock{
  private final Supplier<ItemLike> seed;
  public AdvancedCropBlock(Supplier<ItemLike> seed) {
    super(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP));
    this.seed = seed;
  }
  
  protected ItemLike getBaseSeedId(){
    return seed.get();
  }

  // Based on net.minecraft.world.level.block.CropBlock#randomTick
  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel serverlevel, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
    if (!serverlevel.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
    if (serverlevel.getRawBrightness(pos, 0) >= 9) {
      int i = this.getAge(state);
      if (i < this.getMaxAge()) {
        float f = getGrowthSpeed(this, serverlevel, pos);
        f = modifySpeed(f, pos, serverlevel);
        if (ForgeHooks.onCropsGrowPre(serverlevel, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
          serverlevel.setBlock(pos, this.getStateForAge(i + 1), 2);
          ForgeHooks.onCropsGrowPost(serverlevel, pos, state);
        }
      }
    }
  }
  protected float modifySpeed(float origin, BlockPos pos, BlockGetter getter){
    float ans = origin;
    BlockPos blockpos = pos.below();
    BlockState blockstate = getter.getBlockState(blockpos);
    if(blockstate.hasProperty(AdvancedFarmland.FERTILIZED)){
      int fertilizedLevel = blockstate.getValue(AdvancedFarmland.FERTILIZED);
      ans += (float)fertilizedLevel/5.0f;
    }else{
      ans /= 4;
    }
    return ans;
  }
}
