package kandango.reagenica.villager;

import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import static kandango.reagenica.ChemiItems.*;
import static kandango.reagenica.ChemiBlocks.*;

public class ChemiVillagerTrades {
  public static void addChemistTrades(Int2ObjectMap<List<ItemListing>> trades){
    List<ItemListing> novice = trades.get(1);
    List<ItemListing> apprentice = trades.get(2);
    List<ItemListing> journeyman = trades.get(3);
    List<ItemListing> expert = trades.get(4);
    List<ItemListing> master = trades.get(5);
    novice.add(buy(TESTTUBE.get(), 8, 1, 16, 2, 0.02f));
    novice.add(buy(DISTILLED_WATER_TUBE.get(), 2, 1, 16, 2, 0.02f));
    novice.add(sell(Items.GLASS, 4, 1, 16, 1, 0.02f));
    apprentice.add(buy(BENZENE.get(), 1, 1, 12, 4, 0.07f));
    apprentice.add(buy(SULFURIC_ACID.get(), 1, 1, 12, 4, 0.07f));
    journeyman.add(buy(MINEWIPE.get(), 1, 1, 12, 12, 0.07f));
    journeyman.add(buy(PLATINUM_INGOT.get(), 1, 24, 12, 40, 0.07f));
    expert.add(sell(SODIUM_HYDROXIDE.get(), 1, 1, 12, 14, 0.07f));
    expert.add(buy(PHOSPHORIC_ACID.get(), 1, 2, 12, 15, 0.07f));
    master.add(sell(PLASMID.get(), 1, 4, 6, 0, 0.1f));
    master.add(sell(CHLORINE.get(), 1, 2, 16, 0, 0.1f));
  }

  public static void addGeologistTrades(Int2ObjectMap<List<ItemListing>> trades){
    List<ItemListing> novice = trades.get(1);
    List<ItemListing> apprentice = trades.get(2);
    List<ItemListing> journeyman = trades.get(3);
    List<ItemListing> expert = trades.get(4);
    List<ItemListing> master = trades.get(5);
    novice.add(buy(Items.GRAVEL, 12, 1, 12, 2, 0.05f));
    novice.add(buy(OIL_SAND.get(), 6, 1, 12, 2, 0.05f));
    novice.add(buy(RAW_CHALCOPYRITE.get(), 5, 1, 12, 2, 0.05f));
    novice.add(sell(Items.COBBLED_DEEPSLATE, 8, 1, 16, 1, 0.01f));
    apprentice.add(buy(YUNOHANA_WHITE_ITEM.get(), 4, 1, 12, 6, 0.05f));
    apprentice.add(buy(PINK_GOLD_INGOT.get(), 1, 1, 12, 12, 0.05f));
    journeyman.add(sell(VOLCANO_MAP_KIT.get(), 1, 8, 1, 10, 0.1f));
    journeyman.add(buy(PLASTIC_DISH.get(), 8, 1, 12, 10, 0.05f));
    expert.add(buy(AQUAMARINE.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(EMERALD.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(CITRINE.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(ROSE_QUARTZ.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(RED_BERYL.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(LAPISQUARTZ.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(MORION.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    expert.add(buy(PERIDOT.SHARD_ITEM.get(), 2, 1, 16, 24, 0.05f));
    master.add(sell(IRIDIUM_UPGRADE_STH.get(), 1, IRIDIUM_INGOT.get(), 1, 48, 16, 0, 0.03f));
    master.add(sell(CRATER_MAP_KIT.get(), 1, 16, 1, 0, 0.1f));
  }

  public static ItemListing buy(Item item, int count, int cost, int maxUses, int xp, float pMul){
    return (trader, rand) -> new MerchantOffer(new ItemStack(item, count), new ItemStack(Items.EMERALD, cost), maxUses, xp, pMul);
  }
  public static ItemListing sell(Item item, int count, int cost, int maxUses, int xp, float pMul){
    return (trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, cost), new ItemStack(item, count), maxUses, xp, pMul);
  }
  public static ItemListing sell(Item item, int count, Item cost2, int ccount2, int cost, int maxUses, int xp, float pMul){
    return (trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, cost),new ItemStack(cost2, ccount2) , new ItemStack(item, count), maxUses, xp, pMul);
  }
}
