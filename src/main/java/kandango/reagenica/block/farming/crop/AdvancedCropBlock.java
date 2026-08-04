package kandango.reagenica.block.farming.crop;

import java.util.function.DoubleUnaryOperator;
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
  private final DoubleUnaryOperator bestTemperature;
  public AdvancedCropBlock(Supplier<ItemLike> seed, DoubleUnaryOperator bestTemperature) {
    super(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP));
    this.bestTemperature = bestTemperature;
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
        float temp = serverlevel.getBiome(pos).get().getBaseTemperature();
        f = f * (float)bestTemperature.applyAsDouble(temp);
        float probability = f <= 0.0F ? 0.0F : f / (25.0F + f);
        if (ForgeHooks.onCropsGrowPre(serverlevel, pos, state, rand.nextFloat() < probability)) {
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
      ans /= 3;
    }
    return ans;
  }

  public static class ClimateModifications{
    public static final DoubleUnaryOperator Frigid = t -> {
      if(t<0.15) return 2.0;
      else return Math.max(2.0-(t-0.15)*3, 1.0);
    };
    public static final DoubleUnaryOperator SubArctic = t -> {
      if(t<0.15) return Math.max(2.0-(0.15-t)*2, 1.0);
      if(t<0.4) return 2.0;
      else return Math.max(2.0-(t-0.4)*5, 1.0);
    };
    public static final DoubleUnaryOperator Temperate = t -> {
      if(t<0.4) return Math.max(2.0-(0.4-t)*3, 1.0);
      if(t<0.8) return 2.0;
      else return Math.max(2.0-(t-0.8)*3, 1.0);
    };
    public static final DoubleUnaryOperator Arid = t -> {
      if(t<0.8) return Math.max(2.0-(0.8-t)*4, 1.0);
      else return 2.0;
    };
    public static final DoubleUnaryOperator None = t -> 1.0;

  }
}
