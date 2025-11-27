package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.LitUtil;
import kandango.reagenica.block.fluid.ChemiFluidBurnMap;
import kandango.reagenica.packet.ISingleTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncFluidPacket;
import kandango.reagenica.screen.FluidFuelGeneratorMenu;
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
import net.minecraft.world.level.block.state.BlockState;
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

public class FluidFuelGeneratorBlockEntity extends ElectricGeneratorAbstract implements MenuProvider,ISingleTankBlock{
  private boolean dirty = true;
  private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
    @Override
    protected void onContentsChanged(int slot) {
      dirty = true;
      setChanged();
    }

    @Override
    public boolean isItemValid(int slot, @Nullable ItemStack stack) {
      return true;
    }
  };
  private final FluidTank fuelTank = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  public FluidTank getFuelFluid(){return fuelTank;}

  private boolean isBurning;
  public boolean isBurning(){return isBurning;}
  public void setBurning(boolean p){this.isBurning=p;}
  private int reservedEnergyTick;
  private int reservedEnergyrate;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).outputslot(1).build());
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> fuelTank);

  public FluidFuelGeneratorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.GENERATOR_FLUID.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    fuelTank.readFromNBT(tag.getCompound("FuelTank"));
    this.isBurning = tag.getBoolean("isBurning");
    this.reservedEnergyTick = tag.getInt("reservedEnergyTick");
    this.reservedEnergyrate = tag.getInt("ReservedEnergyRate");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "FuelTank", fuelTank);
    tag.putBoolean("isBurning", isBurning);
    tag.putInt("reservedEnergyTick", reservedEnergyTick);
    tag.putInt("ReservedEnergyRate", reservedEnergyrate);
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
          new SyncFluidPacket(worldPosition, fuelTank.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    if(dirty){
      if(!this.itemHandler.getStackInSlot(0).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 0, 1, fuelTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 0, 1, fuelTank);
        dirty |= in || out;
      }
    }
    this.isBurning=false;
    if(this.reservedEnergyTick==0 && this.fuelTank.getFluidAmount() >= 10){
      ChemiFluidBurnMap.getBurnrate(this.fuelTank.getFluid().getFluid()).filter(r -> !this.energyStorage.isFull()).ifPresent(rate -> {
        this.reservedEnergyrate = rate.energyPerTick();
        this.reservedEnergyTick = rate.burnTick();
        this.fuelTank.drain(10, FluidAction.EXECUTE);
      });
    }
    if(this.reservedEnergyTick>0){
      reservedEnergyTick--;
      generateEnergy(reservedEnergyrate);
      this.isBurning=true;
    }
    LitUtil.setLit(this.isBurning, lv, worldPosition);
    this.provideEnergy();
  }

  private void generateEnergy(int amount){
    if (energyStorage.receiveEnergy(amount, true) > 0) {
      energyStorage.receiveEnergy(amount, false);
    }
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new FluidFuelGeneratorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.fluid_fuel_generator");
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 1000,600);
  }

  @Override
  public void receivePacket(FluidStack fluid1) {
    this.fuelTank.setFluid(fluid1);
  }
  
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidHandlerLazyOptional.invalidate();
  }
}
