package kandango.reagenica.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import kandango.reagenica.ChemiGameRules;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.worldgen.ChemiBiomes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class LocationCompass extends Item{
  private static final String XKey = "targetX";
  private static final String ZKey = "targetZ";

  private final ResourceKey<Biome> Biome;

  public LocationCompass(ResourceKey<Biome> biome){
    super(new Item.Properties());
    this.Biome = biome;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    CompoundTag tag = stack.getOrCreateTag();
    if(tag.contains(XKey) && tag.contains(ZKey))return InteractionResultHolder.pass(stack);
    if(level instanceof ServerLevel slv && player instanceof ServerPlayer sp){
      if(!ChemiGameRules.isCompassAllowed(slv) && !sp.hasPermissions(2)){
        sp.displayClientMessage(Component.translatable("chat.reagenica.location_denied").withStyle(ChatFormatting.YELLOW), true);
        return InteractionResultHolder.success(stack);
      }
      if(!slv.dimension().equals(ChemiBiomes.PALEO_LEVEL)){
        sp.displayClientMessage(Component.translatable("chat.reagenica.compass_dimension_wrong").withStyle(ChatFormatting.YELLOW), true);
        return InteractionResultHolder.success(stack);
      }
      BlockPos origin = player.blockPosition();
      Registry<Biome> biomeReg = slv.registryAccess().registryOrThrow(Registries.BIOME);
      try{
        Holder<Biome> biome = biomeReg.getHolder(Biome).orElseThrow(() -> new NoSuchBiomeException());
        Pair<BlockPos, Holder<Biome>> result = slv.findClosestBiome3d(holder -> holder.equals(biome), origin, 6400, 32, 32);
        if(result!=null){
          BlockPos pos = result.getFirst();
          tag.putInt(XKey, pos.getX());
          tag.putInt(ZKey, pos.getZ());
          sp.displayClientMessage(Component.translatable("chat.reagenica.compass_biome_success").withStyle(ChatFormatting.GREEN), true);
        }else{
          sp.displayClientMessage(Component.translatable("chat.reagenica.compass_biome_not_found").withStyle(ChatFormatting.YELLOW), true);
        }
      }catch(NoSuchBiomeException e){
        ChemistryMod.LOGGER.warn("Reagenica compass : No Such Biome: {}, Player: {}", Biome.toString(), player.getName());
      }
    }
    return InteractionResultHolder.success(stack);
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    CompoundTag tag = stack.getTag();
    if(tag==null || !tag.contains(XKey)){
      tooltip.add(Component.translatable("tooltip.reagenica.compass_lore").withStyle(ChatFormatting.GRAY));
      tooltip.add(Component.translatable("tooltip.reagenica.compass_lore_warn").withStyle(ChatFormatting.RED));
    }
  }

  @Nullable
  public static GlobalPos getPos(ClientLevel lv, ItemStack stack, Entity entity) {
    CompoundTag tag = stack.getTag();
    if(tag!=null && tag.contains(XKey)){
      int tx = tag.getInt(XKey);
      int tz = tag.getInt(ZKey);
      return GlobalPos.of(ChemiBiomes.PALEO_LEVEL, new BlockPos(tx, 0,tz));
    }else{
      return null;
    }
  }

  private static class NoSuchBiomeException extends Exception {
  }
}
