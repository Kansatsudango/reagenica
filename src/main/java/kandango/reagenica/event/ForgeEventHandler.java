package kandango.reagenica.event;

import java.util.stream.StreamSupport;

import kandango.reagenica.ChemiEnchantments;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.enchantment.AntiPoisonEnchantment;
import kandango.reagenica.enchantment.BigMinerEnchantment;
import kandango.reagenica.enchantment.CrystalizedEnchantment;
import kandango.reagenica.enchantment.LastStandEnchantment;
import kandango.reagenica.enchantment.VeinMinerEnchantment;
import kandango.reagenica.event.task.ChainMiningTaskManager;
import kandango.reagenica.family.ChemiToolTiers;
import kandango.reagenica.network.CableNetworkManager;
import kandango.reagenica.worldgen.ChemiBiomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
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

  @SubscribeEvent
  public static void onEffectAdded(MobEffectEvent.Applicable event){
    if(effectApplying.get())return;
    LivingEntity entity = event.getEntity();
    if(entity.level().isClientSide)return;
    int enchLevel = StreamSupport.stream(entity.getArmorSlots().spliterator(),false)
                                 .mapToInt(stack -> stack.getEnchantmentLevel(ChemiEnchantments.ANTI_POISON.get()))
                                 .sum();
    MobEffectInstance effect = event.getEffectInstance();
    if(enchLevel>0 && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL){
      MobEffectInstance reducted = AntiPoisonEnchantment.run(entity, effect, enchLevel);
      effectApplying.set(true);
      entity.addEffect(reducted);
      effectApplying.set(false);
      event.setResult(Result.DENY);// Deny original Effect
    }
  }
  public static final ThreadLocal<Boolean> effectApplying = ThreadLocal.withInitial(() -> false);

  @SubscribeEvent
  public static void onLivingHurt(LivingHurtEvent event){
    if(event.getEntity().level().isClientSide)return;
    if(event.getSource().getEntity() instanceof LivingEntity entity){
      LivingEntity target = event.getEntity();
      ItemStack weapon = entity.getMainHandItem();
      int enchLevel = weapon.getEnchantmentLevel(ChemiEnchantments.LAST_STAND.get());
      if(enchLevel>0){
        float finalDamage = LastStandEnchantment.calc(entity, event.getAmount(), enchLevel);
        event.setAmount(finalDamage);
      }
      if(weapon.getItem() instanceof SwordItem sword && sword.getTier() == ChemiToolTiers.IRIDIUM){
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
      }
    }
  }

  @SubscribeEvent
  public static void onLivingDrops(LivingDropsEvent event) {
    DamageSource source = event.getSource();
    if(source.getEntity() instanceof Player player){
      ItemStack weapon = player.getMainHandItem();
      int enchLevel = weapon.getEnchantmentLevel(ChemiEnchantments.CRYSTALIZED.get());
      if(enchLevel > 0){
        CrystalizedEnchantment.loot(event.getEntity(), enchLevel).ifPresent(item -> event.getDrops().add(item));
      }
    }
  }
}
