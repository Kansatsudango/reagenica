package kandango.reagenica.screen;

import javax.annotation.Nonnull;
import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.DebugEnergyConsumerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.Level;

public class DebugEnergyConsumerMenu extends ChemistryMenu<DebugEnergyConsumerBlockEntity> {
    public DebugEnergyConsumerMenu(int id, Inventory inv, FriendlyByteBuf extradata){
      this(id,inv,(DebugEnergyConsumerBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
    }

    public DebugEnergyConsumerMenu(int id, Inventory inv, DebugEnergyConsumerBlockEntity be){
      super(ModMenus.CONSUMER_DEBUG_MENU.get(),id, inv, be);
      this.addDataSlot(new DataSlot() {
        @Override
        public int get() {return be.getEnergy();}
        @Override
        public void set(int value) {be.setEnergy(value);}
      });
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
      DebugEnergyConsumerBlockEntity be = this.blockEntity;
      if(be!=null){
        Level level = be.getLevel();
        return (level!=null ? level.getBlockState(be.getBlockPos()).getBlock() == be.getBlockState().getBlock() : false)
            && player.distanceToSqr((double) be.getBlockPos().getX() + 0.5D, (double) be.getBlockPos().getY() + 0.5D, (double) be.getBlockPos().getZ() + 0.5D) <= 64.0D;
      }else return false;
    }

    @Override
    public void broadcastChanges(){
      super.broadcastChanges();
    }

    public int getEnergy(){
      return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getEnergy()).orElse(0);
    }
    public int getMaxEnergy(){
      return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getMaxEnergy()).orElse(0);
    }
    
    public DebugEnergyConsumerBlockEntity getBlockEntity(){
      return blockEntity;
    }

    @Override
    protected void internalSlots(DebugEnergyConsumerBlockEntity be) {
      
    }

    @Override
    protected int slotCount() {
      return 0;
    }
}
