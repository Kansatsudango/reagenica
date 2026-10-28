package kandango.reagenica.item;

import kandango.reagenica.ChemiItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Testtube extends Item{
  public Testtube(Properties p) {
    super(p);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level lv, Player player, InteractionHand hand){
    ItemStack stack = player.getItemInHand(hand);
    HitResult hitResult = player.pick(5.0D, 0.0F, false);
    if(hitResult.getType() == HitResult.Type.BLOCK){
      BlockHitResult blockHit = (BlockHitResult) hitResult;
      BlockPos pos = blockHit.getBlockPos();
      BlockState state = lv.getBlockState(pos);
      if (state.getFluidState().is(FluidTags.WATER)) {
        if(player instanceof ServerPlayer sp){
          stack.shrink(1);
          sp.getInventory().placeItemBackInInventory(new ItemStack(ChemiItems.WATER_TUBE.get(),1));
          if(lv instanceof ServerLevel slv){
            slv.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS);
          }
        }
        return InteractionResultHolder.sidedSuccess(stack, lv.isClientSide);
      }
    }
    return InteractionResultHolder.pass(stack);
  }
  
}
