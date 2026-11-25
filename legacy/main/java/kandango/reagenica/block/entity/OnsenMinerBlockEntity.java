package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiGameRules;
import kandango.reagenica.block.entity.fluidhandlers.DrainOnlyFluidHandler;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.fluid.hotspring.OnsenTypes;
import kandango.reagenica.packet.ISingleTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncFluidPacket;
import kandango.reagenica.screen.OnsenMinerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
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

public class OnsenMinerBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity,ISingleTankBlock,ILampController{
  private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        else if(slot==0) return true;
        else return false;
      }
    };
  protected final FluidTank fluidTank = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  @Nullable private OnsenTypes onsenType;
  private boolean dirty=false;
  private int tick=10;
  private int turbo=0;
  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> new DrainOnlyFluidHandler(fluidTank));
  private final LampControllerHelper<OnsenMinerBlockEntity> lamphelper = new LampControllerHelper<OnsenMinerBlockEntity>(this);

  public OnsenMinerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.ONSEN_MINER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    fluidTank.readFromNBT(tag.getCompound("Tank"));
    this.turbo=tag.getInt("Turbo");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "Tank", fluidTank);
    tag.putInt("Turbo", turbo);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandlerLazyOptional.cast();
    }else if(cap == ForgeCapabilities.FLUID_HANDLER){
      return fluidHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }
  public FluidTank getFluidTank(){
    return fluidTank;
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
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new OnsenMinerMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.hotspring_miner");
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
    if(this.dirty){
      dirty=false;
      if(!this.itemHandler.getStackInSlot(0).isEmpty()){
        dirty |= FluidItemConverter.draintoItem(itemHandler, 0, 1, fluidTank);
      }
    }
    if(!ChemiGameRules.isOnsenGenerate(lv))return;
    if(onsenType==null){
      if(lv instanceof ServerLevel slv){
        onsenType = OnsenTypes.getOnsenTypeAt(slv, worldPosition);
      }
    }
    final OnsenTypes onsen = onsenType;
    if(this.turbo>0)this.turbo--;
    if(tick>=10){
      tick=0;
      if(onsen!=null){
        Fluid onsenwater = onsen.getFluid();
        FluidStack onsenWaterStack = new FluidStack(onsenwater, this.turbo==0 ? 25 : 500);
        fluidTank.fill(onsenWaterStack, FluidAction.EXECUTE);
      }
      int max = this.fluidTank.getCapacity();
      int amount = this.fluidTank.getFluidAmount();
      if(amount==0)lamphelper.changeLampState(LampStates.YELLOW);
      else if(amount >= max*0.98f)lamphelper.changeLampState(LampStates.RED);
      else lamphelper.changeLampState(LampStates.GREEN);
    }else{
      tick++;
    }
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidHandlerLazyOptional.invalidate();
  }

  @Override
  public void receivePacket(FluidStack fluid) {
    this.fluidTank.setFluid(fluid);
  }

  @Override
  public LampStates getLampStates() {
    return lamphelper.getLampStates();
  }

  @Override
  public void receivePacket(LampStates states) {
    lamphelper.receivePacket(states);
  }
}
