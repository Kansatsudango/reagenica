package kandango.reagenica.block.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class FractionalDistillerSubBlockEntity extends BlockEntity {
    public FractionalDistillerSubBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FRACTIONAL_DISTILLER_SUB.get(), pos, state);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
      Level lv = this.level;
      if (lv == null || worldPosition == null) return LazyOptional.empty();

      BlockPos below = worldPosition.below();
      BlockEntity be = lv.getBlockEntity(below);
      if (be != null) {
          return be.getCapability(cap, Direction.UP);
      }
      return LazyOptional.empty();
    }
}
