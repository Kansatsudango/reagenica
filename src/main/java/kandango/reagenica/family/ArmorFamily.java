package kandango.reagenica.family;

import java.util.stream.Stream;

import kandango.reagenica.ChemiItems;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public class ArmorFamily {
  public final RegistryObject<ArmorItem> HELMET;
  public final RegistryObject<ArmorItem> CHESTPLATE;
  public final RegistryObject<ArmorItem> LEGGINGS;
  public final RegistryObject<ArmorItem> BOOTS;
  public final String name;
  public final ArmorMaterial material;
  public ArmorFamily(String name, ArmorMaterial material, Rarity rarity){
    this.name = name;
    this.material = material;
    this.HELMET = ChemiItems.ITEMS.register(name+"_helmet", () -> new ArmorItem(
      material, ArmorItem.Type.HELMET, new Item.Properties().rarity(rarity))
    );
    this.CHESTPLATE = ChemiItems.ITEMS.register(name+"_chestplate", () -> new ArmorItem(
      material, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(rarity))
    );
    this.LEGGINGS = ChemiItems.ITEMS.register(name+"_leggings", () -> new ArmorItem(
      material, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(rarity))
    );
    this.BOOTS = ChemiItems.ITEMS.register(name+"_boots", () -> new ArmorItem(
      material, ArmorItem.Type.BOOTS, new Item.Properties().rarity(rarity))
    );
  }
  public Stream<RegistryObject<ArmorItem>> armorItems(){
    return Stream.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS);
  }
}
