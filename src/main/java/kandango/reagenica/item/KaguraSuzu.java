package kandango.reagenica.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class KaguraSuzu extends Item{
  public KaguraSuzu(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    if(level instanceof ServerLevel slv){
      slv.playSound(null, context.getClickedPos(), ChemiSounds.KAGURA_LIGHT.get(), SoundSource.PLAYERS);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    if (!level.isClientSide) {
      level.playSound(null, player.blockPosition(), ChemiSounds.KAGURA_LIGHT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }
    return InteractionResultHolder.success(player.getItemInHand(hand));
  }
  
  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.kagurasuzu").withStyle(ChatFormatting.GOLD));
  }
}
