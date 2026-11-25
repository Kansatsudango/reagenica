package kandango.reagenica.block.entity.electrical;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.block.entity.util.LitUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.recipes.DissolverRecipe;
import kandango.reagenica.screen.DissolverMenu;
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

public class DissolverBlockEntity extends ElectricConsumerAbstract implements MenuProvider,IDualTankBlock{
  private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot%2 == 0) return true;
        else return false;
      }
    };

  private final FluidTank inputFluid = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private final FluidTank outputFluid = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private boolean dirty=true;//Always dirty when loaded newly
  private Optional<DissolverRecipe> cachedRecipe = Optional.empty();

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).outputslot(1).anyfluidInputslot(2).anyfluidOutputslot(4).build());
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(inputFluid, outputFluid));

  public DissolverBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.DISSOLVER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    inputFluid.readFromNBT(tag.getCompound("InputTank"));
    outputFluid.readFromNBT(tag.getCompound("OutputTank"));
    this.progress = tag.getInt("Progress");
    energyStorage.setfromtag(tag.getCompound("Electric"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    CompoundTag fluidTag = new CompoundTag();
    inputFluid.writeToNBT(fluidTag);
    tag.put("InputTank",fluidTag);
    fluidTag = new CompoundTag();
    outputFluid.writeToNBT(fluidTag);
    tag.put("OutputTank",fluidTag);
    tag.putInt("Progress", progress);
    tag.put("Electric", energyStorage.serializetotag());
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER && side!=null) {
      return fluidHandlerLazyOptional.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  public FluidTank getInputTank(){
    return inputFluid;
  }
  public FluidTank getOutputTank(){
    return outputFluid;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new DissolverMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.dissolver");
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
          new SyncDualFluidTanksPacket(worldPosition, inputFluid.getFluid().copy(), outputFluid.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    boolean dirtyflag=false;
    boolean working=false;
    if(lv==null){
      return;
    }
    if(dirty){
      if(!this.itemHandler.getStackInSlot(2).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 2, 3, inputFluid);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 2, 3, inputFluid);
        dirtyflag = in || out;
      }
      if(!this.itemHandler.getStackInSlot(4).isEmpty()){
        dirtyflag = FluidItemConverter.draintoItem(itemHandler, 4, 5, outputFluid);
      }
      cachedRecipe = DissolverRecipe.getRecipe(this.inputFluid.getFluid(), this.itemHandler.getStackInSlot(0), lv);

    }
    dirty=dirtyflag;
    if(cachedRecipe.isPresent() && this.getEnergy() >= 5){
      DissolverRecipe recipe = cachedRecipe.orElseThrow();
      FluidStack fluidin = recipe.getFluidIn();
      FluidStack fluidout = recipe.getFluidOut();
      ItemStack itemout = recipe.getOutputItem();
      if(FluidStackUtil.canFullyInsertToTank(fluidout, outputFluid) && ItemStackUtil.canAddStack(itemHandler.getStackInSlot(1), itemout)){
        working=true;
        this.progress++;
        this.consumeEnergy(5);
        if(progress>=200){
          this.inputFluid.drain(fluidin.getAmount(), FluidAction.EXECUTE);
          this.itemHandler.extractItem(0,1,false);
          this.outputFluid.fill(fluidout, FluidAction.EXECUTE);
          ItemStackUtil.addStackToSlot(itemHandler, 1, itemout);
          this.progress=0;
        }
      }
    }
    LitUtil.setLit(working, lv, worldPosition);
  }
  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(6000, 200,200);
  }
  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.inputFluid.setFluid(fluid1);
    this.outputFluid.setFluid(fluid2);
  }
}
