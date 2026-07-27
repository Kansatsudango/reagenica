package kandango.reagenica.block.fluid;

import kandango.reagenica.ChemistryMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ChemiFluidTypes {
  public static final DeferredRegister<FluidType> FLUID_TYPES =
        DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, ChemistryMod.MODID);
}
