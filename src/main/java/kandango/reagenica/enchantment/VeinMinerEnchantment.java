package kandango.reagenica.enchantment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.world.ChemiCapabilities;
import kandango.reagenica.world.task.ChainMiningTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class VeinMinerEnchantment extends Enchantment{
  public static final int MAX_SEARCH = 257;

  public VeinMinerEnchantment() {
    super(Rarity.RARE, ChemiEnchantments.IRIDIUM_DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
  }

  @Override
  public int getMaxLevel() {
    return 4;
  }

  @Override
  protected boolean checkCompatibility(@Nonnull Enchantment other) {
    return super.checkCompatibility(other)
            && other != ChemiEnchantments.BIG_MINING.get();
  }

  public static void run(ServerLevel slv, ServerPlayer player, BlockPos origin, ItemStack tool, int enchLevel){
    final Block originBlock = slv.getBlockState(origin).getBlock();
    Queue<BlockPos> resultPos = new ArrayDeque<>();
    Queue<BlockPos> searchQueue = new ArrayDeque<>();
    Set<BlockPos> visited = new HashSet<>();

    searchQueue.add(origin);
    visited.add(origin);

    while (!searchQueue.isEmpty() && resultPos.size() < MAX_SEARCH) {
      BlockPos pos = searchQueue.poll();
      resultPos.add(pos);
      for(Vec3i vec : relatives()){
        BlockPos neighbor = pos.offset(vec);
        if(visited.contains(neighbor))continue;
        Block neighborBlock = slv.getBlockState(neighbor).getBlock();
        if(neighborBlock == originBlock){
          visited.add(neighbor);
          searchQueue.add(neighbor);
        }
      }
    }

    if(resultPos.size() == MAX_SEARCH){ // Too many connected
      player.displayClientMessage(Component.translatable("chat.reagenica.too_many_to_vein"), true);
    }else{
      tool.hurtAndBreak(resultPos.size(), player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
      ArrayDeque<BlockPos> queue = new ArrayDeque<>();
      ChemiUtils.cutoffQueue(resultPos, queue, canMine(enchLevel));
      @Nullable final ServerLevel overworld = slv.getServer().getLevel(Level.OVERWORLD);
      if(overworld!=null){
        overworld.getCapability(ChemiCapabilities.CHAIN_MINING_DATA).ifPresent(data -> data.add(slv, new ChainMiningTask(player, queue)));
      }
    }
  }

  private static int canMine(int level){
    return switch(level){
      case 1 -> 10;
      case 2 -> 32;
      case 3 -> 64;
      case 4 -> 256;
      default -> 256;
    };
  }

  // private static int manhattan(BlockPos p, BlockPos q){
  //   return Math.abs(p.getX()-q.getX()) + Math.abs(p.getY()-q.getY()) + Math.abs(p.getZ()-q.getZ());
  // }

  private static List<Vec3i> relatives(){
    List<Vec3i> list = new ArrayList<>();
    for(int x=-1;x<=1;x++){
      for(int y=-1;y<=1;y++){
        for(int z=-1;z<=1;z++){
          if(!(x==0 && y==0 && z==0)){
            list.add(new Vec3i(x, y, z));
          }
        }
      }
    }
    return list;
  }
}
