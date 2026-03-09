package kandango.reagenica.event;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.enchantment.BigMinerEnchantment;
import kandango.reagenica.enchantment.VeinMinerEnchantment;
import kandango.reagenica.event.task.ChainMiningTaskManager;
import kandango.reagenica.network.CableNetworkManager;
import kandango.reagenica.worldgen.ChemiBiomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

  @SubscribeEvent
  public static void onLevelTick(TickEvent.LevelTickEvent event) {
    if (!event.level.isClientSide && event.phase == TickEvent.Phase.END) {
      CableNetworkManager.tick((ServerLevel) event.level);
      veinMining.set(true);
      ChainMiningTaskManager.tick((ServerLevel) event.level);
      veinMining.set(false);
    }
  }

  @SubscribeEvent
  public static void onFuelRegister(FurnaceFuelBurnTimeEvent event) {
    ItemStack stack = event.getItemStack();
    if(stack.is(ChemiItems.ETHANOL_FUEL.get())){
      event.setBurnTime(3200);
    }
  }

  @SubscribeEvent
  public static void onSleepFinished(SleepFinishedTimeEvent event){
    LevelAccessor lv = event.getLevel();
    if(lv instanceof ServerLevel slv){
      if(slv.dimension() == ChemiBiomes.PALEO_LEVEL){
        MinecraftServer server = slv.getServer();
        ServerLevel primaryServerLevel = server.getLevel(Level.OVERWORLD);
        if(primaryServerLevel != null){
          long newTime = primaryServerLevel.getDayTime() + 24000L;
          newTime = newTime - newTime % 24000L;
          primaryServerLevel.setDayTime(newTime);
        }else{
          ChemistryMod.LOGGER.warn("Failed to fetch overworld ServerLevel.");
        }
      }
    }
  }

  @SubscribeEvent
  public static void onBreak(BlockEvent.BreakEvent event){
    if(veinMining.get())return; // Prevent infinite recursion
    Player player = event.getPlayer();
    ItemStack stack = player.getMainHandItem();
    int enchLevel = stack.getEnchantmentLevel(ChemiEnchantments.BIG_MINING.get());
    if(enchLevel>=1){
      LevelAccessor lv = event.getLevel();
      if(lv instanceof ServerLevel slv && player instanceof ServerPlayer sp){
        veinMining.set(true);
        BigMinerEnchantment.run(slv, sp, event.getPos(), stack, enchLevel);
        veinMining.set(false);
      }
    }
    enchLevel = stack.getEnchantmentLevel(ChemiEnchantments.CHAIN_MINING.get());
    if(enchLevel>=1){
      LevelAccessor lv = event.getLevel();
      if(lv instanceof ServerLevel slv && player instanceof ServerPlayer sp){
        VeinMinerEnchantment.run(slv, sp, event.getPos(), stack, enchLevel);
      }
    }
  }
  public static final ThreadLocal<Boolean> veinMining = ThreadLocal.withInitial(() -> false);
}
