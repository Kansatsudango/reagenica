package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.FractionalDistillerBottom;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncQuadFluidTanksPacket;
import kandango.reagenica.recipes.FractionalDistillerRecipe;
import kandango.reagenica.screen.FractionalDistillerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;

public class FractionalDistillerBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity{
  private final int SLOTCOUNT = 10;
  private final ItemStackHandler itemHandler = new ItemStackHandler(SLOTCOUNT) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(slot==0) return true;
        else if(slot==1) return false;
        else if(slot%2==0) return true;
        else return false;
      }
    };

  private final FluidTank fluidTank_in = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private final FluidTank fluidTank_top = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private final FluidTank fluidTank_bottom = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private final FluidTank fluidTank_water = new FluidTank(8000, (FluidStack x)->x.getFluid() == Fluids.WATER){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };

  private int progress;
  public void setProgress(int p){this.progress = p;}
  public int getProgress(){return this.progress;}
  private int fuel = 0;
  public int getFuel(){return fuel;}
  public void setFuel(int p){this.fuel=p;}
  private int fuelmax = 1600;
  public int getFuelMax(){return fuelmax;}
  public void setFuelMax(int p){this.fuelmax=p;}

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).fuelslot(0).outputslot(1).build());
  private final LazyOptional<IFluidHandler> topTankLazyOptional = LazyOptional.of(() -> fluidTank_top);
  private final LazyOptional<IFluidHandler> lowerLazyOptional = LazyOptional.of(() -> new LowerFluidHandler());
  private final LazyOptional<IFluidHandler> waterTankLazyOptional = LazyOptional.of(() -> fluidTank_water);

  private boolean dirty=true;//Always dirty when loaded newly
  @Nullable
  private FractionalDistillerRecipe cachedRecipe;
  
  public FractionalDistillerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.FRACTIONAL_DISTILLER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    fluidTank_in.readFromNBT(tag.getCompound("InputTank"));
    fluidTank_top.readFromNBT(tag.getCompound("TopTank"));
    fluidTank_bottom.readFromNBT(tag.getCompound("BottomTank"));
    fluidTank_water.readFromNBT(tag.getCompound("WaterTank"));
    if (itemHandler.getSlots() != SLOTCOUNT) {
      ListTag substituetag = new ListTag();
      for (int i = 0; i < SLOTCOUNT; i++) {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putInt("Slot", i);
        ItemStack.EMPTY.copy().save(itemTag);
        substituetag.add(itemTag);
      }
      CompoundTag nbt = new CompoundTag();
      nbt.put("Items", substituetag);
      nbt.putInt("Size", SLOTCOUNT);
      itemHandler.deserializeNBT(nbt);
      ChemistryMod.LOGGER.warn("Invalid length tag at {}, using empty substitue.",this.getBlockPos());
    }
    this.fuel=tag.getInt("Fuel");
    this.fuelmax=tag.getInt("MaxFuel");
    this.progress=tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    CompoundTag fluidTag = new CompoundTag();
    fluidTank_in.writeToNBT(fluidTag);
    tag.put("InputTank",fluidTag);
    fluidTag = new CompoundTag();
    fluidTank_top.writeToNBT(fluidTag);
    tag.put("TopTank",fluidTag);
    fluidTag = new CompoundTag();
    fluidTank_bottom.writeToNBT(fluidTag);
    tag.put("BottomTank",fluidTag);
    fluidTag = new CompoundTag();
    fluidTank_water.writeToNBT(fluidTag);
    tag.put("WaterTank",fluidTag);
    super.saveAdditional(tag);
    tag.putInt("Fuel", fuel);
    tag.putInt("MaxFuel", fuelmax);
    tag.putInt("Progress", progress);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      if (side == Direction.UP) return topTankLazyOptional.cast();
      if(side==Direction.NORTH || side==Direction.SOUTH || side==Direction.EAST || side==Direction.WEST){
        Direction facing = getBlockState().getValue(FractionalDistillerBottom.FACING);
        if(side==facing.getOpposite())return waterTankLazyOptional.cast();
        else return lowerLazyOptional.cast();
      }
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }


  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id,@Nonnull Inventory inv,@Nonnull Player player) {
    return new FractionalDistillerMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.fractional_distiller");
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

  private void syncFluidToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new SyncQuadFluidTanksPacket(worldPosition, fluidTank_in.getFluid().copy(),fluidTank_top.getFluid().copy(),fluidTank_bottom.getFluid().copy(),fluidTank_water.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null){
      return;
    }
    boolean dirtyflag = false;
    if(dirty){
      if(!this.itemHandler.getStackInSlot(2).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 2, 3, fluidTank_in);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 2, 3, fluidTank_in);
        dirtyflag |= in || out;
      }
      if(!this.itemHandler.getStackInSlot(4).isEmpty()){
        dirtyflag |= FluidItemConverter.draintoItem(itemHandler, 4, 5, fluidTank_top);
      }
      if(!this.itemHandler.getStackInSlot(6).isEmpty()){
        dirtyflag |= FluidItemConverter.draintoItem(itemHandler, 6, 7, fluidTank_bottom);
      }
      if(!this.itemHandler.getStackInSlot(8).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 8, 9, fluidTank_water);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 8, 9, fluidTank_water);
        dirtyflag |= in || out;
      }
      cachedRecipe = FractionalDistillerRecipe.getRecipe(fluidTank_in.getFluid(), lv).filter(r -> isInsertable(r)).orElse(null);
      if(cachedRecipe!=null && !this.itemHandler.getStackInSlot(0).isEmpty() && this.fuel==0){
        ItemStack fuelstack = this.itemHandler.getStackInSlot(0);
        int burn = ItemStackUtil.getFuelExceptforLava(fuelstack);
        if(burn>0){
          this.fuel = burn;
          this.fuelmax = burn;
          itemHandler.extractItem(0, 1, false);
        }
      }
    }
    if(this.meetHeatAndCoolCondition() && cachedRecipe != null){
      final FractionalDistillerRecipe recipe = cachedRecipe;
      FluidStack input = recipe.getInput();
      FluidStack top = recipe.getTop();
      FluidStack bottom = recipe.getBottom();
      ItemStack residual = recipe.getResidual();
      ItemStack inslot = this.itemHandler.getStackInSlot(1);
      if(FluidStackUtil.canFullyInsertToTank(top, fluidTank_top) && FluidStackUtil.canFullyInsertToTank(bottom, fluidTank_bottom) && ItemStackUtil.canAddStack(inslot, residual)){
        this.progress++;
        if(this.progress >= 200){
          this.progress = 0;
          fluidTank_in.drain(input, FluidAction.EXECUTE);
          fluidTank_top.fill(top, FluidAction.EXECUTE);
          fluidTank_bottom.fill(bottom, FluidAction.EXECUTE);
          fluidTank_water.drain(25, FluidAction.EXECUTE);
          this.itemHandler.setStackInSlot(1, ItemStackUtil.addStack(inslot, residual));
          dirtyflag |= true;
        }
      }else{
        this.progress = 0;
      }
    }else{
      this.progress = 0;
    }
    this.dirty = dirtyflag;
    if(this.fuel>0)this.fuel--;
    if(this.fuel==0)this.dirty=true;
  }
  private boolean meetHeatAndCoolCondition(){
    return this.fluidTank_water.getFluid().getAmount() >= 25 && this.fuel > 0;
  }
  private boolean isInsertable(FractionalDistillerRecipe recipe){
    boolean top = FluidStackUtil.canFullyInsertToTank(recipe.getTop(), fluidTank_top);
    boolean bottom = FluidStackUtil.canFullyInsertToTank(recipe.getBottom(), fluidTank_bottom);
    boolean item = ItemStackUtil.canAddStack(this.itemHandler.getStackInSlot(1), recipe.getResidual());;
    return top && bottom && item;
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  public FluidTank getFluidTankInput(){
    return fluidTank_in;
  }

  public FluidTank getFluidTankTop(){
    return fluidTank_top;
  }

  public FluidTank getFluidTankBottom(){
    return fluidTank_bottom;
  }

  public FluidTank getFluidTankWater(){
    return fluidTank_water;
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    topTankLazyOptional.invalidate();
    lowerLazyOptional.invalidate();
    waterTankLazyOptional.invalidate();
  }

  private final class LowerFluidHandler implements IFluidHandler{

    @Override
    public int getTanks() {
      return 2;
    }

    @Override
    public @Nonnull FluidStack getFluidInTank(int tank) {
      if(tank==0) return fluidTank_in.getFluid();
      else if(tank==1) return fluidTank_bottom.getFluid();
      else throw new IllegalArgumentException();
    }

    @Override
    public int getTankCapacity(int tank) {
      if(tank==0) return fluidTank_in.getCapacity();
      else if(tank==1) return fluidTank_bottom.getCapacity();
      else throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
      if(tank==0)return true;
      else if(tank==1)return false;
      else throw new IndexOutOfBoundsException();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
      return fluidTank_in.fill(resource, action);
    }

    @Override
    public @Nonnull FluidStack drain(FluidStack resource, FluidAction action) {
      return fluidTank_bottom.drain(resource, action);
    }

    @Override
    public @Nonnull FluidStack drain(int maxDrain, FluidAction action) {
      return fluidTank_bottom.drain(maxDrain, action);
    }
  }
}
