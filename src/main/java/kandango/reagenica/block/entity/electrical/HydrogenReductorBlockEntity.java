package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.fluidhandlers.FillOnlyFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.packet.ISingleTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncFluidPacket;
import kandango.reagenica.recipes.HydrogenReductorRecipe;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.screen.HydrogenReductorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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

public class HydrogenReductorBlockEntity extends ElectricConsumerAbstract implements MenuProvider,ISingleTankBlock,ILampController{
  public static final int HydrogenUnit = 50;
  public static final int EnergyUnit = 20;
  
  private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot==0 || slot==3) return true;
        else return false;
      }
    };

  private final FluidTank hydrogenTank = new FluidTank(16000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
      dirty=true;
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.HYDROGEN.getFluid());
    }
  };
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private boolean dirty=true;//Always dirty when loaded newly
  @Nullable private HydrogenReductorRecipe cachedRecipe = null;
  private final LampControllerHelper<HydrogenReductorBlockEntity> lamphelper = new LampControllerHelper<HydrogenReductorBlockEntity>(this);

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).fuelslot(1).outputslot(2,3).anyfluidInputslot(4).anyfluidOutputslot(6).build());
  private final LazyOptional<IFluidHandler> fluidTankLazyOptional = LazyOptional.of(() -> new FillOnlyFluidHandler(hydrogenTank));

  public HydrogenReductorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.HYDROGEN_REDUCTOR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    hydrogenTank.readFromNBT(tag.getCompound("Hydrogen"));
    this.progress = tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "Hydrogen", hydrogenTank);
    tag.putInt("Progress", progress);
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

  public FluidTank getHydrogenTank(){
    return hydrogenTank;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new HydrogenReductorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.hydrogen_reductor");
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
          new SyncFluidPacket(worldPosition, hydrogenTank.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null){
      return;
    }
    if(this.dirty){
      this.dirty=false;
      if(!this.itemHandler.getStackInSlot(3).isEmpty()){
        boolean in = FluidItemConverter.drainfromItem(itemHandler, 3, hydrogenTank);
        this.dirty |= in;
      }
      SimpleContainer container = new SimpleContainer(1);
      container.setItem(0, itemHandler.getStackInSlot(0));
      this.cachedRecipe = lv.getRecipeManager().getRecipeFor(ModRecipes.HYDROGEN_REDUCTOR_TYPE.get(), container, lv).filter(r -> isInsertable(r)).orElse(null);
    }
    boolean isRunning = false;
    if(isMachineReady() && cachedRecipe!=null){
      @Nonnull final HydrogenReductorRecipe recipe = cachedRecipe;
      progress++;
      isRunning=true;
      this.consumeEnergy(EnergyUnit);
      if(progress>=200){
        progress=0;
        ItemStackUtil.shrinkSlot(itemHandler, 0, 1);
        ItemStackUtil.addStackToSlot(itemHandler, 1, recipe.getOutput().copy());
        ItemStackUtil.addStackToSlot(itemHandler, 2, recipe.getByProduct().copy());
        hydrogenTank.drain(HydrogenUnit, FluidAction.EXECUTE);
      }
    }
    if(isRunning)lamphelper.changeLampState(LampStates.GREEN);
    else if(cachedRecipe==null)lamphelper.changeLampState(LampStates.YELLOW);
    else if(hydrogenTank.getFluidAmount() < HydrogenUnit)lamphelper.changeLampState(LampStates.WARN);
    else lamphelper.changeLampState(LampStates.RED);
  }
  private boolean isInsertable(HydrogenReductorRecipe recipe){
    return ItemStackUtil.canAddStack(itemHandler.getStackInSlot(1), recipe.getOutput())
        && ItemStackUtil.canAddStack(itemHandler.getStackInSlot(2), recipe.getByProduct());
  }
  private boolean isMachineReady(){
    return hydrogenTank.getFluidAmount() >= HydrogenUnit
        && energyStorage.getEnergyStored() >= EnergyUnit;
  }
  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 100,100);
  }
  @Override
  public void receivePacket(FluidStack fluid) {
    this.hydrogenTank.setFluid(fluid);
  }
  @Override
  public LampStates getLampStates() {
    return lamphelper.getLampStates();
  }
  @Override
  public void receivePacket(LampStates states) {
    lamphelper.receivePacket(states);
  }
  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidTankLazyOptional.invalidate();
  }
}
