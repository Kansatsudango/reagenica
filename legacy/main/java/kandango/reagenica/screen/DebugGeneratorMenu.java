package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.electrical.DebugGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class DebugGeneratorMenu extends ChemistryMenu<DebugGeneratorBlockEntity> {
    public DebugGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extradata){
      this(id,inv,(DebugGeneratorBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
    }

    public DebugGeneratorMenu(int id, Inventory inv, DebugGeneratorBlockEntity be){
      super(ModMenus.GENERATOR_DEBUG_MENU.get(),id, inv, be);
      this.addDataSlot(new DataSlot() {
        @Override
        public int get() {return be.getEnergy();}
        @Override
        public void set(int value) {be.setEnergy(value);}
      });
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
    
    public DebugGeneratorBlockEntity getBlockEntity(){
      return blockEntity;
    }

    @Override
    protected void internalSlots(DebugGeneratorBlockEntity be) {
      
    }

    @Override
    protected int slotCount() {
      return 0;
    }
}
