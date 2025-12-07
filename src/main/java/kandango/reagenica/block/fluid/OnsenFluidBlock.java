package kandango.reagenica.block.fluid;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class OnsenFluidBlock extends LiquidBlock{
  private final MobEffectInstance effect;
  public OnsenFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties props, MobEffectInstance effect){
    super(fluid,props);
    this.effect = effect;
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
