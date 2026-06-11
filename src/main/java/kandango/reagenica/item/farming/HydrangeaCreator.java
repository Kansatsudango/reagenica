package kandango.reagenica.item.farming;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HydrangeaCreator extends Item{
  public HydrangeaCreator(){
    super(new Item.Properties().stacksTo(64));
  }

  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    ItemStack itemStack = context.getItemInHand();
    BlockState clickedBlock = level.getBlockState(pos);
    if(clickedBlock.is(Blocks.AZALEA) || clickedBlock.is(Blocks.FLOWERING_AZALEA)){
      if(level instanceof ServerLevel slv){
        slv.setBlock(pos, ChemiBlocks.HYDRANGEA.get().defaultBlockState(), Block.UPDATE_ALL);
        itemStack.shrink(1);
      }
      return InteractionResult.SUCCESS;
    }else{
      return InteractionResult.PASS;
    }
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.hydrangea_creator").withStyle(ChatFormatting.GREEN));
  }
}
