package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.screen.DebugEnergyConsumerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class DebugEnergyConsumerBlockEntity extends ElectricConsumerAbstract implements MenuProvider{
  public DebugEnergyConsumerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.ENERGYCONSUMER_DEBUG.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    energyStorage.setfromtag(tag.getCompound("Electric"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    tag.put("Electric", energyStorage.serializetotag());
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    return super.getCapability(cap, side);
  }

  @Override
  public void serverTick(){
    if(this.getEnergy()>=1000){
      this.energyStorage.extractEnergy(777, false);
    }
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new DebugEnergyConsumerMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.debug_consumer");
  }

  @Override
  public ElectricStorage getElectricStorage(int index) {
    return energyStorage;
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(10000, 20,1000);
  }
}
