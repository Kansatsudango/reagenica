package kandango.reagenica.worldgen.forestry;

import java.util.Optional;

import kandango.reagenica.worldgen.ChemiFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

public class ReagenicaTreeGrowers {
  public static final TreeGrower METASEQUOIA = new TreeGrower("metasequoia", Optional.of(ChemiFeatures.MEGA_METASEQUOIA), Optional.of(ChemiFeatures.METASEQUOIA), Optional.empty());
  public static final TreeGrower TAXODIUM = new TreeGrower("taxodium", Optional.empty(), Optional.of(ChemiFeatures.TAXODIUM), Optional.empty());
  public static final TreeGrower MAGNOLIA = new TreeGrower("magnolia", Optional.empty(), Optional.of(ChemiFeatures.MAGNOLIA), Optional.empty());
  public static final TreeGrower GINKGO = new TreeGrower("ginkgo", Optional.empty(), Optional.of(ChemiFeatures.GINKGO), Optional.empty());
  public static final TreeGrower FICUS = new TreeGrower("ficus", Optional.empty(), Optional.of(ChemiFeatures.FICUS), Optional.empty());
}
