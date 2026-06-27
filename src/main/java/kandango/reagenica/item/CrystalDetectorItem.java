package kandango.reagenica.item;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.world.ChemiCapabilities;
import kandango.reagenica.world.task.DelayedSoundTask;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CrystalDetectorItem extends Item{
  private final ResourceKey<PoiType> poi;

  public CrystalDetectorItem(ResourceKey<PoiType> poi, Properties props) {
    super(props);
    this.poi = poi;
  }
  
  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    Player player = context.getPlayer();
    if(level instanceof ServerLevel slv && player instanceof ServerPlayer sp){
      slv.playSound(null, sp.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.get(), SoundSource.PLAYERS, 1.0f, 1.4f);
      resonance(slv, sp);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    if(level instanceof ServerLevel slv && player instanceof ServerPlayer sp){
      slv.playSound(null, sp.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.get(), SoundSource.PLAYERS, 1.0f, 1.4f);
      resonance(slv, sp);
    }
    return InteractionResultHolder.success(player.getItemInHand(hand));
  }

  private void resonance(ServerLevel slv, ServerPlayer sp){
    final BlockPos pos = sp.blockPosition();
    final Vec3 eyePos = sp.getEyePosition();
    final PoiManager poiManager = slv.getPoiManager();
    Optional<BlockPos> mayCrystalPos = poiManager.findClosest(poi -> poi.is(this.poi), pos, 128, PoiManager.Occupancy.ANY);
    mayCrystalPos.ifPresentOrElse(crystalPos -> {
      int distance = (int)Math.sqrt(crystalPos.distSqr(pos));
      SoundPoint sound = getSoundPoint(eyePos, crystalPos);
      putTask(slv, new DelayedSoundTask(slv, sp, Holder.direct(SoundEvents.AMETHYST_BLOCK_RESONATE),
              sound.delay, sound.d.x, sound.d.y, sound.d.z, 
              Component.translatable("chat.reagenica.crystal_found", distance).withStyle(ChatFormatting.GREEN)));
    },() -> {
      sp.displayClientMessage(Component.translatable("chat.reagenica.no_crystal_found").withStyle(ChatFormatting.YELLOW), true);
    });
  }
  private SoundPoint getSoundPoint(Vec3 start, BlockPos target){
    Vec3 dir = Vec3.atCenterOf(target).subtract(start);
    double distance = dir.length();
    Vec3 projected = dir.normalize().scale(8.0);
    return new SoundPoint(projected, (int)distance/4);
  }
  private static void putTask(ServerLevel slv, DelayedSoundTask task){
    @Nullable final ServerLevel overworld = slv.getServer().getLevel(Level.OVERWORLD);
    if(overworld!=null){
      overworld.getCapability(ChemiCapabilities.DELAYED_SOUND_DATA).ifPresent(data -> data.add(slv, task));
    }
  }
  private record SoundPoint(Vec3 d, int delay) {
  }
}
