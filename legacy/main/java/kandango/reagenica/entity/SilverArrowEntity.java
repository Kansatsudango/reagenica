package kandango.reagenica.entity;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiEntities;
import kandango.reagenica.ChemiItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class SilverArrowEntity extends AbstractArrow{
  public SilverArrowEntity(EntityType<? extends SilverArrowEntity> type, Level level){
    super(type,level);
  }

  public SilverArrowEntity(Level level, LivingEntity shooter){
    super(ChemiEntities.SILVER_ARROW.get(), shooter, level);
  }

  @Override
  protected ItemStack getPickupItem() {
    return new ItemStack(ChemiItems.SILVER_ARROW.get());
  }

  @Override
  protected void onHitEntity(@Nonnull EntityHitResult result) {
    super.onHitEntity(result);
    if (result.getEntity() instanceof Warden) {
      DamageSource source = this.level().damageSources().arrow(this, this.getOwner());
      result.getEntity().hurt(source, 1000.0f);
    }
  }
  
}
