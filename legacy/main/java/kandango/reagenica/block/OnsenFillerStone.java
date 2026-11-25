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

public class OnsenFillerStone extends OnsenFiller{
  private static final Map<Direction,VoxelShape> SHAPES = ShapeStream.create(1, 0, 0, 15, 1, 15)
                                                                      .add(1, 1, 3, 15, 10, 13)
                                                                      .createRots(Direction.Plane.HORIZONTAL);
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    Direction facing = state.getValue(FACING);
    return SHAPES.get(facing);
  }
  @Override
  public Collection<ChemiGeometry> waterFlows() {
    return List.of(ChemiGeometry.createPix(5, 2.01, 2, 9, 3, 8)
                   ,ChemiGeometry.createPix(4, 1.01, -2, 12, 3, 3)
                   ,ChemiGeometry.createPix(4, -2, -2, 12, 1.01, -0.5));
  }
}
