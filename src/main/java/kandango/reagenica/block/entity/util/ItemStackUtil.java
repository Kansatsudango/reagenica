package kandango.reagenica.block.entity.util;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackUtil {
  public static boolean canAddStack(ItemStack origin, ItemStack insert){
    if(insert.isEmpty() || origin.isEmpty())return true;
    return ItemStack.isSameItemSameTags(origin, insert) && origin.getCount()+insert.getCount() <= origin.getMaxStackSize();
  }
  public static int canAddStackPartially(ItemStack origin, ItemStack insert){
    if(insert.isEmpty())return 0;
    if(origin.isEmpty())return insert.getCount();
    if(!ItemStack.isSameItemSameTags(origin, insert))return 0;
    int maxstack = origin.getMaxStackSize();
    int origincount = origin.getCount();
    int insertcount = insert.getCount();
    return Math.min(maxstack-origincount,insertcount);
  }

  public static ItemStack addStack(ItemStack original, ItemStack inserting){
    ItemStack origin = original.copy();
    ItemStack insert = inserting.copy();
    if(!canAddStack(origin, insert))throw new IllegalArgumentException("Could not merge items");
    if(origin.isEmpty()){
      return insert;
    }else if(insert.isEmpty()){
      return origin;
    }else{
      origin.grow(insert.getCount());
      return origin;
    }
  }

  public static void addStackToSlot(ItemStackHandler handler, int slot, ItemStack stack){
    ItemStack merged = addStack(handler.getStackInSlot(slot), stack);
    handler.setStackInSlot(slot, merged);
  }
  public static boolean addStackToSlotifPossible(ItemStackHandler handler, int slot, ItemStack stack){
    ItemStack stackinhandler = handler.getStackInSlot(slot);
    if(canAddStack(stackinhandler, stack)){
      ItemStack merged = addStack(stackinhandler, stack);
      handler.setStackInSlot(slot, merged);
      return true;
    }
    return false;
  }
  public static ItemStack addStackToSlotAsMuchAsPossible(ItemStackHandler handler, int slot, ItemStack stack){
    ItemStack stackinhandler = handler.getStackInSlot(slot);
    int addstack = canAddStackPartially(stackinhandler, stack);
    if(addstack>0){
      if(canAddStack(stackinhandler, stack)){
        addStackToSlot(handler, slot, stack);
        return ItemStack.EMPTY;
      }else{
        ItemStack merged = stackinhandler.copy();
        merged.setCount(stackinhandler.getMaxStackSize());
        handler.setStackInSlot(slot, merged);
        ItemStack residual = stack.copy();
        residual.shrink(addstack);
        return residual;
      }
    }else{
      return stack;
    }
  }
  public static void shrinkSlot(ItemStackHandler handler, int slot, int count){
    ItemStack stack = handler.getStackInSlot(slot).copy();
    stack.shrink(count);
    handler.setStackInSlot(slot, stack);
  }

  public static int getFuelExceptforLava(ItemStack itemstack){
    int burn = ForgeHooks.getBurnTime(itemstack, RecipeType.SMELTING);
    if(itemstack.getItem() == Items.LAVA_BUCKET)burn=0;
    return burn;
  }

  public static boolean isEnough(ItemStack is, ItemStack cost){
    if(cost.isEmpty())return true;
    if(is.isEmpty())return false;
    if(ItemStack.isSameItemSameTags(is, cost)){
      return is.getCount() >= cost.getCount();
    }
    return false;
  }
  public static void drop(@Nonnull Level lv,BlockPos pos, ItemStack item){
    if(lv.isClientSide)return;
    if(!item.isEmpty()){
      double x = pos.getX() + 0.5;
      double y = pos.getY() + 1.1;
      double z = pos.getZ() + 0.5;
      ItemEntity itemEntity = new ItemEntity(lv, x, y, z, item);
      lv.addFreshEntity(itemEntity);
    }
  }
  public static boolean insertOrElseThrow(@Nonnull Level lv,BlockPos pos, ItemStackHandler handler, ItemStack item, int start, int end){
    ItemStack stack = item.copy();
    for(int i=start;i<end;i++){
      stack = addStackToSlotAsMuchAsPossible(handler, i, stack);
      if(stack.isEmpty())return true;
    }
    drop(lv, pos, stack);
    return false;
  }

  public static ItemStack getDamagedItem(ItemStack original, int damage, Supplier<ItemStack> itemOnbrake){
    ItemStack stack = original.copy();
    int maxdur = stack.getMaxDamage();
    if(maxdur==0)return original;
    int dur = stack.getDamageValue();
    dur += damage;
    if(dur >= maxdur){
      return itemOnbrake.get();
    }else{
      stack.setDamageValue(dur);
      return stack;
    }
  }
  public static boolean damageItemInSlot(ItemStackHandler handler, int slot, int damage, Supplier<ItemStack> itemOnbrake){
    ItemStack stack = handler.getStackInSlot(slot);
    ItemStack damaged = getDamagedItem(stack, damage, itemOnbrake);
    handler.setStackInSlot(slot, damaged);
    return ItemStack.isSameItem(stack, damaged);
  }
}
