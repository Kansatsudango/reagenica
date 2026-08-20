package kandango.reagenica.block.entity.electrical;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.block.entity.ModBlockEntities;
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
import kandango.reagenica.recipes.AutoRecyclerRecipe;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.recipes.items.ItemStackWithChance;
import kandango.reagenica.screen.AutoRecyclerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
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

public class AutoRecyclerBlockEntity extends ElectricConsumerAbstract implements MenuProvider,ILampController,ISingleTankBlock{
  public static final int ACID_UNIT = 25;
  private final Random rand = new Random();
  
  private final ItemStackHandler itemHandler = new ItemStackHandler(19) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        else return true;
      }
    };
  
  private final FluidTank fluidTank = new FluidTank(8000) {
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.SULFURIC_ACID.getFluid());
    }
  };
  public FluidTank getFluidTank(){return fluidTank;}
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private boolean dirty=true;
  @Nullable private AutoRecyclerRecipe cachedRecipe = null;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).outputslot(1,2,3,4,5,6,7,9).specificFluidInputSlot(ChemiFluids.SULFURIC_ACID.getFluid(), 8).build());
  private final LampControllerHelper<AutoRecyclerBlockEntity> lamphelper = new LampControllerHelper<>(this);
  private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> fluidTank);

  public AutoRecyclerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.AUTO_RECYCLER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    this.progress = tag.getInt("Progress");
    fluidTank.readFromNBT(tag.getCompound("Tank"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    tag.putInt("Progress", progress);
    FluidStackUtil.saveFluid(tag, "Tank", fluidTank);
    super.saveAdditional(tag);
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

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new AutoRecyclerMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.auto_recycler");
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
    if(!(this.level instanceof ServerLevel lv))return;
    if(dirty){
      if(!this.itemHandler.getStackInSlot(8).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 8, fluidTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 8, fluidTank);
        dirty = in || out;
      }
      SimpleContainer container = new SimpleContainer(1);
      container.setItem(0, itemHandler.getStackInSlot(0));
      this.cachedRecipe = hasEnoughAcid() ? lv.getRecipeManager().getRecipeFor(ModRecipes.AUTO_RECYCLER_TYPE.get(), container, lv).orElse(null) : null;
    }
    AutoRecyclerRecipe recipe = this.cachedRecipe;
    if(recipe!=null && this.energyStorage.getEnergyStored() >= 10){
      this.energyStorage.extractEnergy(10, false);
      this.progress++;
      if(this.progress>100){
        this.progress=0;
        this.produce(recipe, lv);
      }
    }
  }
  private boolean hasEnoughAcid(){
    return this.fluidTank.getFluidAmount() >= ACID_UNIT;
  }
  private void produce(AutoRecyclerRecipe recipe, Level lv){
    ItemStackUtil.shrinkSlot(itemHandler, 0, 1);
    fluidTank.drain(ACID_UNIT, FluidAction.EXECUTE);
    ItemStack returnitem = recipe.getReturnItem();
    List<ItemStackWithChance> results = recipe.getResults();
    boolean flag = ItemStackUtil.addStackToSlotifPossible(itemHandler, 1, returnitem);
    if(!flag)ItemStackUtil.drop(lv, worldPosition, returnitem);
    for(ItemStackWithChance stack : results){
      this.insert(lv, stack.roll(rand));
    }
  }
  private void insert(Level lv,ItemStack stack){
    for(int i=2;i<=7;i++){
      boolean insert = ItemStackUtil.addStackToSlotifPossible(itemHandler, i, stack);
      if(insert)return;
    }
    ItemStackUtil.drop(lv, worldPosition, stack);
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 200,200);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
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
  public void receivePacket(FluidStack fluid) {
    this.fluidTank.setFluid(fluid);
  }
  
}
