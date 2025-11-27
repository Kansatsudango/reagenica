package kandango.reagenica.item;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.entity.SilverArrowEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SilverBowItem extends BowItem{
  public SilverBowItem(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    boolean hasAmmo = !player.getProjectile(stack).isEmpty();

    if (!hasAmmo) {
      return InteractionResultHolder.fail(stack);
    }

    player.startUsingItem(hand);
    return InteractionResultHolder.consume(stack);
  }

  @Override
  public void releaseUsing(@Nonnull ItemStack bowStack, @Nonnull Level level, @Nonnull LivingEntity user, int timeLeft) {
    if(!(user instanceof Player player)) return;

    ItemStack ammo = player.getProjectile(bowStack);
    if (ammo.isEmpty() || !(ammo.getItem() instanceof SilverArrowItem)) return;

    int charge = this.getUseDuration(bowStack)-timeLeft;
    float velocity = BowItem.getPowerForTime(charge);
    if(velocity < 0.1f)return;

    if(!level.isClientSide){
      SilverArrowEntity arrow = new SilverArrowEntity(level,player);
      arrow.setBaseDamage(5.0d);
      arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);
      bowStack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
      level.addFreshEntity(arrow);
      if (!player.getAbilities().instabuild) {
        ammo.shrink(1);
        if (ammo.isEmpty()) {
          player.getInventory().removeItem(ammo);
        }
      }
    }
    player.awardStat(Stats.ITEM_USED.get(this));
  }

  @Override
  public Predicate<ItemStack> getAllSupportedProjectiles() {
    return stack -> stack.getItem() == ChemiItems.SILVER_ARROW.get();
  }
}
