package kandango.reagenica;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChemiSounds {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ChemistryMod.MODID);

  //OtoLogic (https://otologic.jp)
  //Licensed under CC BY 4.0 (https://creativecommons.org/licenses/by/4.0/)
  public static final RegistryObject<SoundEvent> KAGURA_LIGHT =
    SOUND_EVENTS.register("kagura_suzu_light",
        () -> SoundEvent.createVariableRangeEvent(
            new ResourceLocation(ChemistryMod.MODID, "kagura_suzu_light")));
  public static final RegistryObject<SoundEvent> KAGURA_RESONANT =
    SOUND_EVENTS.register("kagura_suzu_resonant",
        () -> SoundEvent.createVariableRangeEvent(
            new ResourceLocation(ChemistryMod.MODID, "kagura_suzu_resonant")));
  public static final RegistryObject<SoundEvent> KAGURA_SOLEMN =
    SOUND_EVENTS.register("kagura_suzu_solemn",
        () -> SoundEvent.createVariableRangeEvent(
            new ResourceLocation(ChemistryMod.MODID, "kagura_suzu_solemn")));
  public static final RegistryObject<SoundEvent> KAGURA_BLESSING =
    SOUND_EVENTS.register("kagura_suzu_reverberant",
        () -> SoundEvent.createVariableRangeEvent(
            new ResourceLocation(ChemistryMod.MODID, "kagura_suzu_reverberant")));
}
