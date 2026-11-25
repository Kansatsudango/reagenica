package kandango.reagenica.item;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StackableBowlFoodItem extends Item {
  public StackableBowlFoodItem(Item.Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity){
    super.finishUsingItem(stack, level, entity);
    if(entity instanceof Player player){
      if(!player.getAbilities().instabuild){
        ItemStack bowl = new ItemStack(Items.BOWL);
        if(!player.getInventory().add(bowl)){
          player.drop(bowl, false);
        }
      }
    }
    return stack;
  }
}
