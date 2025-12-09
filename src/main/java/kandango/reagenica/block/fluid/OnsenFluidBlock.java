package kandango.reagenica.block.fluid;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.Yunohana;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class OnsenFluidBlock extends LiquidBlock{
  private final MobEffectInstance effect;
  private final Supplier<? extends Yunohana> yunohana;

  public OnsenFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties props, MobEffectInstance effect, Supplier<? extends Yunohana> yunohana){
    super(fluid,props);
    this.effect = effect;
    this.yunohana = yunohana;
  }

  @Override
  public void animateTick(@Nonnull BlockState state, @Nonnull Level lv, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    if(!lv.isClientSide)return;
    if(random.nextFloat()<0.1f){
      double x = pos.getX() + random.nextDouble();
      double y = pos.getY() + 1.0D;
      double z = pos.getZ() + random.nextDouble();
      lv.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0, 0.02, 0.0);
    }
  }

  @Override
  public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel lv, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    for(int x=-1;x<=1;x++){
      for(int z=-1;z<=1;z++){
        if(random.nextInt(4)!=0)continue;
        BlockPos yunohanapos = pos.offset(x, 1, z);
        if(lv.isEmptyBlock(yunohanapos)){
          Yunohana yunohanaBlock = yunohana.get();
          BlockState yunohanastate = yunohanaBlock.defaultBlockState();
          boolean hadBlockNearby=false;
          for(Direction dir : Direction.values()){
            BlockPos rel = yunohanapos.relative(dir);
            BlockState relState = lv.getBlockState(rel);
            if(relState.isFaceSturdy(lv, rel, dir.getOpposite())){
              yunohanastate = yunohanastate.setValue(MultifaceBlock.getFaceProperty(dir), true);
              hadBlockNearby=true;
            }else{
              yunohanastate = yunohanastate.setValue(MultifaceBlock.getFaceProperty(dir), false);
            }
          }
          if(hadBlockNearby){
            lv.setBlock(yunohanapos, yunohanastate, Block.UPDATE_ALL);
          }
        }
      }
    }
    ChemistryMod.LOGGER.info("Ticked {}",pos.toShortString());
  }

  @Override
  public boolean isRandomlyTicking(@Nonnull BlockState state) {
    return true;
  }

  @Override
  public void entityInside(@Nonnull BlockState state, @Nonnull Level lv, @Nonnull BlockPos pos, @Nonnull Entity entity) {
    if(!lv.isClientSide && entity instanceof LivingEntity living){
      living.addEffect(effect);
    }
  }

  @Override
  public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext context) {
    return true;
  }
}
