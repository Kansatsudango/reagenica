package kandango.reagenica.block.sign;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ChemiStandingSignBlock extends StandingSignBlock{
  public ChemiStandingSignBlock(Block.Properties p, WoodType type){
    super(p, type);
  }
  
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return ModBlockEntities.SIGN.get().create(pos, state);
	}
}
