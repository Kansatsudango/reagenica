package kandango.reagenica.item;

import javax.annotation.Nonnull;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MagicalRod extends Item{
  public MagicalRod(){
    super(new Item.Properties().stacksTo(64));
  }

  @SuppressWarnings("unused")
  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    System.out.println("Debug Rod:");

    return InteractionResult.SUCCESS;
  }
}
