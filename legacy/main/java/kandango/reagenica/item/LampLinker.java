package kandango.reagenica.item;

import javax.annotation.Nonnull;

import kandango.reagenica.block.entity.StackLampBlockEntity;
import kandango.reagenica.block.entity.lamp.ILampController;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LampLinker extends Item{
  public LampLinker(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    if(level instanceof ServerLevel serverLevel){
      Player player=context.getPlayer();
      if(player==null)return InteractionResult.FAIL;
      ItemStack stack=context.getItemInHand();
      CompoundTag tag = stack.getOrCreateTag();
      BlockPos pos=context.getClickedPos();
      BlockEntity be = serverLevel.getBlockEntity(pos);
      if(be instanceof ILampController){
        tag.putLong("LinkTarget", pos.asLong());
        player.displayClientMessage(Component.translatable("chat.reagenica.link_point",pos.toShortString()), true);
        return InteractionResult.SUCCESS;
      }else if(be instanceof StackLampBlockEntity lamp){
        if(!tag.contains("LinkTarget")){
          player.displayClientMessage(Component.translatable("chat.reagenica.link_null"), true);
          return InteractionResult.PASS;
        }
        BlockPos targetPos = BlockPos.of(tag.getLong("LinkTarget"));
        lamp.setLinkedPos(targetPos);
        stack.removeTagKey("LinkTarget");
        player.displayClientMessage(Component.translatable("chat.reagenica.link_success",targetPos.toShortString()), true);
        return InteractionResult.SUCCESS;
      }else{
        player.displayClientMessage(Component.translatable("chat.reagenica.point_null"), true);
        return InteractionResult.PASS;
      }
    }
    return InteractionResult.SUCCESS;
  }
}
