package kandango.reagenica.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Yunohana extends MultifaceBlock {
  private final MultifaceSpreader spreader;
  public static final MapCodec<Yunohana> CODEC = simpleCodec(Yunohana::new);

  public Yunohana(Properties p){
    super(p);
    this.spreader = new MultifaceSpreader(this);
  }
  public Yunohana(){
    super(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().sound(SoundType.SAND));
    this.spreader = new MultifaceSpreader(this);
  }

  @Override
  public MultifaceSpreader getSpreader() {
    return this.spreader;
  }

  @Override
  protected MapCodec<? extends MultifaceBlock> codec() {
    return CODEC;
  }
  
}
