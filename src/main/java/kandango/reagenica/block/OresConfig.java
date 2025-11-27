package kandango.reagenica.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class OresConfig {
  public static Block deepslateOres(){
    return new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DEEPSLATE)
            .strength(4.5f, 6.0f)
            .sound(SoundType.DEEPSLATE)
            .requiresCorrectToolForDrops());
  }
  public static Block stoneOres(){
    return new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0f, 6.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops());
  }
}
