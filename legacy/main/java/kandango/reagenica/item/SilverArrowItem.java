package kandango.reagenica.item;

import javax.annotation.Nonnull;

import kandango.reagenica.entity.SilverArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SilverArrowItem extends ArrowItem{
  public SilverArrowItem(){
    super(new Item.Properties().stacksTo(64));
  }

  @Override
  public AbstractArrow createArrow(@Nonnull Level level, @Nonnull ItemStack stack, @Nonnull LivingEntity shooter) {
    return new SilverArrowEntity(level, shooter);
  }
}
