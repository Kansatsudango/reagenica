package kandango.reagenica.item;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import kandango.reagenica.ChemiGameRules;
import kandango.reagenica.ChemiTags;
import kandango.reagenica.worldgen.ChemiBiomes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class ExplorerMapKit extends Item{
  private final Function<? super ServerPlayer, ? extends Optional<BlockPos>> finder;
  private final String name;
  private final MapDecoration.Type marker;

  public ExplorerMapKit(Properties p, String name, MapDecoration.Type marker, Function<? super ServerPlayer, ? extends Optional<BlockPos>> finder) {
    super(p);
    this.finder = finder;
    this.name = name;
    this.marker = marker;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if(level instanceof ServerLevel slv && player instanceof ServerPlayer sp){
      if(!ChemiGameRules.isCompassAllowed(slv) && !player.hasPermissions(2)){
        player.displayClientMessage(Component.translatable("chat.reagenica.location_denied").withStyle(ChatFormatting.YELLOW), true);
        return InteractionResultHolder.success(stack);
      }
      sp.getCooldowns().addCooldown(this, 200);
      ItemStack map = finder.apply(sp).map(pos -> drawMap(slv, pos)).orElse(ItemStack.EMPTY);
      if(!map.isEmpty()){
        player.addItem(map);
        stack.shrink(1);
        return InteractionResultHolder.success(stack);
      }else{
        player.displayClientMessage(Component.translatable("chat.reagenica.map_structure_not_found"), true);
        return InteractionResultHolder.success(stack);
      }
    }
    return InteractionResultHolder.pass(stack);
  }

  private ItemStack drawMap(ServerLevel slv, BlockPos pos){
    ItemStack map = MapItem.create(slv, pos.getX(), pos.getZ(), (byte)2, true, true);
    renderBiomePreviewMapFixed(slv, map);
    MapItemSavedData.addTargetDecoration(map, pos, name, marker);
    map.setHoverName(Component.translatable("item.reagenica."+name+"_map"));
    return map;
  }

  @Override
  public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.reagenica.map_lore").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("tooltip.reagenica.map_lore_2").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("tooltip.reagenica.map_lore_warn").withStyle(ChatFormatting.RED));
  }

  public static final Function<ServerPlayer, Optional<BlockPos>> Crater = sp -> {
    Level lv = sp.level();
    if(lv instanceof ServerLevel slv){
      if(slv.dimension() == ChemiBiomes.PALEO_LEVEL){
        return Optional.ofNullable(slv.findNearestMapStructure(ChemiTags.Structures.CRATER_TARGET, sp.blockPosition(), 100, true));
      }
    }
    return Optional.empty();
  };
  public static final Function<ServerPlayer, Optional<BlockPos>> Volcano = sp -> {
    Level lv = sp.level();
    if(lv instanceof ServerLevel slv){
      if(slv.dimension() == ChemiBiomes.PALEO_LEVEL){
        Registry<Biome> biomeReg = slv.registryAccess().registryOrThrow(Registries.BIOME);
        Holder<Biome> biome = biomeReg.getHolder(ChemiBiomes.VOLCANO_PEAKS).orElseThrow();
        return Optional.ofNullable(slv.findClosestBiome3d(holder -> holder.equals(biome), sp.blockPosition(), 6400, 32, 32)).map(Pair::getFirst);
      }
    }
    return Optional.empty();
  };

  //Based on Minecraft source code
  //net.minecraft.world.item.MapItem#renderBiomePreviewMap, needed 1 line change for my custom dimension
  public static void renderBiomePreviewMapFixed(ServerLevel p_42851_, ItemStack p_42852_) {
    MapItemSavedData mapitemsaveddata = MapItem.getSavedData(p_42852_, p_42851_);
    if (mapitemsaveddata != null) {
      if (p_42851_.dimension() == mapitemsaveddata.dimension) {
        int i = 1 << mapitemsaveddata.scale;
        int j = mapitemsaveddata.centerX;
        int k = mapitemsaveddata.centerZ;
        boolean[] aboolean = new boolean[16384];
        int l = j / i - 64;
        int i1 = k / i - 64;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int j1 = 0; j1 < 128; ++j1) {
          for(int k1 = 0; k1 < 128; ++k1) {
            //Changed line
            Holder<Biome> holder = p_42851_.getBiome(blockpos$mutableblockpos.set((l + k1) * i, 200, (i1 + j1) * i));
            aboolean[j1 * 128 + k1] = holder.is(BiomeTags.WATER_ON_MAP_OUTLINES);
          }
        }

        for(int j2 = 1; j2 < 127; ++j2) {
          for(int k2 = 1; k2 < 127; ++k2) {
            int l2 = 0;

            for(int l1 = -1; l1 < 2; ++l1) {
               for(int i2 = -1; i2 < 2; ++i2) {
                  if ((l1 != 0 || i2 != 0) && isBiomeWatery(aboolean, j2 + l1, k2 + i2)) {
                     ++l2;
                  }
               }
            }

            MapColor.Brightness mapcolor$brightness = MapColor.Brightness.LOWEST;
            MapColor mapcolor = MapColor.NONE;
            if (isBiomeWatery(aboolean, j2, k2)) {
              mapcolor = MapColor.COLOR_ORANGE;
              if (l2 > 7 && k2 % 2 == 0) {
                switch ((j2 + (int)(Mth.sin((float)k2 + 0.0F) * 7.0F)) / 8 % 5) {
                  case 0:
                  case 4:
                    mapcolor$brightness = MapColor.Brightness.LOW;
                    break;
                  case 1:
                  case 3:
                    mapcolor$brightness = MapColor.Brightness.NORMAL;
                    break;
                  case 2:
                    mapcolor$brightness = MapColor.Brightness.HIGH;
                }
              } else if (l2 > 7) {
                mapcolor = MapColor.NONE;
              } else if (l2 > 5) {
                mapcolor$brightness = MapColor.Brightness.NORMAL;
              } else if (l2 > 3) {
                mapcolor$brightness = MapColor.Brightness.LOW;
              } else if (l2 > 1) {
                mapcolor$brightness = MapColor.Brightness.LOW;
              }
            } else if (l2 > 0) {
              mapcolor = MapColor.COLOR_BROWN;
              if (l2 > 3) {
                mapcolor$brightness = MapColor.Brightness.NORMAL;
              } else {
                mapcolor$brightness = MapColor.Brightness.LOWEST;
              }
            }

            if (mapcolor != MapColor.NONE) {
              mapitemsaveddata.setColor(j2, k2, mapcolor.getPackedId(mapcolor$brightness));
            }
          }
        }
      }
    }
  }
  private static boolean isBiomeWatery(boolean[] p_212252_, int p_212253_, int p_212254_) {
     return p_212252_[p_212254_ * 128 + p_212253_];
  }
}
