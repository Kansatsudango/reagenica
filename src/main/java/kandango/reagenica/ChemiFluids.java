package kandango.reagenica;

import kandango.reagenica.block.fluid.ChemiFluidObject;
import kandango.reagenica.block.fluid.ChemiGasObject;
import kandango.reagenica.block.fluid.ChemiOnsenObject;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ChemiFluids {
  public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, ChemistryMod.MODID);

  public static final ChemiFluidObject BIOETHANOL = new ChemiFluidObject("bioethanol", 0x99ffddbb);
  public static final ChemiFluidObject SULFURIC_ACID = new ChemiFluidObject("sulfuric_acid", 0x99ddaa22);
  public static final ChemiFluidObject DILUTE_SULFURIC_ACID = new ChemiFluidObject("dilute_sulfuric_acid", 0x99ddcf7c);
  public static final ChemiFluidObject SODIUM_HYDROXIDE = new ChemiFluidObject("sodium_hydroxide", 0x992196F3);
  public static final ChemiFluidObject ETHANOL = new ChemiFluidObject("ethanol", 0xbbdcdcdc);
  public static final ChemiFluidObject PHOSPHORIC_ACID = new ChemiFluidObject("phosphoric_acid", 0x99ffa07a);
  public static final ChemiFluidObject COPPER_SULFATE = new ChemiFluidObject("copper_sulfate", 0xcc0099ff);
  public static final ChemiFluidObject SOY_SAUCE = new ChemiFluidObject("soy_sauce", 0xdd431606);
  public static final ChemiFluidObject SALT_WATER = new ChemiFluidObject("salt_water", 0xddb0e0e6);
  public static final ChemiFluidObject DISTILLED_WATER = new ChemiFluidObject("distilled_water", 0xcc5599ff);
  public static final ChemiFluidObject HEATED_WATER = new ChemiFluidObject("heated_water", 0xccfff0f5);
  public static final ChemiFluidObject AQUA_REGIA = new ChemiFluidObject("aqua_regia", 0xccfa8072);
  public static final ChemiFluidObject ALUMINA_MELT = new ChemiFluidObject("alumina_melt", 0x33cc9988);
  public static final ChemiFluidObject SODIUM_CHLORIDE_MELT = new ChemiFluidObject("sodium_chloride_melt", 0x339ea66f);
  public static final ChemiFluidObject GOLD_SOLUTION = new ChemiFluidObject("gold_solution", 0xbbffd700);
  public static final ChemiFluidObject SILVER_SOLUTION = new ChemiFluidObject("silver_solution", 0xbb87cefa);
  public static final ChemiFluidObject PLATINUM_SOLUTION = new ChemiFluidObject("platinum_solution", 0xbbdda0dd);
  public static final ChemiFluidObject COOKING_SAKE = new ChemiFluidObject("cooking_sake", 0xFFFFFFE0);
  public static final ChemiFluidObject MIRIN = new ChemiFluidObject("mirin", 0xFFF8E58C);
  public static final ChemiFluidObject RICE_VINEGAR = new ChemiFluidObject("rice_vinegar", 0xFFe0bc67);
  public static final ChemiFluidObject FRUIT_VINEGAR = new ChemiFluidObject("fruit_vinegar", 0xFFc93264);
  public static final ChemiFluidObject WINE = new ChemiFluidObject("wine", 0xFF59006b);
  public static final ChemiFluidObject CRUDE_OIL = new ChemiFluidObject("crude_oil", 0xFF333333);
  public static final ChemiFluidObject DIESEL_FUEL = new ChemiFluidObject("diesel_fuel", 0xCC00ff7f);
  public static final ChemiFluidObject NAPHTHA = new ChemiFluidObject("naphtha", 0xCCFFFF00);
  public static final ChemiFluidObject ETHYLENE = new ChemiFluidObject("ethylene", 0xCCDCDCDC);
  public static final ChemiFluidObject BENZENE = new ChemiFluidObject("benzene", 0xCC808080);
  public static final ChemiGasObject AMMONIA = new ChemiGasObject("ammonia", 0xCC83ccd2);
  public static final ChemiGasObject HYDROGEN = new ChemiGasObject("hydrogen", 0xCCdddddd);
  public static final ChemiGasObject OXYGEN = new ChemiGasObject("oxygen", 0xCCffe4e1);
  public static final ChemiGasObject NITROGEN = new ChemiGasObject("nitrogen", 0xCCf0faff);
  public static final ChemiOnsenObject SIMPLE_HOTSPRING = new ChemiOnsenObject("simple_hotspring", 0xFFABCED8, new MobEffectInstance(MobEffects.REGENERATION, 60, 0), ChemiBlocks.YUNOHANA_WHITE::get);
  public static final ChemiOnsenObject SULFUR_HOTSPRING = new ChemiOnsenObject("sulfur_hotspring", 0xFFDCCB18, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3600, 0), ChemiBlocks.YUNOHANA_YELLOW::get);
  public static final ChemiOnsenObject CHLORIDE_HOTSPRING = new ChemiOnsenObject("chloride_hotspring", 0xFFC89932, new MobEffectInstance(MobEffects.REGENERATION, 60, 0), ChemiBlocks.YUNOHANA_SPOTTED::get);
  public static final ChemiOnsenObject IRON_HOTSPRING = new ChemiOnsenObject("iron_hotspring", 0xFFc37854, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600, 0), ChemiBlocks.YUNOHANA_RED::get);
  public static final ChemiOnsenObject RADIOACTIVE_HOTSPRING = new ChemiOnsenObject("radioactive_hotspring", 0xFF7EBEA5, new MobEffectInstance(MobEffects.REGENERATION, 60, 0), ChemiBlocks.YUNOHANA_DARK_YELLOW::get);
}
