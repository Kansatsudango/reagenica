package kandango.reagenica.item;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import net.minecraft.server.level.ServerLevel;
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
    if(level instanceof ServerLevel slv){
      try{
        Field field = ServerLevel.class.getDeclaredField("serverLevelData");
        field.setAccessible(true);

        Object data = field.get(slv);
        ChemistryMod.LOGGER.info("ServerLevelData class: {}", data.getClass().getName());
      }catch(Exception e){
        ChemistryMod.LOGGER.error("Exception caught!",e);
      }
    }

    return InteractionResult.SUCCESS;
  }
}
