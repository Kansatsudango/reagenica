package kandango.reagenica.utils;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

public class ComponentUtil {
  public static Component rainbowLine(String line, @Nullable Level level, int speed, int step){
    MutableComponent component = Component.literal("");
    long tick = Optional.ofNullable(level).map(lv -> (lv.getGameTime()*speed)).orElse(0L);
    int[] codePoints = line.codePoints().toArray();
    for(int i=0;i<codePoints.length;i++){
      String ch = new String(Character.toChars(codePoints[i]));
      final int index = i;
      component.append(Component.literal(ch).withStyle(style -> style.withColor(toGaming(tick+index*step))));
    }
    return component;
  }
  public static int toGaming(long time){
    int tick = (int)time%1536;
    int grade = tick%256;
    if(tick<256){
      return colorOf(255, grade, 0);
    }else if(tick<512){
      return colorOf(255-grade, 255, 0);
    }else if(tick<768){
      return colorOf(0, 255, grade);
    }else if(tick<1024){
      return colorOf(0, 255-grade, 255);
    }else if(tick<1280){
      return colorOf(grade, 0, 255);
    }else if(tick<1536){
      return colorOf(255, 0, 255-grade);
    }else{
      return 0;
    }
  }
  private static int colorOf(int r, int g, int b){
    return r + g*0x100 + b*0x10000;
  }
}
