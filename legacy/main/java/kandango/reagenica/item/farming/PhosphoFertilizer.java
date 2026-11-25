package kandango.reagenica.item.farming;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.BlockUtil;
import kandango.reagenica.block.farming.AdvancedFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PhosphoFertilizer extends Item{
  public PhosphoFertilizer(){
    super(new Item.Properties().stacksTo(64));
  }

  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    Player player = context.getPlayer();
    ItemStack itemStack = context.getItemInHand();
    BlockState clickedBlock = level.getBlockState(pos);
    BlockState clickedBlockBelow = level.getBlockState(pos.below());
    @Nullable BlockPos farmpos = null;
    if(isFarm(clickedBlock.getBlock()))farmpos=pos;
    else if(isFarm(clickedBlockBelow.getBlock()))farmpos=pos.below();
    if(farmpos!=null){
      for(int x=-2;x<=2;x++){
        for(int z=-2;z<=2;z++){
          BlockPos currentpos = farmpos.offset(x,0,z);
          BlockState current = level.getBlockState(currentpos);
          if(isFarm(current.getBlock())){
            int moist = BlockUtil.getStatus(current, FarmBlock.MOISTURE).or(() -> BlockUtil.getStatus(current, AdvancedFarmland.MOISTURE)).orElse(0);
            if(BlockUtil.getStatus(current, AdvancedFarmland.FERTILIZED).map(f -> f<2).orElse(true)){
              BlockState fertilized = ChemiBlocks.ADVANCED_FARMLAND.get().defaultBlockState().setValue(AdvancedFarmland.FERTILIZED, 1).setValue(AdvancedFarmland.MOISTURE, moist);
              level.setBlock(currentpos, fertilized, 3);
            }
          }
        }
      }
      if (player != null && !player.getAbilities().instabuild) {
        itemStack.shrink(1);
      }
      return InteractionResult.SUCCESS;
    }else{
      return InteractionResult.PASS;
    }
  }

  private boolean isFarm(Block block){
    return block == Blocks.FARMLAND || block == ChemiBlocks.ADVANCED_FARMLAND.get();
  }
}
