package kandango.reagenica.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChemiSignBlockEntity extends SignBlockEntity{
  public ChemiSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

  @Override
	public BlockEntityType<?> getType() {
		return ModBlockEntities.SIGN.get();
	}
}
