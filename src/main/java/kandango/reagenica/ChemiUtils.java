package kandango.reagenica;

import java.util.Optional;

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
}
