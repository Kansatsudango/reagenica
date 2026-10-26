package kandango.reagenica.block.entity.electrical;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.block.Frier;
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
import kandango.reagenica.recipes.FrierRecipe;
import kandango.reagenica.screen.FrierMenu;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
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

public class FrierBlockEntity extends ElectricConsumerAbstract implements MenuProvider,ISingleTankBlock,ILampController{
  private final Random rand = new Random();
  public static final float MAX_TEMPERATURE = 180;
  public static final float COOKABLE_THRESHOLD = 170;
  private final ItemStackHandler itemHandler = new ItemStackHandler(13) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        dirty=true;
      }

      @Override 
      public int getSlotLimit(int slot){
        if(1<=slot && slot<=3)return 1;
        else return 64;
      }

      @Override
      public boolean isItemValid(int slot, @Nullable ItemStack stack) {
        if(stack==null) return false;
        if(slot<=3) return true;
        if(slot==4) return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)>0;
        else if(slot<=10) return false;
        else if(slot==11) return true;
        else return false;
      }
    };

  private final FluidTank oilTank = new FluidTank(8000){
    @Override
    protected void onContentsChanged(){
      setChanged();
      syncFluidToClient();
      dirty=true;
    }

    @Override 
    public int fill(FluidStack resource, FluidAction action){
      int prev = this.getFluidAmount();
      int filled = super.fill(resource, action);
      if(action.execute())dropTempByFillingOil(prev, filled);
      return filled;
    }
    
    @Override
    public boolean isFluidValid(FluidStack stack) {
      return stack.getFluid().isSame(ChemiFluids.COOKING_OIL.getFluid());
    }
  };
  private int progress = 0;
  public int getProgress(){return progress;}
  public void setProgress(int p){this.progress=p;}
  private int fuel = 0;
  public int getFuel(){return fuel;}
  public void setFuel(int p){this.fuel=p;}
  private int fuelmax = 1600;
  public int getFuelMax(){return fuelmax;}
  public void setFuelMax(int p){this.fuelmax=p;}
  private float temperature = 20;
  public float getTemperature(){return temperature;}
  public void setTemperature(float p){this.temperature=p;}
  private boolean dirty=true;//Always dirty when loaded newly
  private final FrierRecipes cachedRecipes = new FrierRecipes();
  private boolean isUsingEnergy = false;
  public boolean isUsingEnergy(){return this.isUsingEnergy;}
  public void setUsingEnergy(boolean p){this.isUsingEnergy=p;}
  private final LampControllerHelper<FrierBlockEntity> lamphelper = new LampControllerHelper<FrierBlockEntity>(this);

  private final LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> CommonChemiItemHandler.Builder.of(itemHandler).deniedSlots(1,2,3).fuelslot(4).outputslot(5,6,7,8,9,10).anyfluidInputslot(11).build());
  private final LazyOptional<IFluidHandler> fluidTanksLazyOptional = LazyOptional.of(() -> oilTank);

  public FrierBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.FRIER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    oilTank.readFromNBT(tag.getCompound("OilTank"));
    this.progress = tag.getInt("Progress");
    this.fuel = tag.getInt("Fuel");
    this.temperature = tag.getFloat("Temperature");
    this.fuelmax = tag.getInt("FuelMax");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Inventory", itemHandler.serializeNBT());
    FluidStackUtil.saveFluid(tag, "OilTank", oilTank);
    tag.putInt("Progress", progress);
    tag.putInt("Fuel", fuel);
    tag.putFloat("Temperature", temperature);
    tag.putInt("FuelMax", fuelmax);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return fluidTanksLazyOptional.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  public FluidTank getOilTank(){
    return oilTank;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new FrierMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.frier");
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
          new SyncFluidPacket(worldPosition, oilTank.getFluid().copy())
      );
    }
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null){
      return;
    }
    if(dirty){
      dirty=false;
      for(int i=1;i<=3;i++){
        this.cachedRecipes.setRecipe(FrierRecipe.getRecipe(itemHandler.getStackInSlot(0), lv), i);
      }
      if(!this.itemHandler.getStackInSlot(11).isEmpty()){
        boolean out = FluidItemConverter.draintoItem(itemHandler, 11, oilTank);
        boolean in  = FluidItemConverter.drainfromItem(itemHandler, 11, oilTank);
        dirty |= in || out;
      }
    }
    for(int i=1;i<=3;i++){
      if(this.itemHandler.getStackInSlot(i).isEmpty()){
        ItemStack input = this.itemHandler.getStackInSlot(0);
        if(!input.isEmpty()){
          ItemStack stack = this.itemHandler.extractItem(0, 1, false);
          this.itemHandler.setStackInSlot(i, stack);
        }
      }
    }
    if(this.cachedRecipes.hasAny() || getBlockState().getOptionalValue(Frier.POWERED).orElse(false)){
      if(this.fuel==0 && this.temperature<=170){
        int fuelInSlot = ItemStackUtil.burnAsFuel(itemHandler, 4);
        if(fuelInSlot>0){
          this.fuel=fuelInSlot;
          this.fuelmax=fuelInSlot;
        }
      }
      if(lv.getGameTime()%10==0){
        if(this.fuel>0){
          if(this.temperature<180)heatup(1.5f);
        }else if(this.energyStorage.getEnergyStored()>=70){
          if(this.temperature<180)heatup(1.0f);
          // keeping hot also consumes energy!
          this.energyStorage.extractEnergy(70, false);
        }else{
          if(this.temperature>20)naturalCooldown();
        }
      }
    }
    if(this.fuel>0){
      this.fuel--;
    }
    if(this.cachedRecipes.hasAny() && this.temperature>=170 && this.oilTank.getFluidAmount() >= 4000){
      if(this.cachedRecipes.getResultStacks().anyMatch(stack -> findInsertSlot(stack).isPresent())){
        this.progress++;
      }
      if(this.progress>200){
        this.progress=0;
        for(int i=1;i<=3;i++){
          final int index = i;
          this.cachedRecipes.getRecipe(index).ifPresent(r -> completeRecipe(r, index, lv));
        }
        this.oilTank.drain(25, FluidAction.EXECUTE);
      }
    }
  }
  private void completeRecipe(FrierRecipe recipe, int slotId, Level lv){
    ItemStack result = recipe.getOutput();
    findInsertSlot(result).ifPresent(slot -> {
      ItemStackUtil.addStackToSlotifPossible(itemHandler, slot, result);
      ItemStack byproduct = recipe.getByproduct().roll(rand);
      if(!byproduct.isEmpty()){
        findInsertSlot(byproduct).ifPresentOrElse(
          slot2 -> ItemStackUtil.addStackToSlotifPossible(itemHandler, slot2, byproduct),
          () -> ItemStackUtil.drop(lv, worldPosition, byproduct));
      }
    });
  }
  private OptionalInt findInsertSlot(ItemStack stack){
    int emptyslot = -1;
    for(int i=5;i<11;i++){
      ItemStack current = this.itemHandler.getStackInSlot(i);
      if(ItemStackUtil.canAddStack(stack, current)){
        if(current.isEmpty() && emptyslot == -1){
          emptyslot=i;
        }else{
          return OptionalInt.of(i);
        }
      }
    }
    if(emptyslot != -1)return OptionalInt.of(emptyslot);
    else return OptionalInt.empty();
  }

  private void dropTempByFillingOil(int prev, int amount){
    if(amount<=0)return;
    float newTemp = (this.temperature*prev + 20*amount)/(prev+amount);
    this.temperature = newTemp;
  }
  private void naturalCooldown(){
    int oilAmount = this.oilTank.getFluidAmount();
    float hc = oilAmount*0.05f;
    float cooldown = 100.0f / hc;
    this.temperature -= cooldown;
    if(this.temperature < 20) this.temperature = 20;
  }
  private void heatup(float energy){
    float maxTank = this.oilTank.getCapacity();
    float oilAmount = this.oilTank.getFluidAmount();
    if(oilAmount<=0)return;
    float dt = maxTank*energy/oilAmount;
    this.temperature += dt;
    if(this.temperature > MAX_TEMPERATURE) this.temperature = MAX_TEMPERATURE;
  }
  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(3000, 200,200);
  }
  @Override
  public void receivePacket(FluidStack fluid) {
    this.oilTank.setFluid(fluid);
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
    fluidTanksLazyOptional.invalidate();
  }

  private class FrierRecipes {
    private final FrierRecipe[] recipes = new FrierRecipe[3];
    public Optional<FrierRecipe> getRecipe(int slot){
      return Optional.ofNullable(recipes[toArrayIndex(slot)]);
    }
    public void setRecipe(Optional<FrierRecipe> recipe, int slot){
      recipes[toArrayIndex(slot)] = recipe.orElse(null);
    }
    public Stream<ItemStack> getResultStacks(){
      return Arrays.stream(recipes).filter(Objects::nonNull).map(r -> r.getOutput());
    }
    public boolean hasAny(){
      return Arrays.stream(recipes).anyMatch(Objects::nonNull);
    }

    private int toArrayIndex(int slot){
      if(1<=slot && slot<=3)return slot-1;
      else throw new IndexOutOfBoundsException();
    }
  }
}
