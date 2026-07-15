package kandango.reagenica;

import kandango.reagenica.entity.SilverArrowEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChemiEntities {
  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ChemistryMod.MODID);
  
  public static final DeferredHolder<EntityType<SilverArrowEntity>> SILVER_ARROW = ENTITIES.register("silver_arrow",
        () -> EntityType.Builder.<SilverArrowEntity>of(SilverArrowEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build("silver_arrow"));
}
