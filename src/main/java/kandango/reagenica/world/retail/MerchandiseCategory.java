package kandango.reagenica.world.retail;

import java.util.Arrays;
import java.util.Optional;

public enum MerchandiseCategory {
  PRODUCE("produce"),
  MEAT("meat"),
  FISH("fish"),
  DELICA("delica"),
  DAILY("daily"),
  GROCERY("grocery"),
  NONFOODS("nonfoods"),
  NULL("null");
  private final String keyName;
  private MerchandiseCategory(String name){
    this.keyName = name;
  }

  public static Optional<MerchandiseCategory> fromString(String name){
    return Arrays.stream(MerchandiseCategory.values()).filter(c -> c.keyName.equals(name)).findFirst();
  }
}
