package kandango.reagenica.item;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.screen.OreBagMenu;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class OreBag extends Item{

  public OreBag(){
    super(new Item.Properties().stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
      if (!level.isClientSide) {
          ItemStack stack = player.getItemInHand(hand);
          int slot = findSlot(player, stack);
          if (slot == -1) {
            return InteractionResultHolder.fail(stack); // 見つからないケース
          }
          NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                  (windowId, inventory, p) -> new OreBagMenu(windowId, inventory, slot),
                  Component.translatable("gui.reagenica.ore_bag")
          ), buf -> buf.writeVarInt(slot));
      }
      return InteractionResultHolder.success(player.getItemInHand(hand));
  }

  private int findSlot(Player player, ItemStack target) {
    Inventory inv = player.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
        if (ItemStack.isSameItemSameTags(inv.getItem(i), target)) {
            return i;
        }
    }
    return -1;
  }

  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag tag = super.getShareTag(stack);
    if (tag == null) tag = new CompoundTag();
    LazyOptional<IItemHandler> maybeitemhandler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
    if(maybeitemhandler.isPresent()){
      IItemHandler handler = maybeitemhandler.orElseThrow(() -> new IllegalStateException("what happened in this moment!?"));
        if (handler instanceof INBTSerializable<?> serializable) {
          tag.put("Inventory", serializable.serializeNBT());
        }else{
        }
    }else{
    }
    return tag;
  }

  @Override
  public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
    super.readShareTag(stack, tag);
    if (tag != null && tag.contains("Inventory")) {
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            if (handler instanceof INBTSerializable<?> genericserializable) {
              @SuppressWarnings("unchecked")
              INBTSerializable<CompoundTag> serializable = (INBTSerializable<CompoundTag>) genericserializable;
              serializable.deserializeNBT(tag.getCompound("Inventory"));
            }
        });
    }
  }

  @Override
  public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    //stack.getOrCreateTag().put("Inventory", new CompoundTag());
    return new OreBagProvider(nbt);
  }

  public static class OreBagProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final ItemStackHandler handler;
    private final LazyOptional<IItemHandler> lazyHandler;

    public OreBagProvider(@Nullable CompoundTag nbt) {
      this.handler = new ItemStackHandler(27){
        @Override
        public boolean isItemValid(int slot, @Nullable ItemStack stack) {
          return isAcceptableItem(stack);
        }
      };
      if (nbt != null) {
        this.handler.deserializeNBT(nbt);
      }else{
        ChemistryMod.LOGGER.debug("OreBag NBT was null, creating new tag.");
      }
      this.lazyHandler = LazyOptional.of(() -> handler);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return cap == ForgeCapabilities.ITEM_HANDLER ? lazyHandler.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
      return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
      handler.deserializeNBT(nbt);
    }

    private static boolean isAcceptableItem(ItemStack stack){
      if (stack.is(ChemiTags.Items.ORE_BAG_ACCEPT)) return true;
      return false;
    }
  }
}
