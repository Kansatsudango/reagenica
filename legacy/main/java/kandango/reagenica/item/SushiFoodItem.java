package kandango.reagenica.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SushiFoodItem extends Item {
  public SushiFoodItem(Item.Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity){
    if(entity instanceof Player player){
      ItemStack mainhand = player.getMainHandItem();
      ItemStack offhand = player.getOffhandItem();
      @Nullable ItemStack sushi=null;
      @Nullable ItemStack soysauce=null;
      boolean offhandSushi = false;
      if(mainhand.getItem() instanceof SushiFoodItem){
        sushi = mainhand;
        if(offhand.is(ChemiItems.SOYSAUCE_BOWL.get())){
          soysauce=offhand;
        }
      }else if(offhand.getItem() instanceof SushiFoodItem){
        sushi = offhand;
        if(mainhand.is(ChemiItems.SOYSAUCE_BOWL.get())){
          soysauce = mainhand;
        }
        offhandSushi = true;
      }
      if(sushi!=null && soysauce!=null){
        final boolean offhandSushiAte = offhandSushi;
        soysauce.hurtAndBreak(1, player, p -> {
          if(offhandSushiAte){
            player.broadcastBreakEvent(InteractionHand.MAIN_HAND);
          }else{
            player.broadcastBreakEvent(InteractionHand.OFF_HAND);
          }
          ItemStack bowl = new ItemStack(Items.BOWL);
          if(!player.getInventory().add(bowl)){
            player.drop(bowl, false);
          }
        });
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0));
        if(player instanceof ServerPlayer sp){
          Advancement advancement = sp.server.getAdvancements().getAdvancement(new ResourceLocation("reagenica", "sushi_soysauce"));
          if(advancement!=null){
            sp.getAdvancements().award(advancement, "in_code");
          }else{
            ChemistryMod.LOGGER.warn("Advancement sushi_soysauce not found.");
          }
        }
      }
    }
    super.finishUsingItem(stack, level, entity);
    return stack;
  }
}
