package kandango.reagenica.block;

import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Yunohana extends MultifaceBlock {
  private final MultifaceSpreader spreader;

  public Yunohana(){
    super(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().sound(SoundType.SAND));
    this.spreader = new MultifaceSpreader(this);
  }

  @Override
  public MultifaceSpreader getSpreader() {
    return this.spreader;
  }
  
}
