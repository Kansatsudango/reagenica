package kandango.reagenica.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TaxodiumRoot extends Block{
  private static final VoxelShape SHAPE = ShapeStream.create(5, 0, 5, 11, 12, 11).build();

  public TaxodiumRoot() {
    super(BlockBehaviour.Properties.of().dynamicShape().strength(1.0f).offsetType(OffsetType.XZ).sound(SoundType.MANGROVE_ROOTS));
  }
  
  @Override
  public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
    Vec3 vec3 = state.getOffset(getter, pos);
    return SHAPE.move(vec3.x, vec3.y, vec3.z);
  }
}
