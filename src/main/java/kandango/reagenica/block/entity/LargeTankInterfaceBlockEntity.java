package kandango.reagenica.block.entity;

import java.lang.ref.WeakReference;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemistryMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class LargeTankInterfaceBlockEntity extends BlockEntity{
  @Nullable private BlockPos masterPos;
  @Nullable private WeakReference<LargeTankCoreBlockEntity> cachedBE;
  private Optional<LargeTankCoreBlockEntity> getMasterifPresentAndCache(){
    BlockPos master = masterPos;
    WeakReference<LargeTankCoreBlockEntity> cached = cachedBE;
    if(master==null){
      return Optional.empty();
    }else{
      if(cached!=null){
        LargeTankCoreBlockEntity be = cached.get();
        if(be!=null){
          return Optional.of(be);
        }
      }
      // Master Presents && (cache==null or cache ref was destroyed)
      Level lv = this.level;
      if(lv!=null){
        if(lv.isLoaded(master)){
          BlockEntity be = lv.getBlockEntity(master);
          if(be instanceof LargeTankCoreBlockEntity tank){
            cachedBE = new WeakReference<LargeTankCoreBlockEntity>(tank);
            return Optional.of(tank);
          }else{
            ChemistryMod.LOGGER.warn("Master Pos "+master.toShortString()+" was not BE looking for.");
            return Optional.empty();
          }
        }else{
          return Optional.empty();
        }
      }else{
        return Optional.empty();
      }
    }
  }
  public LargeTankInterfaceBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.LARGE_TANK_INTERFACE.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    if(tag.getBoolean("HasMaster")){
      this.masterPos = BlockPos.of(tag.getLong("MasterPos"));
    }else{
      this.masterPos = null;
    }
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    BlockPos master = masterPos;
    if(master!=null){
      tag.putLong("MasterPos", master.asLong());
      tag.putBoolean("HasMaster", true);
    }else{
      tag.putBoolean("HasMaster", false);
    }
    super.saveAdditional(tag);
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
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    this.handleUpdateTag(pkt.getTag());
  }

  public void setMaster(BlockPos pos){
    this.masterPos = pos;
    this.cachedBE = null;
    if(level instanceof ServerLevel serverLevel) {
      serverLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
    ChemistryMod.LOGGER.debug("Master set:{}", pos.toShortString());
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    return this.getMasterifPresentAndCache().map(tank -> tank.getCapability(cap, side)).orElse(LazyOptional.empty());
  }

  public Optional<LargeTankCoreBlockEntity> getMasterBlockEntity(){
    return getMasterifPresentAndCache();
  }
}
