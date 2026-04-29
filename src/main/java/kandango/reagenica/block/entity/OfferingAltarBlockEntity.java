package kandango.reagenica.block.entity;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.ChemiSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OfferingAltarBlockEntity extends BlockEntity {
  private int faith_point;

  public OfferingAltarBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.OFFERING_ALTAR.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    this.faith_point = tag.getInt("Faith");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.putInt("Faith", faith_point);
    super.saveAdditional(tag);
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

  public void onInteract(Player player, ItemStack stack, boolean sneaking){
    Level lv = this.level;
    if(lv instanceof ServerLevel slv){
      if(stack.is(ChemiItems.KAGURASUZU.get())){
        slv.playSound(null, worldPosition, ChemiSounds.KAGURA_BLESSING.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
      }
    }
  }
}
