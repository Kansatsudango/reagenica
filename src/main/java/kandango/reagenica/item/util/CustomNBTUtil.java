package kandango.reagenica.item.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class CustomNBTUtil {
  public static <T> Optional<T> readData(ItemStack stack, Function<CompoundTag, T> reader){
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    if(data==null) return Optional.empty();
    CompoundTag tag = data.copyTag();
    return Optional.ofNullable(reader.apply(tag));
  }

  public static void writeData(ItemStack stack, Consumer<CompoundTag> writer){
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    CompoundTag tag = data!=null ? data.copyTag() : new CompoundTag();
    writer.accept(tag);
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }
}
