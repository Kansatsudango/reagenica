package kandango.reagenica.block.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class BlastFurnaceSubBlockEntity extends BlockEntity {
    public BlastFurnaceSubBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLAST_FURNACE_SUB.get(), pos, state);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
      Level lv = this.level;
      if (lv == null || worldPosition == null) return LazyOptional.empty();

      BlockPos below = worldPosition.below();
      BlockEntity be = lv.getBlockEntity(below);
      if (be != null && be instanceof BlastFurnaceBlockEntity) {
        return be.getCapability(cap, Direction.UP);
      }else{
        BlockPos belowbelow = worldPosition.below().below();
        BlockEntity bbe = lv.getBlockEntity(belowbelow);
        if (bbe != null && bbe instanceof BlastFurnaceBlockEntity) {
            return bbe.getCapability(cap, Direction.UP);
        }
      }
      return LazyOptional.empty();
    }
}
