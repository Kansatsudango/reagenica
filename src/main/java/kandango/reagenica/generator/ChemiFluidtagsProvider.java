package kandango.reagenica.generator;

import java.util.concurrent.CompletableFuture;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.fluid.ChemiFluidInterface;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ChemiFluidtagsProvider extends FluidTagsProvider{

  public ChemiFluidtagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper helper) {
    super(output, lookupProvider, ChemistryMod.MODID, helper);
  }

  protected void addTags(Provider provider) {
    ChemiFluids.FLUID_SET.forEach(fluid -> addSameNameTag(fluid));
    addTag(ChemiFluids.ETHYLENE, ResourceLocation.fromNamespaceAndPath("forge", "ethene"));
  }
  private void addSameNameTag(ChemiFluidInterface fluid){
    TagKey<Fluid> tagKey = FluidTags.create(ResourceLocation.fromNamespaceAndPath("forge", fluid.getName()));
    tag(tagKey).add(fluid.getFluid(), fluid.getFlowingFluid());
  }
  private void addTag(ChemiFluidInterface fluid, ResourceLocation location){
    TagKey<Fluid> tagKey = FluidTags.create(location);
    tag(tagKey).add(fluid.getFluid(), fluid.getFlowingFluid());
  }
  
}
