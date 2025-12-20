package kandango.reagenica.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HolidayTree extends Block{
  private static final VoxelShape SHAPE = ShapeStream.create(6,0,6,10,2,10)
                                                     .add(7,2,7,9,3,9)
                                                     .add(3,3,3,13,5,13)
                                                     .add(4,5,4,12,8,12)
                                                     .add(5,8,5,11,11,11)
                                                     .add(6,11,6,10,14,10)
                                                     .add(7,14,7,9,16,9)
                                                     .build();

  public HolidayTree() {
    super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.8F).sound(SoundType.WOOD).noOcclusion());
  }

  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    return SHAPE;
  }
}
