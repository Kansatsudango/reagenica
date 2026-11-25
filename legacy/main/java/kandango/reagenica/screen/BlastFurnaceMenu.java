package kandango.reagenica.screen;

import java.util.List;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiUtils;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.BlastFurnaceBlockEntity;
import kandango.reagenica.screen.slots.SlotPriorityRule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlastFurnaceMenu extends ChemistryMenu<BlastFurnaceBlockEntity> {
  
  public BlastFurnaceMenu(int id, Inventory inv, FriendlyByteBuf extradata){
    this(id,inv,(BlastFurnaceBlockEntity)inv.player.level().getBlockEntity(extradata.readBlockPos()));
  }

  public BlastFurnaceMenu(int id, Inventory inv, BlastFurnaceBlockEntity be){
    super(ModMenus.BLAST_FURNACE_MENU.get(),id,inv,be);
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getTemp();}
      @Override
      public void set(int value) {be.setTemp(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getFuel();}
      @Override
      public void set(int value) {be.setFuel(value);}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getFuelMax();}
      @Override
      public void set(int value) {be.setFuelMax(value);}
    });
    this.addDataSlot(new DataSlot() {// least 16bit
      @Override
      public int get() {return be.getProgress()&0xFFFF;}
      @Override
      public void set(int value) {be.setProgress((be.getProgress() & 0xFFFF0000) | (value & 0xFFFF));}
    });
    this.addDataSlot(new DataSlot() {// most 16bit
      @Override
      public int get() {return (be.getProgress() >> 16) & 0xFFFF;}
      @Override
      public void set(int value) {be.setProgress((be.getProgress() & 0xFFFF) | ((value & 0xFFFF) << 16));}
    });
    this.addDataSlot(new DataSlot() {
      @Override
      public int get() {return be.getmaxTemp();}
      @Override
      public void set(int value) {be.setmaxTemp(value);}
    });
  }

  public int getTemp(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getTemp()).orElse(0);
  }
  public int getBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuel()).orElse(0);
  }
  public int getMaxBurnTime(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getFuelMax()).orElse(0);
  }
  public int getProgress(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getProgress()).orElse(0);
  }
  public double getSliderValue(){
    return ChemiUtils.nonNullOrLog(this.blockEntity).map(x -> x.getSliderValue()).orElse(0.0);
  }

  @Override
  public List<SlotPriorityRule> quickMoveRules() {
    List<SlotPriorityRule> rules = List.of(
      SlotPriorityRule.single(stack -> stack.is(ItemTags.create(new ResourceLocation("minecraft", "coals"))), 1)
    );
    return rules;
  }

  @Override
  protected void internalSlots(BlastFurnaceBlockEntity be) {
    IItemHandler handler = be.getItemHandler();
    this.addSlot(new SlotItemHandler(handler, 0, 56, 23));
    this.addSlot(new SlotItemHandler(handler, 1, 56, 59));
    this.addSlot(new SlotItemHandler(handler, 2, 116, 24){
      @Override
      public void onTake(@Nonnull Player player, @Nonnull ItemStack stack){
        super.onTake(player, stack);
        if(!player.level().isClientSide){
          awardExp(player);
        }
      }
    });
    this.addSlot(new SlotItemHandler(handler, 3, 116, 48));
  }
  @Override
  protected ItemStack quickMoveStackFunc(int slotcount, @Nonnull Player player, int index, List<SlotPriorityRule> rules) {
    if(index==2){
      if(!player.level().isClientSide){
        awardExp(player);
      }
    }
    return super.quickMoveStackFunc(slotcount, player, index, rules);
  }

  @Override
  protected int slotCount() {
    return 4;
  }

  private void awardExp(Player pl){
    BlastFurnaceBlockEntity be = this.blockEntity;
    if(be==null){
      ChemistryMod.LOGGER.warn("Tried to get Experience while Blockentity was null!");
    }else{
      be.awardExp(pl);
    }
  }
}
