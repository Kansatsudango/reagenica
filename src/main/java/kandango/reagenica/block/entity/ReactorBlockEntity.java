package kandango.reagenica.block.entity;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.fluidhandlers.SimpleIOFluidHandler;
import kandango.reagenica.block.entity.itemhandler.ChemiItemHandler;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.block.entity.util.ItemStackUtil;
import kandango.reagenica.packet.IDualTankBlock;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.recipes.ModRecipes;
import kandango.reagenica.recipes.ReactorRecipe;
import kandango.reagenica.screen.ReactorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

public class ReactorBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity,IDualTankBlock,ILampController{
  private final int SLOTCOUNT = 9;
  private final ChemiItemHandler itemHandler = new ChemiItemHandler(SLOTCOUNT) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        return true;
      }
      
      @Override
      public int getSlotLimit(int slot)
      {
        return 1;
      }
    };

  private final FluidTank fluidTank_main = new FluidTank(30000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.DISTILLED_WATER.getFluid());
    }
  };
  private final FluidTank fluidTank_heat = new FluidTank(4000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      dirty=true;
      syncFluidToClient();
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.HEATED_WATER.getFluid());
    }
  };

  private final LazyOptional<ItemStackHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
  private final LazyOptional<IFluidHandler> waterTankLazyOptional = LazyOptional.of(() -> new SimpleIOFluidHandler(fluidTank_main, fluidTank_heat));

  private boolean dirty=true;//Always dirty when loaded newly
  private boolean isActive=false;
  private boolean isSCRAMed;
  private boolean wasPowered=false;
  public boolean isSCRAMed(){return isSCRAMed;}
  public void setSCRAMed(boolean p){isSCRAMed=p;}
  private ReactorSlotState[] slotstates = {new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState(),new ReactorSlotState()};
  private final LampControllerHelper<ReactorBlockEntity> lamphelper = new LampControllerHelper<ReactorBlockEntity>(this);

  public ReactorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.REACTOR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    fluidTank_main.readFromNBT(tag.getCompound("MainTank"));
    fluidTank_heat.readFromNBT(tag.getCompound("HeatTank"));
    this.isSCRAMed = tag.getBoolean("SCRAM");
    this.isActive = tag.getBoolean("Active");
  }

  @Override
  public void onLoad(){
    if(level!=null){
      wasPowered = level.hasNeighborSignal(worldPosition);
    }
    super.onLoad();
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    CompoundTag fluidTag = new CompoundTag();
    fluidTank_main.writeToNBT(fluidTag);
    tag.put("MainTank",fluidTag);
    fluidTag = new CompoundTag();
    fluidTank_heat.writeToNBT(fluidTag);
    tag.put("HeatTank",fluidTag);
    tag.putBoolean("SCRAM", isSCRAMed);
    tag.putBoolean("Active", isActive);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return waterTankLazyOptional.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }


  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id,@Nonnull Inventory inv,@Nonnull Player player) {
    return new ReactorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.reactor");
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
          new SyncDualFluidTanksPacket(worldPosition, fluidTank_main.getFluid().copy(),fluidTank_heat.getFluid().copy())
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
      if(!this.isSCRAMed){
        boolean oldActive=this.isActive;
        this.isActive=false;
        for(int i=0;i!=9;i++){
          slotstates[i].recalc(itemHandler, i,lv);
          if(slotstates[i].getSlotType()==ReactorSlot.FUEL)this.isActive=true;
        }
        if(oldActive!=this.isActive){
          setChanged();
          lv.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
      }
    }
    if(this.isActive && !this.isSCRAMed){
      long time = lv.getGameTime();
      if(time%20==0){
        int energy=0;
        for(int i=0;i!=9;i++){
          ReactorSlot type = slotstates[i].getSlotType();
          if(type==ReactorSlot.FUEL){
            decreaseFuel(i);
            energy+=10+5*slotstates[i].getAmplify();
          }else if(type==ReactorSlot.RECIPE){
            ReactorRecipe recipe = slotstates[i].getRecipe();
            if(recipe!=null){
              RandomSource rand = lv.getRandom();
              if(rand.nextInt(1000) < slotstates[i].getAmplify()){
                itemHandler.setStackInSlot(i, recipe.getOutput().copy());
              }
            }
          }
        }
        this.fluidTank_main.drain(energy, FluidAction.EXECUTE);
        this.fluidTank_heat.fill(new FluidStack(ChemiFluids.HEATED_WATER.getFluid(), energy), FluidAction.EXECUTE);
      }
      if(this.fluidTank_main.getFluidAmount() <= this.fluidTank_main.getCapacity()*0.83333f || this.fluidTank_heat.getFluidAmount() == this.fluidTank_heat.getCapacity()){
        scram(lv);
      }
    }
    if(this.isActive)lamphelper.changeLampState(LampStates.GREEN);
    else if(this.isSCRAMed)lamphelper.changeLampState(LampStates.RED);
    else lamphelper.changeLampState(LampStates.YELLOW);
    lamphelper.lampSyncer();
  }
  private void decreaseFuel(int index){
    boolean changed = ItemStackUtil.damageItemInSlot(this.itemHandler, index, 1, () -> new ItemStack(ChemiItems.DEPLETED_FUEL_ROD.get()));
    if(changed){
      this.dirty=true;
    }
  }
  private void scram(@Nonnull Level lv){
    this.isSCRAMed=true;
    this.isActive=false;
    lv.playSound(null,worldPosition,SoundEvents.ANVIL_LAND,SoundSource.BLOCKS,1.0f,1.1f);
  }
  private void scramRecover(){
    this.isSCRAMed=false;
    this.dirty=true;
  }

  public void onSignalChanged(boolean poweredNow) {
    if(poweredNow && !wasPowered){
      scramRecover();
    }
    this.wasPowered = poweredNow;
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  public FluidTank getFluidTankMain(){
    return fluidTank_main;
  }

  public FluidTank getFluidTankHeat(){
    return fluidTank_heat;
  }

  @Override
  public void receivePacket(FluidStack fluid1, FluidStack fluid2) {
    this.fluidTank_main.setFluid(fluid1);
    this.fluidTank_heat.setFluid(fluid2);
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
  public void invalidateCaps(){
    super.invalidateCaps();
    itemHandlerLazyOptional.invalidate();
    waterTankLazyOptional.invalidate();
  }

  private class ReactorSlotState {
    private ReactorSlot slot;
    private int amplify;
    private ReactorRecipe recipe;
    public ReactorSlotState(){
      this.slot = ReactorSlot.EMPTY;
      this.amplify=1;
      this.recipe=null;
    }
    public void recalc(IItemHandler handler,int slotnum,Level lv){
      int score=0;
      this.slot=getSlotState(handler.getStackInSlot(slotnum));
      for(int rel : relatives(slotnum)){
        ItemStack stackrel = handler.getStackInSlot(rel);
        if(getSlotState(stackrel)==ReactorSlot.FUEL){
          score++;
        }
      }
      if(this.slot==ReactorSlot.RECIPE){
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, handler.getStackInSlot(slotnum));
        this.recipe = lv.getRecipeManager().getRecipeFor(ModRecipes.REACTOR_TYPE.get(), container, lv).orElse(null);
      }else{
        this.recipe = null;
      }
      this.amplify=score;
    }
    public ReactorSlot getSlotType(){return slot;}
    public int getAmplify(){return amplify;}
    @Nullable public ReactorRecipe getRecipe(){return recipe;}
    private Set<Integer> relatives(int slot){
      if(slot==0)return new HashSet<>(Set.of(1,3));
      else if(slot==1)return new HashSet<>(Set.of(0,2,4));
      else if(slot==2)return new HashSet<>(Set.of(1,5));
      else if(slot==3)return new HashSet<>(Set.of(0,4,6));
      else if(slot==4)return new HashSet<>(Set.of(1,3,5,7));
      else if(slot==5)return new HashSet<>(Set.of(2,4,8));
      else if(slot==6)return new HashSet<>(Set.of(3,7));
      else if(slot==7)return new HashSet<>(Set.of(4,6,8));
      else if(slot==8)return new HashSet<>(Set.of(5,7));
      else throw new IndexOutOfBoundsException();
    }
    private ReactorSlot getSlotState(ItemStack stack){
      if(stack.isEmpty())return ReactorSlot.EMPTY;
      else if(stack.getItem()==ChemiItems.URANIUM_FUEL_ROD.get())return ReactorSlot.FUEL;
      else return ReactorSlot.RECIPE;
    }
  }
  private enum ReactorSlot{
    EMPTY,
    FUEL,
    RECIPE
  }
}
