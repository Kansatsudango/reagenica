package kandango.reagenica.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import kandango.reagenica.ChemiItems;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;

public class ToolFamily {
  public static final List<ToolFamily> Tools = Collections.synchronizedList(new ArrayList<>());
  public final RegistryObject<SwordItem> SWORD;
  public final RegistryObject<PickaxeItem> PICKAXE;
  public final RegistryObject<AxeItem> AXE;
  public final RegistryObject<ShovelItem> SHOVEL;
  public final RegistryObject<HoeItem> HOE;
  public final String name;
  public final Tier material;
  public ToolFamily(String name, Tier material, Rarity rarity){
    this.name = name;
    this.material = material;
    this.SWORD = ChemiItems.ITEMS.register(name+"_sword", () -> new SwordItem(material, 3, -2.4f,
      new Item.Properties().rarity(rarity)));
    this.PICKAXE = ChemiItems.ITEMS.register(name+"_pickaxe", () -> new PickaxeItem(material, 1, -2.8f,
      new Item.Properties().rarity(rarity)));
    this.AXE = ChemiItems.ITEMS.register(name+"_axe", () -> new AxeItem(material, 5.0F, -3.0F,
      new Item.Properties().rarity(rarity)));
    this.SHOVEL = ChemiItems.ITEMS.register(name+"_shovel", () -> new ShovelItem(material, 1.5F, -3.0F,
      new Item.Properties().rarity(rarity)));
    this.HOE = ChemiItems.ITEMS.register(name+"_hoe", () -> new HoeItem(material, -3, 0.0F,
      new Item.Properties().rarity(rarity)));
    Tools.add(this); 
  }
  public Stream<RegistryObject<? extends TieredItem>> toolItems(){
    return Stream.of(SWORD, PICKAXE, AXE, SHOVEL, HOE);
  }
}
