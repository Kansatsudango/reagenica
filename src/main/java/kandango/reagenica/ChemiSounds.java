package kandango.reagenica;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiSounds {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ChemistryMod.MODID);

  //OtoLogic (https://otologic.jp)
  //Licensed under CC BY 4.0 (https://creativecommons.org/licenses/by/4.0/)
  public static final DeferredHolder<SoundEvent> KAGURA_LIGHT =
    SOUND_EVENTS.register("kagura_suzu_light",
        () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, "kagura_suzu_light")));
  public static final DeferredHolder<SoundEvent> KAGURA_RESONANT =
    SOUND_EVENTS.register("kagura_suzu_resonant",
        () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, "kagura_suzu_resonant")));
  public static final DeferredHolder<SoundEvent> KAGURA_SOLEMN =
    SOUND_EVENTS.register("kagura_suzu_solemn",
        () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, "kagura_suzu_solemn")));
  public static final DeferredHolder<SoundEvent> KAGURA_BLESSING =
    SOUND_EVENTS.register("kagura_suzu_reverberant",
        () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, "kagura_suzu_reverberant")));
  //効果音ラボ
  public static final DeferredHolder<SoundEvent> WIND_CHIME =
    SOUND_EVENTS.register("wind_chime",
        () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID, "wind_chime")));
}
