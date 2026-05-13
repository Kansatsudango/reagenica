package kandango.reagenica.item.farming;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.farming.MushroomBed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MushroomFertilizer extends Item{
  public MushroomFertilizer(){
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
    if(isMushroomBed(clickedBlock.getBlock()))farmpos=pos;
    else if(isMushroomBed(clickedBlockBelow.getBlock()))farmpos=pos.below();
    if(farmpos!=null){
      for(int x=-2;x<=2;x++){
        for(int z=-2;z<=2;z++){
          BlockPos currentpos = farmpos.offset(x,0,z);
          BlockState current = level.getBlockState(currentpos);
          if(isMushroomBed(current.getBlock())){
            BlockState fertilized = ChemiBlocks.MUSHROOM_BED.get().defaultBlockState().setValue(MushroomBed.FERTILIZED, 3);
            level.setBlock(currentpos, fertilized, 3);
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

  private boolean isMushroomBed(Block block){
    return block == ChemiBlocks.MUSHROOM_BED.get();
  }
}
