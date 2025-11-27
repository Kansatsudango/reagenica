package kandango.reagenica.block.entity.electrical;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampControllerHelper;
import kandango.reagenica.block.entity.lamp.LampState;
import kandango.reagenica.block.entity.lamp.LampStates;
import kandango.reagenica.item.bioreagent.BioGrowingPlate;
import kandango.reagenica.item.bioreagent.BioReagent;
import kandango.reagenica.screen.IncubatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
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
import net.minecraftforge.items.ItemStackHandler;

public class IncubatorBlockEntity extends ElectricConsumerAbstract implements MenuProvider,ILampController{
  private final ItemStackHandler itemHandler = new ItemStackHandler(26) {
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

  private boolean dirty=true;
  private boolean turbo_mode = false;
  private Random rand = new Random();

  private final LazyOptional<ItemStackHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
  private final LampControllerHelper<IncubatorBlockEntity> lamphelper = new LampControllerHelper<IncubatorBlockEntity>(this);

  public IncubatorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.INCUBATOR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    energyStorage.setfromtag(tag.getCompound("Electric"));
    this.turbo_mode = tag.getBoolean("Turbo");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.put("Inventory", itemHandler.serializeNBT());
    tag.put("Electric", energyStorage.serializetotag());
    tag.putBoolean("Turbo", turbo_mode);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        return itemHandlerLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }

  public ItemStackHandler getItemHandler(){
    return itemHandler;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new IncubatorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.incubator");
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
    if(lv==null){
      return;
    }
    if(dirty){}
    if(lv.getGameTime()%20 == 0){
      this.consumeEnergy(8);
      boolean hassome=false;
      boolean hasgrowing=false;
      for(int i=0;i<26;i++){
        ItemStack stack = this.itemHandler.getStackInSlot(i);
        if(!stack.isEmpty()){
          if(this.getEnergy()>0){
            if(turbo_mode || rand.nextInt(30)==0){
              ItemStack grown = grow(stack.copy());
              this.itemHandler.setStackInSlot(i, grown);
            }
          }else{
            if(rand.nextInt(200)==0){
              if(stack.getItem() instanceof BioReagent plate){
                if(!plate.isSterile(stack)){
                  this.itemHandler.setStackInSlot(i, contaminatedPlate());
                }
              }
            }
          }
          hassome=true;
          if(!hasgrowing && stack.getItem() == ChemiItems.GROWING_PLATE.get()){
            hasgrowing=true;
          }
        }
      }
      if(this.getEnergy()>=9900){
        if(hasgrowing){
          lamphelper.changeLampState(new LampStates(LampState.OFF, LampState.ON, LampState.ON));
        }else{
          lamphelper.changeLampState(LampStates.GREEN);
        }
      }else if(this.getEnergy()>0){
        if(hasgrowing){
          lamphelper.changeLampState(LampStates.WARN);
        }else{
          lamphelper.changeLampState(LampStates.GREEN);
        }
      }else{
        if(hassome && hasgrowing){
          lamphelper.changeLampState(new LampStates(LampState.BLINK, LampState.BLINK, LampState.OFF));
        }else if(hassome){
          lamphelper.changeLampState(LampStates.RED);
        }else{
          lamphelper.changeLampState(LampStates.YELLOW);
        }
      }
    }
    lamphelper.lampSyncer();
  }
  private ItemStack grow(ItemStack stack){
    if(stack.getItem() instanceof BioGrowingPlate plate){
      int growth = plate.getGrowth(stack);
      String parenttype = plate.parentType(stack);
      int speed = plate.getSpeed(stack);
      boolean sterile = plate.isSterile(stack);
      if(growth < 7){
        if(!sterile){
          if(rand.nextInt(10)==0){
            return contaminatedPlate();
          }
        }
        BioGrowingPlate.setGrowth(stack,growth+1);
        return stack;
      }else{
        int dice = rand.nextInt(100);
        if(parenttype.equals("Crude")){
          if(dice < 40){
            return contaminatedPlate();
          }else if(dice < 60){
            return new ItemStack(ChemiItems.YEAST.get());
          }else if(dice < 80){
            return new ItemStack(ChemiItems.ORYZAE.get());
          }else{
            return new ItemStack(ChemiItems.ACETOBACTER.get());
          }
        }else if(parenttype.equals("Yeast")){
          ItemStack yeast = new ItemStack(ChemiItems.YEAST.get());
          BioReagent.setStats(yeast, modify(speed), sterile);
          return yeast;
        }else if(parenttype.equals("Oryzae")){
          ItemStack oryzae = new ItemStack(ChemiItems.ORYZAE.get());
          BioReagent.setStats(oryzae, modify(speed), sterile);
          return oryzae;
        }else if(parenttype.equals("Acetobacter")){
          ItemStack aceto = new ItemStack(ChemiItems.ACETOBACTER.get());
          BioReagent.setStats(aceto, modify(speed), sterile);
          return aceto;
        }else{
          return contaminatedPlate();
        }
      }
    }else{
      return stack;
    }
  }
  private ItemStack contaminatedPlate(){
    ItemStack contaminated = new ItemStack(ChemiItems.CONTAMINATED_PLATE.get());
    BioReagent.setColor(contaminated, (rand.nextInt(0xffffff)|0xff000000));
    return contaminated;
  }
  private int modify(int original){
    return Mth.clamp(original+rand.nextInt(3)-1, 0, 30);
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 200,200);
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
  }
}
