package kandango.reagenica.block;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import kandango.reagenica.block.entity.electrical.ElectricCableCopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CableCopper extends CableAbstract {

  public CableCopper() {
    super(BlockBehaviour.Properties.of().noOcclusion().strength(0.1f).isRedstoneConductor((state, world, pos) -> false));
  }

  @Nullable
  @Override
  public ElectricCableCopperBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
    return new ElectricCableCopperBlockEntity(pos, state);
  }

  @Override
  public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
    return true;
  }
}
