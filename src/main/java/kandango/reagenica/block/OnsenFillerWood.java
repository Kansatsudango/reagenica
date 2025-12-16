package kandango.reagenica.block;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiGeometry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OnsenFillerWood extends OnsenFiller{
  private static final Map<Direction,VoxelShape> SHAPES = ShapeStream.create(3, 0, 4, 13, 6, 12)
                                                                      .add(2, 6, 0, 14, 10, 14)
                                                                      .add(2,10,5,14,11,14)
                                                                      .createRots(Direction.Plane.HORIZONTAL);
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    Direction facing = state.getValue(FACING);
    return SHAPES.get(facing);
  }
  @Override
  public Collection<ChemiGeometry> waterFlows() {
    return List.of(ChemiGeometry.createPix(3, 7, -2, 13, 8, 13)
                   ,ChemiGeometry.createPix(4, -2, -2, 12, 7, -0.5));
  }
}
