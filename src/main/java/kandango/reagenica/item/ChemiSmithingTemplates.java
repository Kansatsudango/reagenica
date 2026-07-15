package kandango.reagenica.item;

import java.util.List;

import kandango.reagenica.ChemistryMod;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

@SuppressWarnings("unused")
public class ChemiSmithingTemplates{
  //code from net.minecraft.world.item.SmithingTemplateItem
  private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
  private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
  private static final ResourceLocation EMPTY_SLOT_HELMET = ResourceLocation.fromNamespaceAndPath("item/empty_armor_slot_helmet");
  private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.fromNamespaceAndPath("item/empty_armor_slot_chestplate");
  private static final ResourceLocation EMPTY_SLOT_LEGGINGS = ResourceLocation.fromNamespaceAndPath("item/empty_armor_slot_leggings");
  private static final ResourceLocation EMPTY_SLOT_BOOTS = ResourceLocation.fromNamespaceAndPath("item/empty_armor_slot_boots");
  private static final ResourceLocation EMPTY_SLOT_HOE = ResourceLocation.fromNamespaceAndPath("item/empty_slot_hoe");
  private static final ResourceLocation EMPTY_SLOT_AXE = ResourceLocation.fromNamespaceAndPath("item/empty_slot_axe");
  private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.fromNamespaceAndPath("item/empty_slot_sword");
  private static final ResourceLocation EMPTY_SLOT_SHOVEL = ResourceLocation.fromNamespaceAndPath("item/empty_slot_shovel");
  private static final ResourceLocation EMPTY_SLOT_PICKAXE = ResourceLocation.fromNamespaceAndPath("item/empty_slot_pickaxe");
  private static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.fromNamespaceAndPath("item/empty_slot_ingot");
  private static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST = ResourceLocation.fromNamespaceAndPath("item/empty_slot_redstone_dust");
  private static final ResourceLocation EMPTY_SLOT_QUARTZ = ResourceLocation.fromNamespaceAndPath("item/empty_slot_quartz");
  private static final ResourceLocation EMPTY_SLOT_EMERALD = ResourceLocation.fromNamespaceAndPath("item/empty_slot_emerald");
  private static final ResourceLocation EMPTY_SLOT_DIAMOND = ResourceLocation.fromNamespaceAndPath("item/empty_slot_diamond");
  private static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.fromNamespaceAndPath("item/empty_slot_lapis_lazuli");
  private static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD = ResourceLocation.fromNamespaceAndPath("item/empty_slot_amethyst_shard");
  private static final String DESCRIPTION_ID = Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath("smithing_template"));
  
  public static SmithingTemplateItem iridium_upgrade(){
    return new SmithingTemplateItem(
      Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID,"smithing_template.iridium_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT),
      Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID,"smithing_template.iridium_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT),
      Component.translatable(Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID,"iridium_upgrade"))).withStyle(TITLE_FORMAT),
      Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID,"smithing_template.iridium_upgrade.base_slot_description"))),
      Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ChemistryMod.MODID,"smithing_template.iridium_upgrade.additions_slot_description"))),
      List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL),
      List.of(EMPTY_SLOT_INGOT)
    );
  }
}
