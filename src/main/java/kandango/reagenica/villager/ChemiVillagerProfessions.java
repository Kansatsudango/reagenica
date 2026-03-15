package kandango.reagenica.villager;

import com.google.common.collect.ImmutableSet;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.world.ChemiPOIs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChemiVillagerProfessions {
  public static final DeferredRegister<VillagerProfession> PROFESSIONS = 
    DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ChemistryMod.MODID);

  public static final RegistryObject<VillagerProfession> CHEMIST = PROFESSIONS.register("chemist", 
    () -> new VillagerProfession(
      "chemist",
      holder -> holder.is(ChemiPOIs.CHEMIST_POI.getKey()), 
      holder -> holder.is(ChemiPOIs.CHEMIST_POI.getKey()), 
      ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC)
  );
  public static final RegistryObject<VillagerProfession> GEOLOGIST = PROFESSIONS.register("geologist", 
    () -> new VillagerProfession(
      "chemist",
      holder -> holder.is(ChemiPOIs.GEOLOGIST_POI.getKey()), 
      holder -> holder.is(ChemiPOIs.GEOLOGIST_POI.getKey()), 
      ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_TOOLSMITH)
  );
}
