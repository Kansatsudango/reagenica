package kandango.reagenica.villager;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.villager.goals.VisitStallGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChemistryMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerInitEvent {
  @SubscribeEvent
  public static void onBrainInit(EntityJoinLevelEvent event){
    if(event.getEntity() instanceof Villager villager && !villager.level().isClientSide()){
      if (!villager.goalSelector.getAvailableGoals().stream().anyMatch(goal -> goal.getGoal() instanceof VisitStallGoal)) {
        AttributeInstance attribute = villager.getAttribute(Attributes.MOVEMENT_SPEED);
        if(attribute==null){
          ChemistryMod.LOGGER.warn("Oh here? null? really?", new IllegalStateException("villager.getAttribute(Attributes.MOVEMENT_SPEED) was null"));
          return;
        }
        villager.goalSelector.addGoal(3, new VisitStallGoal(villager, attribute.getValue()));
      }
    }
  }

  @SubscribeEvent
  public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
    if(event.getObject() instanceof Villager){
      event.addCapability(new ResourceLocation(ChemistryMod.MODID, "stall_visit_cooldown"), new VisitCooldownCapability());
    }
  }
}
