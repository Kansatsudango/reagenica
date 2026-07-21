package kandango.reagenica.family;

import java.util.List;
import java.util.Map;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemistryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterial.Layer;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReagenicaArmorMaterials {
  public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, ChemistryMod.MODID);
  public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PINK_GOLD = ARMOR_MATERIALS.register(
                                      "pink_gold", 
                                      () -> new ArmorMaterial(armors(3, 7, 5, 3), 25, 
                                      SoundEvents.ARMOR_EQUIP_GOLD, 
                                      () -> Ingredient.of(ChemiItems.PINK_GOLD_INGOT.get()), 
                                      layer("pink_gold"), 0.75f, 0.0f));
  public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PLATINUM = ARMOR_MATERIALS.register(
                                      "platinum", () -> new ArmorMaterial(armors(4, 8, 6, 4), 12, 
                                      SoundEvents.ARMOR_EQUIP_GOLD, 
                                      () -> Ingredient.of(ChemiItems.PLATINUM_INGOT.get()), 
                                      layer("platinum"), 2.0f, 0.1f));
  public static final DeferredHolder<ArmorMaterial, ArmorMaterial> IRIDIUM = ARMOR_MATERIALS.register(
                                      "iridium", () -> new ArmorMaterial(armors(4, 8, 6, 4), 15, 
                                      SoundEvents.ARMOR_EQUIP_NETHERITE, 
                                      () -> Ingredient.of(ChemiItems.IRIDIUM_INGOT.get()), 
                                      layer("iridium"), 3.0f, 0.15f));

  private static final Map<ArmorItem.Type, Integer> armors(int h, int c, int l, int b){
    return Map.of(ArmorItem.Type.HELMET, h, ArmorItem.Type.CHESTPLATE, c, ArmorItem.Type.LEGGINGS, l, ArmorItem.Type.BOOTS, b);
  }

  private static final List<Layer> layer(String materialName){
    return List.of(
      new ArmorMaterial.Layer(
        ResourceLocation.fromNamespaceAndPath(
            ChemistryMod.MODID,
            materialName
        )
      )
    );
  }
}
