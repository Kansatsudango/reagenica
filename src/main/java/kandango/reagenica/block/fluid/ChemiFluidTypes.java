package kandango.reagenica.block.fluid;

import kandango.reagenica.ChemistryMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChemiFluidTypes {
  public static final DeferredRegister<FluidType> FLUID_TYPES =
        DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ChemistryMod.MODID);
}
