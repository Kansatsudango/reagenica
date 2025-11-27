package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiTags;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.recipes.ElectroLysisRecipe;
import kandango.reagenica.screen.ElectroLysisMenu;
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

public class ElectroLysisBlockEntity extends ElectricConsumerAbstract implements MenuProvider,IDualTankBlock{
  private final ItemStackHandler itemHandler = new ItemStackHandler(12) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot<=1) return true;
        if(slot==2) return false;
        else return slot%2!=0;
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
  private final FluidTank outputFluid = new FluidTank(4000){
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
  private ElectroLysisRecipe cachedRecipe = null;
  private boolean isUsingEnergy = false;
  public boolean isUsingEnergy(){return this.isUsingEnergy;}
  public void setUsingEnergy(boolean p){this.isUsingEnergy=p;}

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler)
                .customInputRule(SlotPriorityRule.single(item -> item.is(ChemiItems.TESTTUBE.get()), 3))
                .customInputRule(SlotPriorityRule.single(item -> item.is(ChemiItems.TESTTUBE.get()), 5))
                .customInputRule(SlotPriorityRule.single(item -> item.is(ChemiTags.Items.ELECTRODES), 0))
                .customInputRule(SlotPriorityRule.single(item -> item.is(ChemiTags.Items.ELECTRODES), 1))
                .outputslot(2,11).build());
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(inputFluid,outputFluid));

  public ElectroLysisBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.ELECTROLYSIS_DEVICE.get(),pos,state);
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
    return new ElectroLysisMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.electrolysis_device");
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
    if(lv==null){
      return;
    }
    if(dirty){
      if(!this.itemHandler.getStackInSlot(7).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 7, 8, outputFluid);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 7, 8, outputFluid);
        dirtyflag = in || out;
      }
      if(!this.itemHandler.getStackInSlot(9).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 9, 10, inputFluid);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 9, 10, inputFluid);
        dirtyflag = in || out;
      }
      this.cachedRecipe = ElectroLysisRecipe.getRecipe(this.inputFluid.getFluid(), this.itemHandler.getStackInSlot(1), this.itemHandler.getStackInSlot(0), lv)
                                            .filter(r -> canInsert(r))
                                            .orElse(null);
    }
    if(cachedRecipe!=null && this.getEnergy()>=20){
      ItemStack anoderes = cachedRecipe.getOutputP();
      ItemStack cathoderes = cachedRecipe.getOutputN();
      FluidStack outfluid = cachedRecipe.getFluidOut();
      ItemStack anodegas = cachedRecipe.getOutputGasP();
      ItemStack cathodegas = cachedRecipe.getOutputGasN();
      FluidStack influid = cachedRecipe.getFluidIn();
      if(canInsert(cachedRecipe)){
        this.progress++;
        this.consumeEnergy(20);
        if(this.progress >= 400){
          this.progress=0;
          this.inputFluid.drain(influid.getAmount(), FluidAction.EXECUTE);
          ItemStackUtil.addStackToSlot(itemHandler, 2, anoderes);
          ItemStackUtil.addStackToSlot(itemHandler, 11, cathoderes);
          this.outputFluid.fill(outfluid, FluidAction.EXECUTE);
          if(!anodegas.isEmpty() && this.itemHandler.getStackInSlot(3).getItem() == ChemiItems.TESTTUBE.get()){
            boolean res = ItemStackUtil.addStackToSlotifPossible(itemHandler, 4, anodegas);
            if(res) this.itemHandler.getStackInSlot(3).shrink(1);
          }
          if(!cathodegas.isEmpty() && this.itemHandler.getStackInSlot(5).getItem() == ChemiItems.TESTTUBE.get()){
            boolean res = ItemStackUtil.addStackToSlotifPossible(itemHandler, 6, cathodegas);
            if(res) this.itemHandler.getStackInSlot(5).shrink(1);
          }
          if(cachedRecipe.anodeMelts()){
            ItemStackUtil.damageItemInSlot(itemHandler, 0, 10, () -> ItemStack.EMPTY);
          }else{
            ItemStackUtil.damageItemInSlot(itemHandler, 0, 1, () -> ItemStack.EMPTY);
          }
          ItemStackUtil.damageItemInSlot(itemHandler, 1, 1, () -> ItemStack.EMPTY);
          dirtyflag=true;
        }
      }
    }
    this.dirty=dirtyflag;
  }
  private boolean canInsert(ElectroLysisRecipe recipe){
    ItemStack anode = recipe.getOutputP();
    ItemStack cathode = recipe.getOutputN();
    FluidStack fluid = recipe.getFluidOut();
    ItemStack anodeslot = itemHandler.getStackInSlot(2);
    ItemStack cathodeslot = itemHandler.getStackInSlot(11);
    return (anodeslot.isEmpty() || ItemStackUtil.canAddStack(anodeslot, anode))
            && (cathodeslot.isEmpty() || ItemStackUtil.canAddStack(cathodeslot, cathode))
            && FluidStackUtil.canFullyInsertToTank(fluid, outputFluid);
  }
  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 200,200);
  }
  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.inputFluid.setFluid(fluid1);
    this.outputFluid.setFluid(fluid2);
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidHandlerLazyOptional.invalidate();
  }
}
