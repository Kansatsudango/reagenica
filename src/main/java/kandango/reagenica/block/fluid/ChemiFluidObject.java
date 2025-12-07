package kandango.reagenica.block.fluid;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

public class ChemiFluidObject implements ChemiFluidInterface{
  private RegistryObject<FluidType> FluidType;
  private RegistryObject<FlowingFluid> StillFluid;
  private RegistryObject<FlowingFluid> FlowingFluid;
  private ForgeFlowingFluid.Properties Properties;
  private RegistryObject<Item> BucketItem;
  private RegistryObject<LiquidBlock> LiquidBlock;

  public ChemiFluidObject(String name, int color){
    this.FluidType = ChemiFluidTypes.FLUID_TYPES.register(name,() -> new OrganicFluidType(color));
    this.StillFluid = ChemiFluids.FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(this.Properties));
    this.FlowingFluid = ChemiFluids.FLUIDS.register("flowing_"+name, () -> new ForgeFlowingFluid.Flowing(this.Properties));
    this.Properties = new ForgeFlowingFluid.Properties(this.FluidType, this.StillFluid::get, this.FlowingFluid::get).bucket(()->this.BucketItem.get()).block(()->this.LiquidBlock.get());
    this.BucketItem = ChemiItems.registerandlist(name+"_bucket", () -> new BucketItem(this.StillFluid,new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
    this.LiquidBlock = ChemiBlocks.BLOCKS.register(name+"_block", () -> new OrganicFluidBlock(() -> this.StillFluid.get(),BlockBehaviour.Properties.of().noLootTable().noCollission().strength(100)));
  }

  public FluidType getType(){
    return FluidType.get();
  }
  public Fluid getFluid(){
    return StillFluid.get();
  }
  public Fluid getFlowingFluid(){
    return FlowingFluid.get();
  }
  public LiquidBlock getBlock(){
    return LiquidBlock.get();
  }
}
