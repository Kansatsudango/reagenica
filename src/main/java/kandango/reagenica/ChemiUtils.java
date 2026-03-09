package kandango.reagenica;

import java.util.Optional;
import java.util.Queue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChemiUtils {
  @Nonnull public static <T> Optional<T> nonNullOrLog(@Nullable T obj){
    if(obj==null){
      ChemistryMod.LOGGER.error("No null allowed here! ",new IllegalStateException());
      return Optional.empty();
    }
    return Optional.of(obj);
  }

  public static <T> void cutoffQueue(Queue<? extends T> origin, Queue<? super T> dest, int size){
    for(int i=0;i<size;i++){
      T element = origin.poll();
      if(element==null)return;
      dest.add(element);
    }
  }
}
