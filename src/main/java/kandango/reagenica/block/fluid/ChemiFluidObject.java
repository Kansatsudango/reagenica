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
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class ChemiFluidObject implements ChemiFluidInterface{
  private DeferredHolder<FluidType, FluidType> FluidType;
  private DeferredHolder<Fluid, FlowingFluid> StillFluid;
  private DeferredHolder<Fluid, FlowingFluid> FlowingFluid;
  private BaseFlowingFluid.Properties Properties;
  private DeferredItem<Item> BucketItem;
  private DeferredBlock<LiquidBlock> LiquidBlock;
  public final String name;

  public ChemiFluidObject(String name, int color){
    this.name=name;
    this.FluidType = ChemiFluidTypes.FLUID_TYPES.register(name,() -> new OrganicFluidType(color));
    this.StillFluid = ChemiFluids.FLUIDS.register(name, () -> new BaseFlowingFluid.Source(this.Properties));
    this.FlowingFluid = ChemiFluids.FLUIDS.register("flowing_"+name, () -> new BaseFlowingFluid.Flowing(this.Properties));
    this.Properties = new BaseFlowingFluid.Properties(this.FluidType, this.StillFluid::get, this.FlowingFluid::get).bucket(()->this.BucketItem.get()).block(()->this.LiquidBlock.get());
    this.BucketItem = ChemiItems.registerandlist(name+"_bucket", () -> new BucketItem(this.StillFluid.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
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
  public String getName(){
    return name;
  }
}
