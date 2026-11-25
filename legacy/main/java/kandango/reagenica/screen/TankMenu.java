package kandango.reagenica.screen;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.SimpleTankBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TankMenu extends ChemistryMenu<SimpleTankBlockEntity> {
  private final LargerIntDataSlots capacity;

  public TankMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(SimpleTankBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public TankMenu(int id, Inventory inv, SimpleTankBlockEntity be){
    super(ModMenus.TANK_MENU.get(),id,inv,be);
    capacity = new LargerIntDataSlots(() -> be.getFluidTank().getCapacity());
    this.addDataSlot(capacity.getLittleSlot());
    this.addDataSlot(capacity.getBiggerSlot());
  }

  public FluidStack getFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  
  public int getTankCapacity(){
    return capacity.getValue();
  }

  @Override
  protected void internalSlots(SimpleTankBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 98, 18));
    this.addSlot(new SlotItemHandler(handler, 1, 98, 50));
  }

  @Override
  protected int slotCount() {
    return 2;
  }

  @Override
  public boolean stillValid(@Nonnull Player player) {
    return true;// Larger Tank may be accessed from far away
  }
}
