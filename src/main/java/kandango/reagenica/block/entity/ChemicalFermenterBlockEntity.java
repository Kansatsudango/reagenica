package kandango.reagenica.block.entity;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.itemhandler.CommonChemiItemHandler;
import kandango.reagenica.block.entity.util.FluidItemConverter;
import kandango.reagenica.block.entity.util.FluidStackUtil;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.recipes.FermentationRecipe;
import kandango.reagenica.screen.ChemicalFermenterMenu;
import kandango.reagenica.screen.slots.SlotPriorityRule;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class ChemicalFermenterBlockEntity extends BlockEntity implements MenuProvider,IDualTankBlock,ITickableBlockEntity{
  public static final int FERMENT_TIME = 30000;
  private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot<4) return true;
        else if(slot==4) return stack.getItem() instanceof BioReagent;
        else if(slot==5) return false;
        else if(slot==6) return true;
        else if(slot==7) return false;
        else if(slot==8) return true;
        else if(slot==9) return false;
        else return false;
      }
    };
  private final FluidTank outputTank = new FluidTank(4000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private final FluidTank inputTank = new FluidTank(4000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
  };
  private int processprogress=0;
  private boolean dirty=true;//Always dirty when loaded newly
  private FermentationRecipe cachedRecipe = null;

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler)
                                                                    .customInputRule(SlotPriorityRule.single(stack -> stack.getItem() instanceof BioReagent, 4))
                                                                    .outputslot(5).build());
  private final LazyOptional<IFluidHandler> fluidTankLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(inputTank,outputTank));

  public ChemicalFermenterBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.CHEMICAL_FERMENTER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    outputTank.readFromNBT(tag.getCompound("Tank"));
    inputTank.readFromNBT(tag.getCompound("InputTank"));
    this.processprogress = tag.getInt("Progress");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    CompoundTag fluidTag = new CompoundTag();
    outputTank.writeToNBT(fluidTag);
    tag.put("Tank",fluidTag);
    fluidTag = new CompoundTag();
    inputTank.writeToNBT(fluidTag);
    tag.put("InputTank",fluidTag);
    tag.putInt("Progress", processprogress);
    super.saveAdditional(tag);
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

  public FluidTank getFluidTank(){
    return outputTank;
  }
  public FluidTank getInputFluidTank(){
    return inputTank;
  }

  public int getprogress(){
    return processprogress;
  }

  public void setProgress(int v){
    processprogress = v;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new ChemicalFermenterMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.fermenter");
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
          new SyncDualFluidTanksPacket(worldPosition, inputTank.getFluid().copy(), outputTank.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    boolean dirtyflag = false;
    if(lv==null){
      return;
    }
    if(dirty){
      SimpleContainer container = new SimpleContainer(5);
      container.setItem(0, itemHandler.getStackInSlot(0));
      container.setItem(1, itemHandler.getStackInSlot(1));
      container.setItem(2, itemHandler.getStackInSlot(2));
      container.setItem(3, itemHandler.getStackInSlot(3));
      container.setItem(4, itemHandler.getStackInSlot(4));
      cachedRecipe = FermentationRecipe.getRecipe(container, inputTank.getFluid(), lv).filter(r -> canOutput(r)).orElse(null);
      if(!this.itemHandler.getStackInSlot(6).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 6, 7, inputTank);
        boolean in = FluidItemConverter.drainfromItem(itemHandler, 6, 7, inputTank);
        dirtyflag |= (in|out);
      }
      if(!this.itemHandler.getStackInSlot(8).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 8, 9, outputTank);
        dirtyflag |= out;
      }
    }
    dirty = dirtyflag;
    if(cachedRecipe != null){
      FluidStack output = cachedRecipe.getResultFluid();
      ItemStack outitem = cachedRecipe.getOutputItem();
      ItemStack bioreagent = this.itemHandler.getStackInSlot(4);
      int speed=30;
      if(bioreagent.getItem() instanceof BioReagent br){
        speed += br.getSpeed(bioreagent);
      }
      this.processprogress+=speed;
      if(this.processprogress >= FERMENT_TIME){
        inputTank.drain(cachedRecipe.getInputFluid().getAmount(), FluidAction.EXECUTE);
        outputTank.fill(output,FluidAction.EXECUTE);
        ItemStackUtil.addStackToSlot(itemHandler, 5, outitem);
        this.consumeIngredient(cachedRecipe.getInputs(), lv);
        this.consumeBioseed(cachedRecipe);
        this.processprogress=0;
        lv.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        this.dirty=true;
      }
    }else{
      processprogress=0;
    }
  }
  private void consumeIngredient(List<Ingredient> ingredients,@Nonnull Level lv){
    for(Ingredient ingredient : ingredients){
      for(int i=0;i<4;i++){
        ItemStack stack = itemHandler.getStackInSlot(i);
        if(ingredient.test(stack)){
          if(stack.hasCraftingRemainingItem()){
            ItemStack remaining = stack.getCraftingRemainingItem();
            ItemStackUtil.insertOrElseThrow(lv,this.worldPosition,this.itemHandler,remaining,0,4);
          }
          itemHandler.extractItem(i, 1, false);
          break;
        }
      }
    }
  }
  private void consumeBioseed(FermentationRecipe recipe){
    Ingredient bioseed = recipe.getBioseed();
    if(!bioseed.isEmpty()){
      ItemStack consumed = ItemStackUtil.getDamagedItem(this.itemHandler.getStackInSlot(4), 1, () -> ItemStack.EMPTY);
      this.itemHandler.setStackInSlot(4, consumed);
    }
  }
  private boolean canOutput(FermentationRecipe recipe){
    FluidStack output = recipe.getResultFluid();
    ItemStack outitem = recipe.getOutputItem();
    return (FluidStackUtil.canFullyInsertToTank(output, outputTank)) && (ItemStackUtil.canAddStack(this.itemHandler.getStackInSlot(5), outitem));
  }

  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.inputTank.setFluid(fluid1);
    this.outputTank.setFluid(fluid2);
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    fluidTankLazyOptional.invalidate();
  }
}
