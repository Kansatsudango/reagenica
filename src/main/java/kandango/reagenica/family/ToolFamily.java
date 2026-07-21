package kandango.reagenica.family;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import kandango.reagenica.ChemiItems;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;

public class ToolFamily {
  public static final List<ToolFamily> Tools = Collections.synchronizedList(new ArrayList<>());
  public final DeferredItem<SwordItem> SWORD;
  public final DeferredItem<PickaxeItem> PICKAXE;
  public final DeferredItem<AxeItem> AXE;
  public final DeferredItem<ShovelItem> SHOVEL;
  public final DeferredItem<HoeItem> HOE;
  public final String name;
  public final Tier material;
  public ToolFamily(String name, Tier material, Rarity rarity){
    this.name = name;
    this.material = material;
    this.SWORD = ChemiItems.ITEMS.register(name+"_sword", () -> new SwordItem(material,
      new Item.Properties().rarity(rarity).attributes(SwordItem.createAttributes(material, 3.0f, -2.4f))));
    this.PICKAXE = ChemiItems.ITEMS.register(name+"_pickaxe", () -> new PickaxeItem(material,
      new Item.Properties().rarity(rarity).attributes(PickaxeItem.createAttributes(material, 1.0f, -2.8f))));
    this.AXE = ChemiItems.ITEMS.register(name+"_axe", () -> new AxeItem(material,
      new Item.Properties().rarity(rarity).attributes(AxeItem.createAttributes(material, 5.0F, -3.0F))));
    this.SHOVEL = ChemiItems.ITEMS.register(name+"_shovel", () -> new ShovelItem(material,
      new Item.Properties().rarity(rarity).attributes(ShovelItem.createAttributes(material, 1.5F, -3.0F))));
    this.HOE = ChemiItems.ITEMS.register(name+"_hoe", () -> new HoeItem(material,
      new Item.Properties().rarity(rarity).attributes(HoeItem.createAttributes(material, -2, 0.0F))));
    Tools.add(this); 
  }
  public Stream<DeferredItem<? extends TieredItem>> toolItems(){
    return Stream.of(SWORD, PICKAXE, AXE, SHOVEL, HOE);
  }
}
