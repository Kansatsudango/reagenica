package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.packet.ISingleTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncFluidPacket;
import kandango.reagenica.screen.TankMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;

public abstract class SimpleTankBlockEntity extends BlockEntity implements MenuProvider,ISingleTankBlock,ITickableBlockEntity,ILampController{
  protected final ItemStackHandler itemHandler = new ItemStackHandler(2) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        return slot==0;
      }
    };
  protected final FluidTank fluidTank = new FluidTank(tankSize()){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  private boolean dirty = true;
  private final LampControllerHelper<SimpleTankBlockEntity> lamphelper = new LampControllerHelper<SimpleTankBlockEntity>(this);

  private final LazyOptional<IFluidHandler> fluidTankLazyOptional = LazyOptional.of(() -> fluidTank);

  public SimpleTankBlockEntity(BlockEntityType<? extends SimpleTankBlockEntity> type,BlockPos pos, BlockState state){
    super(type,pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    fluidTank.readFromNBT(tag.getCompound("Tank"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    CompoundTag fluidTag = new CompoundTag();
    fluidTank.writeToNBT(fluidTag);
    tag.put("Tank",fluidTag);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
        return fluidTankLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public FluidTank getFluidTank(){
    return fluidTank;
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new TankMenu(id, inv, this);
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
          new SyncFluidPacket(worldPosition, fluidTank.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    boolean dirtyflag = false;
    if(this.dirty){
      if(!this.itemHandler.getStackInSlot(0).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 0, 1, fluidTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 0, 1, fluidTank);
        dirtyflag |= in || out;
      }
      int max = this.fluidTank.getCapacity();
      int amount = this.fluidTank.getFluidAmount();
      if(amount==0)lamphelper.changeLampState(LampStates.YELLOW);
      else if(amount >= max*0.98f)lamphelper.changeLampState(LampStates.RED);
      else lamphelper.changeLampState(LampStates.GREEN);
    }
    this.dirty = dirtyflag;
    lamphelper.lampSyncer();
  }

  @Override
  public void receivePacket(FluidStack fluid) {
    this.getFluidTank().setFluid(fluid);
  }
  @Override
  public LampStates getLampStates() {
    return lamphelper.getLampStates();
  }
  @Override
  public void receivePacket(LampStates states) {
    lamphelper.receivePacket(states);
  }

  protected abstract int tankSize();
  
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    fluidTankLazyOptional.invalidate();
  }
}
