package kandango.reagenica.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.BushBlock;

public class WildCrops extends BushBlock{
  public static final MapCodec<WildCrops> CODEC = simpleCodec(WildCrops::new);

  public WildCrops(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BushBlock> codec() {
    return CODEC;
  }
  
}
