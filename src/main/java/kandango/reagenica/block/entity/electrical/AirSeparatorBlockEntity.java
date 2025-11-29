package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.DrainOnlyFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.screen.AirSeparatorMenu;
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

public class AirSeparatorBlockEntity extends ElectricConsumerAbstract implements MenuProvider,IDualTankBlock{
  public static final int PRODUCTION_UNIT = 20;
  private boolean dirty = true;
  private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
      dirty=true;
    }

    @Override
    public boolean isItemValid(int slot, @Nullable ItemStack stack) {
      if(slot==0)return true;
      else return slot%2==1;
    }
  };
  private final FluidTank oxygenTank = new FluidTank(10000) {
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  public FluidStack getOxygen(){
    return oxygenTank.getFluid();
  }
  private final FluidTank nitrogenTank = new FluidTank(10000) {
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
    }
  };
  public FluidStack getNitrogen(){
    return nitrogenTank.getFluid();
  }
  private int progress=0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).anyfluidOutputslot(1).anyfluidOutputslot(3).build());
  private final LazyOptional<IFluidHandler> oxygenHandlerLazyOptional = LazyOptional.of(() -> new DrainOnlyFluidHandler(oxygenTank));
  private final LazyOptional<IFluidHandler> nitrogenHandlerLazyOptional = LazyOptional.of(() -> new DrainOnlyFluidHandler(nitrogenTank));

  public AirSeparatorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.AIR_SEPARATOR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    nitrogenTank.readFromNBT(tag.getCompound("NitrogenTank"));
    oxygenTank.readFromNBT(tag.getCompound("OxygenTank"));
    progress = tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "NitrogenTank", nitrogenTank);
    FluidStackUtil.saveFluid(tag, "OxygenTank", oxygenTank);
    tag.putInt("Progress", progress);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandlerLazyOptional.cast();
    }else if(cap == ForgeCapabilities.FLUID_HANDLER){
      if(side == Direction.UP){
        return oxygenHandlerLazyOptional.cast();
      }else{
        return nitrogenHandlerLazyOptional.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  private void syncFluidToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new SyncDualFluidTanksPacket(worldPosition, nitrogenTank.getFluid().copy(), oxygenTank.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    boolean flag=false;
    if(dirty){
      if(!this.itemHandler.getStackInSlot(1).isEmpty()){
        flag |= FluidItemConverter.draintoItem(itemHandler, 1, 2, oxygenTank);
      }
      if(!this.itemHandler.getStackInSlot(3).isEmpty()){
        flag |= FluidItemConverter.draintoItem(itemHandler, 3, 4, nitrogenTank);
      }
    }
    ItemStack filterItem = itemHandler.getStackInSlot(0);
    final int speed = filterSpeed(filterItem);
    if(speed!=0 && insertable()){
    final int energyConsumption = 10*speed;
      if(this.getEnergy()>energyConsumption){
        this.consumeEnergy(energyConsumption);
        this.progress+=speed;
        if(progress>600){
          this.progress=0;
          ItemStackUtil.damageItemInSlot(itemHandler, 0, 1, () -> ItemStack.EMPTY);
          this.nitrogenTank.fill(new FluidStack(ChemiFluids.NITROGEN.getFluid(), PRODUCTION_UNIT*4), FluidAction.EXECUTE);
          this.oxygenTank.fill(new FluidStack(ChemiFluids.OXYGEN.getFluid(), PRODUCTION_UNIT), FluidAction.EXECUTE);
        }
      }
    }
    dirty=flag;
  }
  private int filterSpeed(ItemStack filter){
    if(filter.is(ChemiItems.COPPER_FILTER.get()))return 1;
    else if(filter.is(ChemiItems.SILVER_FILTER.get()))return 3;
    else if(filter.is(ChemiItems.PLATINUM_FILTER.get()))return 6;
    else return 0;
  }
  private boolean insertable(){
    return nitrogenTank.getSpace() > (PRODUCTION_UNIT*4) && oxygenTank.getSpace() > PRODUCTION_UNIT;
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new AirSeparatorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.air_separator");
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
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 1000,400);
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    oxygenHandlerLazyOptional.invalidate();
    nitrogenHandlerLazyOptional.invalidate();
  }

  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    nitrogenTank.setFluid(fluid1);
    oxygenTank.setFluid(fluid2);
  }
}
