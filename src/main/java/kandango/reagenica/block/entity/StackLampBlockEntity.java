package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.block.entity.lamp.ILampController;
import kandango.reagenica.block.entity.lamp.LampState;
import kandango.reagenica.block.entity.lamp.LampStates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class StackLampBlockEntity extends BlockEntity{
  @Nullable
  private BlockPos linked_pos = null;

  public StackLampBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.STACK_LAMP.get(), pos, state);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    return LazyOptional.empty();
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    if (tag.contains("LinkedPos")) {
        linked_pos = BlockPos.of(tag.getLong("LinkedPos"));
    } else {
        linked_pos = null;
    }
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    BlockPos lp = linked_pos;
    if(lp!=null){
      tag.putLong("LinkedPos", lp.asLong());
    }
    super.saveAdditional(tag);
  }

  @Override
  public void setChanged(){
    super.setChanged();
    final Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      lv.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
  
  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    CompoundTag tag = pkt.getTag();
    if(tag!=null){
      this.load(tag);
    }
  }
  
  @Override
  public CompoundTag getUpdateTag() {
    return this.saveWithoutMetadata();
  }
  
  @Override
  public void handleUpdateTag(CompoundTag tag) {
    this.load(tag);
  }

  public LampStates getLampStates(){
    BlockPos pos=linked_pos;
    if(pos==null){
      return new LampStates(LampState.BLINK, LampState.BLINK, LampState.BLINK);
    }else{
      Level lv=this.level;
      if(lv==null)return new LampStates(LampState.BLINK, LampState.BLINK, LampState.BLINK);
      BlockEntity entity = lv.getBlockEntity(pos);
      if(entity==null)return new LampStates(LampState.BLINK, LampState.BLINK, LampState.OFF);
      if(entity instanceof ILampController lamp){
        LampStates states = lamp.getLampStates();
        return states;
      }else{
        return new LampStates(LampState.BLINK, LampState.BLINK, LampState.OFF);
      }
    }
  }

  public void setLinkedPos(BlockPos pos){
    this.linked_pos=pos;
    this.setChanged();
  }
}
