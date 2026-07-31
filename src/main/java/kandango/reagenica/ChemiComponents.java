package kandango.reagenica;

import java.util.UUID;

import com.mojang.serialization.Codec;

import kandango.reagenica.item.bioreagent.BioReagentTypes;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChemiComponents {
  public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ChemistryMod.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> STERILE = 
            DATA_COMPONENTS.register("sterile", () -> 
              DataComponentType.<Boolean>builder()
              .persistent(Codec.BOOL).build()
            );
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> EFFICIENCY = 
            DATA_COMPONENTS.register("efficiency", () -> 
              DataComponentType.<Byte>builder()
              .persistent(Codec.BYTE).build()
            );
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COLOR = 
            DATA_COMPONENTS.register("color", () -> 
              DataComponentType.<Integer>builder()
              .persistent(Codec.INT).build()
            );
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<BioReagentTypes>> BIO_TYPE = 
            DATA_COMPONENTS.register("parent_type", () -> 
              DataComponentType.<BioReagentTypes>builder()
              .persistent(BioReagentTypes.CODEC)
              .networkSynchronized(ByteBufCodecs.fromCodec(BioReagentTypes.CODEC)).build()
            );
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> GROWTH = 
            DATA_COMPONENTS.register("growth", () -> 
              DataComponentType.<Byte>builder()
              .persistent(Codec.BYTE).build()
            );
  public static final DeferredHolder<DataComponentType<?>,DataComponentType<ItemContainerContents>> BAG_CONTENTS =
            DATA_COMPONENTS.register("bag_contents", () ->
              DataComponentType.<ItemContainerContents>builder()
                .persistent(ItemContainerContents.CODEC)
                .networkSynchronized(ItemContainerContents.STREAM_CODEC).build()
            );
  public static final DeferredHolder<DataComponentType<?>,DataComponentType<UUID>> BAG_UUID =
            DATA_COMPONENTS.register("bag_uuid", () -> 
              DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).build()
            );
}
