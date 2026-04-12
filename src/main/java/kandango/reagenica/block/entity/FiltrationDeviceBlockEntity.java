package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.screen.FiltrationDeviceMenu;
import kandango.reagenica.screen.slots.SlotPriorityRule;
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

public class FiltrationDeviceBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity,IDualTankBlock{
  private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        else if(slot==0) return true;
        else return slot%2==1;
      }
    };
  private final FluidTank inputTank = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  private final FluidTank outputTank = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  public FluidTank getInputTank(){
    return inputTank;
  }
  public FluidTank getOutputTank(){
    return outputTank;
  }
  private boolean dirty=false;
  private int progress=0;
  public int getProgress(){
    return progress;
  }
  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler)
                                                                                          .customInputRule(SlotPriorityRule.single(stack -> stack.is(ChemiItems.CARBON_FILTER.get()), 0))
                                                                                          .specificFluidInputSlot(Fluids.WATER, 1).anyfluidOutputslot(3).build());
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(inputTank, outputTank));

  public FiltrationDeviceBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.FILTRATION_DEVICE.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    inputTank.readFromNBT(tag.getCompound("InputTank"));
    outputTank.readFromNBT(tag.getCompound("OutputTank"));
    this.progress = tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "InputTank", inputTank);
    FluidStackUtil.saveFluid(tag, "OutputTank", outputTank);
    tag.putInt("Progress", progress);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandlerLazyOptional.cast();
    }else if(cap == ForgeCapabilities.FLUID_HANDLER){
      if(side == Direction.UP || side == Direction.DOWN){
        return fluidHandlerLazyOptional.cast();
      }else{
        return LazyOptional.empty();
      }
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }
  private void syncFluidToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new SyncDualFluidTanksPacket(worldPosition, inputTank.getFluid().copy(), outputTank.getFluid().copy())
      );
    }
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new FiltrationDeviceMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.filtration_device");
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
    if(lv==null)return;
    boolean dirtyflag = false;
    if(this.dirty){
      if(!this.itemHandler.getStackInSlot(1).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 1, inputTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 1, inputTank);
        dirtyflag |= in || out;
      }
      if(!this.itemHandler.getStackInSlot(3).isEmpty()){
        dirtyflag |= FluidItemConverter.draintoItem(itemHandler, 3, outputTank);
      }
      this.dirty = dirtyflag;
    }
    FluidStack result = new FluidStack(ChemiFluids.DISTILLED_WATER.getFluid(), 100);
    if(isReady(result)){
      this.progress++;
      if(this.progress>=200){
        this.progress=0;
        inputTank.drain(100, FluidAction.EXECUTE);
        outputTank.fill(result, FluidAction.EXECUTE);
        ItemStackUtil.damageItemInSlot(itemHandler, 0, 1, () -> ItemStack.EMPTY);
      }
    }else{
      if(this.progress>=2)this.progress-=2;
    }
  }
  private boolean isReady(FluidStack stack){
    ItemStack filterItem = itemHandler.getStackInSlot(0);
    FluidStack input = inputTank.getFluid();
    boolean hasFilter = filterItem.is(ChemiItems.CARBON_FILTER.get());
    boolean hasInput = input.getFluid().isSame(Fluids.WATER) && input.getAmount() >= 100;
    boolean canInsert = FluidStackUtil.canFullyInsertToTank(stack, outputTank);
    return hasFilter && hasInput && canInsert;
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidHandlerLazyOptional.invalidate();
  }

  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.inputTank.setFluid(fluid1);
    this.outputTank.setFluid(fluid2);
  }
}
