package kandango.reagenica.block.entity.electrical;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.ModBlockEntities;
import kandango.reagenica.screen.SolarPowerGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class SolarPowerGeneratorBlockEntity extends ElectricGeneratorAbstract implements MenuProvider{
  private int powerTick=0;
  private boolean isBlocked = false;

  public SolarPowerGeneratorBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.GENERATOR_SOLAR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    return super.getCapability(cap, side);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    if(lv==null)return;
    if(this.powerTick<10){
      this.powerTick++;
    }else{
      this.powerTick=0;
      this.isBlocked = !lv.canSeeSky(worldPosition.above());
      if(!this.isBlocked)this.generateEnergy(lv);
    }
    this.provideEnergy();
  }

  private void generateEnergy(@Nonnull Level lv){
    int energyRate = 0;
    boolean isClear = !lv.isRaining() && !lv.isThundering();
    int dayTime = (int)(lv.getDayTime() % 24000);
    if(dayTime<12000){//Day
      energyRate = 10;
    }else if(dayTime<13000){//Evening
      energyRate = (13000-dayTime)/100;
    }else if(dayTime<23000){//Night
      energyRate = 0;
    }else{//Dawn
      energyRate = (dayTime-23000)/100;
    }
    if(!isClear)energyRate/=2;
    this.energyStorage.receiveEnergy(energyRate, false);
  }

  public boolean isBlocked(){
    return this.isBlocked;
  }
  public void setBlocked(boolean value){
    this.isBlocked = value;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new SolarPowerGeneratorMenu(id, inv, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.solar_power_generator");
  }

  @Override
  protected ElectricStorage energyStorageProvider() {
    return new ElectricStorage(3000, 1000,400);
  }

  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
  }
}
