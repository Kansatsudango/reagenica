package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.BlockUtil;
import kandango.reagenica.block.LeadBattery;
import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.block.entity.electrical.Handlers.ConsumerEnergyHandler;
import kandango.reagenica.block.entity.electrical.Handlers.GeneratorEnergyHandler;
import kandango.reagenica.screen.LeadBatteryMenu;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class LeadBatteryBlockEntity extends ElectricGeneratorAbstract implements MenuProvider{
  private final LazyOptional<IEnergyStorage> energyInLazyOptional = LazyOptional.of(() -> new ConsumerEnergyHandler(energyStorage));
  private final LazyOptional<IEnergyStorage> energyOutLazyOptional = LazyOptional.of(() -> new GeneratorEnergyHandler(energyStorage));
  
  public LeadBatteryBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.LEAD_BATTERY.get(),pos,state);
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
    if (cap == ForgeCapabilities.ENERGY) {
      Direction facing = BlockUtil.getStatus(getBlockState(), LeadBattery.FACING).orElse(Direction.NORTH);
      if(side == facing.getOpposite()) return energyOutLazyOptional.cast();
      else return energyInLazyOptional.cast();
    }
    return LazyOptional.empty();
  }
  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    energyInLazyOptional.invalidate();
    energyOutLazyOptional.invalidate();
  }

  @Override
  public void serverTick(){
    this.provideEnergy();
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new LeadBatteryMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.lead_battery");
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(300000, 1000,1000);
  }
  
}
