package kandango.reagenica.screen;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.block.entity.OnsenMinerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OnsenMinerMenu extends ChemistryMenu<OnsenMinerBlockEntity> {
  public OnsenMinerMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(OnsenMinerBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public OnsenMinerMenu(int id, Inventory inv, OnsenMinerBlockEntity be){
    super(ModMenus.ONSEN_MINER_MENU.get(),id,inv,be);
  }

  public FluidStack getFluid(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTank().getFluid()).orElse(FluidStack.EMPTY);
  }
  public FluidTank getFluidTank(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFluidTank()).orElse(new FluidTank(8000));
  }

  @Override
  protected void internalSlots(OnsenMinerBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 98, 18));
    this.addSlot(new SlotItemHandler(handler, 1, 98, 50));
  }

  @Override
  protected int slotCount() {
    return 2;
  }
}
