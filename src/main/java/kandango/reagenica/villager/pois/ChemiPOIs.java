package kandango.reagenica.villager.pois;

import java.util.Set;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemistryMod;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChemiPOIs {
  public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ChemistryMod.MODID);
  public static final RegistryObject<PoiType> STALL_POI = POI_TYPES.register("stall_poi",
    () -> new PoiType(Set.copyOf(ChemiBlocks.TRADING_STALL.get().getStateDefinition().getPossibleStates()),1,1)
  );

  public static void register(IEventBus modEventbus){
    POI_TYPES.register(modEventbus);
  }
}
