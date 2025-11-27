package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.screen.HaberBoschMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class HaberBoschBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity{
  public static final int REACTION_UNIT = 100;
  private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot==0) return true;
        else return slot%2==1;
      }
    };
  private final FluidTank outputTank = new FluidTank(6000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.AMMONIA.getFluid());
    }
  };
  public FluidStack getOutputFluid(){return outputTank.getFluid();}
  public void setOutputAmount(int amount){
    if(amount==0){
      outputTank.setFluid(FluidStack.EMPTY);
    }else{
      outputTank.setFluid(new FluidStack(ChemiFluids.AMMONIA.getFluid(),amount));
    }
  }
  private final FluidTank nitrogenTank = new FluidTank(6000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.NITROGEN.getFluid());
    }
  };
  public FluidStack getNitroFluid(){return nitrogenTank.getFluid();}
  public void setNitroAmount(int amount){
    if(amount==0){
      nitrogenTank.setFluid(FluidStack.EMPTY);
    }else{
      nitrogenTank.setFluid(new FluidStack(ChemiFluids.NITROGEN.getFluid(),amount));
    }
  }
  private final FluidTank hydrogenTank = new FluidTank(6000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.HYDROGEN.getFluid());
    }
  };
  public FluidStack getHydroFluid(){return hydrogenTank.getFluid();}
  public void setHydroAmount(int amount){
    if(amount==0){
      hydrogenTank.setFluid(FluidStack.EMPTY);
    }else{
      hydrogenTank.setFluid(new FluidStack(ChemiFluids.HYDROGEN.getFluid(),amount));
    }
  }
  private int progress=0;
  public int getProgress(){return progress;}
  public void setProgress(int v){progress = v;}
  private int burntime=0;
  public int getBurntime(){return burntime;}
  public void setBurntime(int v){burntime = v;}
  private int maxburntime=0;
  public int getMaxBurnTime(){return maxburntime;}
  public void setMaxBurnTime(int v){maxburntime = v;}
  private boolean dirty=true;//Always dirty when loaded newly

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler)
                                                                    .fuelslot(0).outputslot(2,4,6).build());
  private final LazyOptional<IFluidHandler> fluidTankLazyOptional = LazyOptional.of(() -> new HaberBoschFluidHandler(hydrogenTank,nitrogenTank,outputTank));

  public HaberBoschBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.HABER_BOSCH.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    outputTank.readFromNBT(tag.getCompound("OutputTank"));
    nitrogenTank.readFromNBT(tag.getCompound("NitrogenTank"));
    hydrogenTank.readFromNBT(tag.getCompound("HydrogenTank"));
    this.progress = tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "OutputTank", outputTank);
    FluidStackUtil.saveFluid(tag, "NitrogenTank", nitrogenTank);
    FluidStackUtil.saveFluid(tag, "HydrogenTank", hydrogenTank);
    tag.putInt("Progress", progress);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
        return fluidTankLazyOptional.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new HaberBoschMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.haber_bosch");
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      this.handleUpdateTag(pkt.getTag());
  }
  
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
  }
  
  @Override
  public CompoundTag getUpdateTag() {
      return saveWithoutMetadata();
  }
  
  @Override
  public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    boolean dirtyflag = false;
    if(lv==null){
      return;
    }
    if(dirty){
      if(!this.itemHandler.getStackInSlot(1).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 1, 2, nitrogenTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 1, 2, nitrogenTank);
        dirtyflag |= in || out;
      }
      if(!this.itemHandler.getStackInSlot(3).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 3, 4, hydrogenTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 3, 4, hydrogenTank);
        dirtyflag |= in || out;
      }
      if(!this.itemHandler.getStackInSlot(5).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 5, 6, outputTank);
        dirtyflag |= out;
      }
      if(this.burntime==0 && isReady()){
        ItemStack fuelstack = this.itemHandler.getStackInSlot(0);
        int burn = ForgeHooks.getBurnTime(fuelstack, RecipeType.SMELTING);
        if(burn>0){
          this.burntime = burn;
          this.maxburntime = burn;
          itemHandler.extractItem(0, 1, false);
          dirtyflag=true;
        }
      }
    }
    if(this.burntime>0){
      this.burntime--;
      if(this.burntime==0)dirtyflag=true;
      if(isReady()){
        this.progress++;
        if(this.progress>=200){
          hydrogenTank.drain(REACTION_UNIT*3/2, FluidAction.EXECUTE);
          nitrogenTank.drain(REACTION_UNIT/2, FluidAction.EXECUTE);
          outputTank.fill(new FluidStack(ChemiFluids.AMMONIA.getFluid(), REACTION_UNIT), FluidAction.EXECUTE);
          this.progress=0;
          dirtyflag=true;
        }
      }
    }else{
      if(this.progress!=0){
        this.progress = Math.max(0, this.progress-5);
      }
    }
    dirty = dirtyflag;
  }
  private boolean isReady(){
    return this.hydrogenTank.getFluidAmount() >= (REACTION_UNIT*3/2)
        && this.nitrogenTank.getFluidAmount() >= (REACTION_UNIT/2)
        && this.outputTank.getSpace() >= REACTION_UNIT;
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidTankLazyOptional.invalidate();
  }

  private class HaberBoschFluidHandler implements IFluidHandler {
    final FluidTank hydroTank;
    final FluidTank nitroTank;
    final FluidTank outputTank;
    public HaberBoschFluidHandler(FluidTank t1, FluidTank t2, FluidTank t3){
      this.hydroTank=t1;
      this.nitroTank=t2;
      this.outputTank=t3;
    }
    @Override
    public int getTanks() {
      return 3;
    }

    @Override
    public @Nonnull FluidStack getFluidInTank(int tank) {
      return getTankbyIndex(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
      return getTankbyIndex(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
      return getTankbyIndex(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
      if(resource.getFluid().isSame(ChemiFluids.HYDROGEN.getFluid()))return hydroTank.fill(resource, action);
      else if(resource.getFluid().isSame(ChemiFluids.OXYGEN.getFluid()))return nitroTank.fill(resource, action);
      else return 0;
    }

    @Override
    public @Nonnull FluidStack drain(FluidStack resource, FluidAction action) {
      return outputTank.drain(resource, action);
    }

    @Override
    public @Nonnull FluidStack drain(int maxDrain, FluidAction action) {
      return outputTank.drain(maxDrain, action);
    }
    private FluidTank getTankbyIndex(int index){
      if(index==0)return hydroTank;
      else if(index==1)return nitroTank;
      else if(index==2)return outputTank;
      else throw new IndexOutOfBoundsException("index"+index+"is out of range");
    }
  }
}
