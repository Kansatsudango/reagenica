package kandango.reagenica.block.entity;

import java.util.List;

import javax.annotation.Nonnull;

import org.joml.Vector3f;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiSounds;
import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.item.Amulet;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public class OfferingAltarBlockEntity extends BlockEntity {
  private int faith_point;
  private static final int MAX_FAITH = 1280;
  private ItemStack amulet = ItemStack.EMPTY;
  public static final ResourceLocation COMMON_LOOTS = new ResourceLocation(ChemistryMod.MODID, "gameplay/altar_common");
  public static final ResourceLocation RARE_LOOTS = new ResourceLocation(ChemistryMod.MODID, "gameplay/altar_rare");
  public static final ResourceLocation EPIC_LOOTS = new ResourceLocation(ChemistryMod.MODID, "gameplay/altar_epic");
  public static final ResourceLocation LEGENDARY_LOOTS = new ResourceLocation(ChemistryMod.MODID, "gameplay/altar_legendary");

  public OfferingAltarBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.OFFERING_ALTAR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    this.faith_point = tag.getInt("Faith");
    this.amulet = ItemStack.of(tag.getCompound("Amulet"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.putInt("Faith", faith_point);
    tag.put("Amulet", amulet.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    this.handleUpdateTag(pkt.getTag());
  }
  
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
  
  @Override
  public CompoundTag getUpdateTag() {
    return saveWithoutMetadata();
  }

  public void onInteract(Player player, ItemStack stack){
    Level lv = this.level;
    if(lv instanceof ServerLevel slv){
      if(stack.is(ChemiItems.KAGURASUZU.get())){
        player.getCooldowns().addCooldown(ChemiItems.KAGURASUZU.get(), 60);
        final int particles = Math.min((faith_point+19)/20, 50);
        spawnParticles(slv, this.faith_point);
        slv.sendParticles(ParticleTypes.ENCHANT, 
          worldPosition.getX() + 0.5,
          worldPosition.getY() + 1.0,
          worldPosition.getZ() + 0.5,
          particles,
          1.5, 1.5, 1.5,0.02);
        slv.playSound(null, worldPosition, getSound(faith_point), SoundSource.PLAYERS, 1.0f, 1.0f);
        if(faith_point>=MAX_FAITH && player instanceof ServerPlayer sp){
          Advancement advancement = sp.server.getAdvancements().getAdvancement(new ResourceLocation("reagenica", "kaguramai"));
          if(advancement!=null){
            sp.getAdvancements().award(advancement, "in_code");
          }else{
            ChemistryMod.LOGGER.warn("Advancement kaguramai not found.");
          }
        }
      }else if(stack.getItem() instanceof Amulet){
        this.amulet = stack.copy();
        this.faith_point += 40;
        slv.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 10.0f, 1.0f);
        stack.setCount(0);
      }else if(stack.is(ChemiTags.Items.OFFERABLES)){
        int count=stack.getCount();
        int unit=getFaithPointPerUnit(stack);
        this.faith_point += unit*count;
        stack.setCount(0);
      }
    }
  }
  private void spawnParticles(ServerLevel slv, int faith){
    if(faith==0)return;
    final int common_weight = commonWeight(faith);
    final int rare_weight = rareWeight(faith);
    final int epic_weight = epicWeight(faith);
    final int legendary_weight = legendaryWeight(faith);
    final int sum = common_weight+rare_weight+epic_weight+legendary_weight;
    DustParticleOptions dust = new DustParticleOptions(new Vector3f(0.8f, 0.8f, 0.8f), 1.0f);
    if(common_weight!=0)slv.sendParticles(dust, 
          worldPosition.getX() + 0.5,
          worldPosition.getY() + 1.0,
          worldPosition.getZ() + 0.5,
          100*common_weight/sum,
          1.5, 1.5, 1.5,0.02);
    dust = new DustParticleOptions(new Vector3f(0.3f, 0.9f, 0.4f), 1.0f);
    if(rare_weight!=0)slv.sendParticles(dust, 
          worldPosition.getX() + 0.5,
          worldPosition.getY() + 1.0,
          worldPosition.getZ() + 0.5,
          100*rare_weight/sum,
          1.5, 1.5, 1.5,0.02);
    dust = new DustParticleOptions(new Vector3f(0.25f, 0.35f, 0.8f), 1.0f);
    if(epic_weight!=0)slv.sendParticles(dust, 
          worldPosition.getX() + 0.5,
          worldPosition.getY() + 1.0,
          worldPosition.getZ() + 0.5,
          100*epic_weight/sum,
          1.5, 1.5, 1.5,0.02);
    dust = new DustParticleOptions(new Vector3f(0.8f, 0.6f, 0.3f), 1.0f);
    if(legendary_weight!=0)slv.sendParticles(dust, 
          worldPosition.getX() + 0.5,
          worldPosition.getY() + 1.0,
          worldPosition.getZ() + 0.5,
          100*legendary_weight/sum,
          1.5, 1.5, 1.5,0.02);
  }
  public void signal(ServerLevel slv){
    ItemStackUtil.drop(slv, worldPosition, roll(slv, this.faith_point));
    this.faith_point=0;
  }
  private ItemStack roll(ServerLevel slv, int faith){
    LootTable loottable = getLootTable(slv, faith);
    LootParams params = new LootParams.Builder(slv)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition))
            .create(LootContextParamSets.CHEST);
    List<ItemStack> results = loottable.getRandomItems(params);
    return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
  }
  private static int commonWeight(int faith){return faith<MAX_FAITH?Math.max(0, 1100-4*faith):0;}
  private static int rareWeight(int faith){return faith<MAX_FAITH?Math.max(0, -(faith-400)*(faith-400)/160 + 1000):0;}
  private static int epicWeight(int faith){return faith<MAX_FAITH?Math.max(0, 3*faith-200):0;}
  private static int legendaryWeight(int faith){return Math.max(0, 4*faith-1000);}
  private LootTable getLootTable(ServerLevel slv, int faith){
    final RandomSource rand = slv.getRandom();
    final LootTable common = slv.getServer().getLootData().getLootTable(COMMON_LOOTS);
    final LootTable rare = slv.getServer().getLootData().getLootTable(RARE_LOOTS);
    final LootTable epic = slv.getServer().getLootData().getLootTable(EPIC_LOOTS);
    final LootTable legendary = slv.getServer().getLootData().getLootTable(LEGENDARY_LOOTS);
    if(faith==0){
      return LootTable.EMPTY;
    }else if(faith<MAX_FAITH){
      final int common_weight = commonWeight(faith);
      final int rare_weight = rareWeight(faith);
      final int epic_weight = epicWeight(faith);
      final int legendary_weight = legendaryWeight(faith);
      final int rng = rand.nextInt(common_weight + rare_weight + epic_weight + legendary_weight);
      if(rng < common_weight){
        return common;
      }else if(rng < common_weight+rare_weight){
        return rare;
      }else if(rng < common_weight+rare_weight+epic_weight){
        return epic;
      }else{
      }
    }else{
    }
    if(!this.amulet.isEmpty()){
      if(this.amulet.getItem() instanceof Amulet amuletItem){
        LootTable table = createEnchantedBookTable(amuletItem.getStoredEnchantment(), amuletItem.getEnchLevelProvider());
        this.amulet = ItemStack.EMPTY;
        return table;
      }else{
        ChemistryMod.LOGGER.warn("Amulet item was not an Amulet: {}, at {}", amulet, worldPosition);
      }
    }
    return legendary;
  }
  private LootTable createEnchantedBookTable(Enchantment ench, NumberProvider np){
    LootItemConditionalFunction.Builder<?> enchFunc = new SetEnchantmentsFunction.Builder().withEnchantment(ench, np);
    LootPool.Builder pool = LootPool.lootPool().add(LootItem.lootTableItem(Items.BOOK).apply(enchFunc).setWeight(1)).setRolls(ConstantValue.exactly(1));
    return LootTable.lootTable().withPool(pool).build();
  }

  private int getFaithPointPerUnit(ItemStack stack){
    if(stack.is(ChemiBlocks.ONION_SEEDS_PURPLE.get()))return 40;
    if(stack.is(ChemiTags.Items.CRYSTAL_SHARDS))return 20;
    else if(stack.is(Tags.Items.CROPS))return 1;
    else return 3;
  }
  private SoundEvent getSound(int faith){
    if(faith<MAX_FAITH/20)return ChemiSounds.KAGURA_LIGHT.get();
    else if(faith<MAX_FAITH/3)return ChemiSounds.KAGURA_RESONANT.get();
    else if(faith<MAX_FAITH)return ChemiSounds.KAGURA_SOLEMN.get();
    else return ChemiSounds.KAGURA_BLESSING.get();
  }
}
