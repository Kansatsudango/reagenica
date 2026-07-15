package kandango.reagenica.villager;

import com.google.common.collect.ImmutableSet;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.world.ChemiPOIs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiVillagerProfessions {
  public static final DeferredRegister<VillagerProfession> PROFESSIONS = 
    DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ChemistryMod.MODID);

  public static final DeferredHolder<VillagerProfession> CHEMIST = PROFESSIONS.register("chemist", 
    () -> new VillagerProfession(
      "chemist",
      holder -> holder.is(ChemiPOIs.CHEMIST_POI.getKey()), 
      holder -> holder.is(ChemiPOIs.CHEMIST_POI.getKey()), 
      ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC)
  );
  public static final DeferredHolder<VillagerProfession> GEOLOGIST = PROFESSIONS.register("geologist", 
    () -> new VillagerProfession(
      "chemist",
      holder -> holder.is(ChemiPOIs.GEOLOGIST_POI.getKey()), 
      holder -> holder.is(ChemiPOIs.GEOLOGIST_POI.getKey()), 
      ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_TOOLSMITH)
  );
}
