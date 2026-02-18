package kandango.reagenica.worldgen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.worldgen.biome.PaleoBiomeSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChemiBiomes {
  public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCE =
    DeferredRegister.create(Registries.BIOME_SOURCE, ChemistryMod.MODID);
  public static final RegistryObject<Codec<PaleoBiomeSource>> PALEO_BIOME_SOURCE = 
    BIOME_SOURCE.register("paleo_biomes", () -> PaleoBiomeSource.CODEC);
  public static final List<ResourceKey<Biome>> biomeList = new ArrayList<>();

  public static final ResourceKey<Level> PALEO_LEVEL = 
    ResourceKey.create(Registries.DIMENSION, new ResourceLocation(ChemistryMod.MODID, "paleogene"));

  public static final ResourceKey<Biome> WARM_OCEAN = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "primeval_warm_ocean")));
  public static final ResourceKey<Biome> LUKEWARM_OCEAN = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "primeval_lukewarm_ocean")));
    
  public static final ResourceKey<Biome> CANYON_RIVER = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "canyon_river")));
  public static final ResourceKey<Biome> PRIMEVAL_RIVER = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "primeval_river")));
  public static final ResourceKey<Biome> TAXODIUM_RIVER = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "primeval_taxodium_river")));

  public static final ResourceKey<Biome> METASEQUOIA_FOREST = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "metasequoia_forest")));
  public static final ResourceKey<Biome> MEGA_METASEQUOIA_FOREST = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "mega_metasequoia_forest")));
  public static final ResourceKey<Biome> MAGNOLIA_FOREST = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "magnolia_forest")));
  public static final ResourceKey<Biome> PALEO_PLAIN = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "paleo_plain")));
  public static final ResourceKey<Biome> PALEO_DESERT = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "paleo_desert")));
  public static final ResourceKey<Biome> PALEO_OASIS = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "paleo_desert_oasis")));
  public static final ResourceKey<Biome> FERN_GARDEN = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "fern_garden")));
  public static final ResourceKey<Biome> PALEO_JUNGLE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "paleo_jungle")));
  public static final ResourceKey<Biome> PALEO_SWAMP = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "paleo_swamp")));
  public static final ResourceKey<Biome> MESA = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "drought")));
  public static final ResourceKey<Biome> MESA_FOREST = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "drought_forest")));
  public static final ResourceKey<Biome> FUNGI_FIELD = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "chaos_fungi_field")));

  public static final ResourceKey<Biome> ROCKY_PEAKS = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "rocky_peaks")));
  public static final ResourceKey<Biome> SNOWY_PEAKS = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "snowy_peaks")));
  public static final ResourceKey<Biome> VOLCANO_PEAKS = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "volcano_peaks")));
  public static final ResourceKey<Biome> WARM_SLOPES = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "warm_slopes")));
  public static final ResourceKey<Biome> SNOWY_SLOPES = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "snowy_slopes")));
  public static final ResourceKey<Biome> VOLCANO_SLOPES = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "volcano_slopes")));
    
  public static final ResourceKey<Biome> PALEO_MEADOW = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "paleo_meadow")));
  public static final ResourceKey<Biome> LAVA_PLATEAU = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "lava_plateau")));

  public static final ResourceKey<Biome> COMMON_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "common_cave")));
  public static final ResourceKey<Biome> COAL_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "coal_cave")));
  public static final ResourceKey<Biome> DIAMOND_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "diamond_cave")));
  public static final ResourceKey<Biome> EMERALD_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "emerald_cave")));
  public static final ResourceKey<Biome> GOLD_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "gold_cave")));
  public static final ResourceKey<Biome> IRON_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "iron_cave")));
  public static final ResourceKey<Biome> LAPIS_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "lapis_cave")));
  public static final ResourceKey<Biome> LEAD_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "lead_cave")));
  public static final ResourceKey<Biome> REDSTONE_CAVE = addToList(
    ResourceKey.create(Registries.BIOME, new ResourceLocation(ChemistryMod.MODID, "redstone_cave")));

  private static ResourceKey<Biome> addToList(ResourceKey<Biome> registry){
    biomeList.add(registry);
    return registry;
  }
}
