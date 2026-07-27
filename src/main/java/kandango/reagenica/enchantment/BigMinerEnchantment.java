package kandango.reagenica.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BigMinerEnchantment{
  public static void run(ServerLevel slv, ServerPlayer player, BlockPos origin, ItemStack tool, int enchLevel){
    if(enchLevel>5){
      enchLevel=5; // safety guard when overenchanted by command
    }
    for(int x=-enchLevel;x<=enchLevel;x++){
      for(int y=-enchLevel;y<=enchLevel;y++){
        for(int z=-enchLevel;z<=enchLevel;z++){
          BlockPos pos = origin.offset(x, y, z);
          if(!pos.equals(origin)){
            BlockState state = slv.getBlockState(pos);
            if(state.getDestroySpeed(slv, pos)<0)continue;
            if(!player.hasCorrectToolForDrops(state, slv, pos))continue;
            player.gameMode.destroyBlock(pos);
          }
        }
      }
    }
  }
}
