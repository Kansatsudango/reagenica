package kandango.reagenica.block.fluid;

import kandango.reagenica.ChemistryMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ChemiFluidTypes {
  public static final DeferredRegister<FluidType> FLUID_TYPES =
        DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ChemistryMod.MODID);
}
