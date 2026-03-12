package kandango.reagenica;

import javax.annotation.Nonnull;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class ChemiGameRules {
  public static void init(){}

  public static final GameRules.Key<GameRules.BooleanValue> DO_ONSEN_GENERATE =
                GameRules.register("doOnsenGenerate", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
  public static boolean isOnsenGenerate(@Nonnull Level lv){
    return lv.getGameRules().getRule(DO_ONSEN_GENERATE).get();
  }
  public static final GameRules.Key<GameRules.BooleanValue> ALLOW_COMPASS =
                GameRules.register("allowLocationCompassUse", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
  public static boolean isCompassAllowed(@Nonnull Level lv){
    return lv.getGameRules().getRule(ALLOW_COMPASS).get();
  }
}
