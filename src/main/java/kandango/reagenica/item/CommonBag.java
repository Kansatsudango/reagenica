package kandango.reagenica.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.screen.SimpleBagMenu;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

public class CommonBag<T extends AbstractContainerMenu> extends Item{
  public static final List<CommonBag<?>> Bags = new ArrayList<>();
  public static final String UUIDKey = "BagUUID";
  private static final Predicate<ItemStack> isAllowed = stack -> !stack.is(ChemiTags.Items.BAGS_DENY) && !stack.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent();
  private final int slotCount;
  private final int inv_start;
  private final RegistryObject<MenuType<T>> menutype;
  private final Predicate<ItemStack> filter;
  private final boolean hasSpecialFilter;

  public CommonBag(int slots, int inv_start, RegistryObject<MenuType<T>> type){
    super(new Item.Properties().stacksTo(1));
    this.slotCount = slots;
    this.inv_start = inv_start;
    this.menutype = type;
    this.filter = isAllowed;
    this.hasSpecialFilter = false;
    Bags.add(this);
  }
  public CommonBag(int slots, int inv_start, RegistryObject<MenuType<T>> type, Predicate<ItemStack> filter){
    super(new Item.Properties().stacksTo(1));
    this.slotCount = slots;
    this.inv_start = inv_start;
    this.menutype = type;
    this.filter = isAllowed.and(filter);
    this.hasSpecialFilter = true;
    Bags.add(this);
  }

  public boolean isValidItem(ItemStack stack){
    return this.filter.test(stack);
  }
  public boolean canAutoStock(){
    return this.hasSpecialFilter;
  }

  @Override
  public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slot, boolean selected) {
    if (!level.isClientSide) {
      CompoundTag tag = stack.getOrCreateTag();
      if (!tag.hasUUID(UUIDKey)) {
        UUID id = UUID.randomUUID();
        tag.putUUID(UUIDKey, id);
        ChemistryMod.LOGGER.debug("Issued new UUID for a bag. UUID:{}, Holder:{}", id, entity);
      }
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    if (!level.isClientSide) {
      ItemStack stack = player.getItemInHand(hand);
      OptionalInt mayslot = findSlot(player, stack);
      if (mayslot.isEmpty()) return InteractionResultHolder.fail(stack);
      int slot = mayslot.getAsInt();
      getBagID(stack).ifPresentOrElse(uuid -> {
        NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
              (windowId, inventory, p) -> new SimpleBagMenu(menutype.get(), inv_start, windowId, inventory, slot, uuid),
              this.getName(stack)
          ), buf -> {
            buf.writeVarInt(slot);
            buf.writeUUID(uuid);
          });
      },()->{
        ChemistryMod.LOGGER.error("Bag UUID was null when opening screen, skipped. Player:{}", player);
      });
    }
    return InteractionResultHolder.success(player.getItemInHand(hand));
  }

  private OptionalInt findSlot(Player player, ItemStack target) {
    Inventory inv = player.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
        if (ItemStack.isSameItemSameTags(inv.getItem(i), target)) {
            return OptionalInt.of(i);
        }
    }
    return OptionalInt.empty();
  }

  @Override
  public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    return new CommonBagProvider(stack, slotCount, this.filter);
  }

  public static Optional<UUID> getBagID(ItemStack stack){
    return Optional.ofNullable(stack.getTag()).filter(tag -> tag.hasUUID(UUIDKey)).map(tag -> tag.getUUID(UUIDKey));
  }

  public static class CommonBagProvider implements ICapabilityProvider{
    private final ItemStackHandler handler;
    private final LazyOptional<IItemHandler> lazyHandler;
    private final ItemStack stack;
    private final int slotsCount;

    public CommonBagProvider(ItemStack stack, int count, Predicate<ItemStack> isValid) {
      this.stack = stack;
      this.slotsCount = count;
      this.handler = new ItemStackHandler(count){
        @Override
        public boolean isItemValid(int slot, @Nullable ItemStack stack) {
          return isValid.test(stack);
        }
        @Override
        protected void onContentsChanged(int slot) {
          save();
        }
      };
      load();
      this.lazyHandler = LazyOptional.of(() -> handler);
    }
    private void save(){
      CompoundTag tag = stack.getOrCreateTag();
      tag.put("Inventory", handler.serializeNBT());
    }
    private void load(){
      CompoundTag tag = stack.getTag();
      if(tag!=null && tag.contains("Inventory")){
        CompoundTag inventoryTag = tag.getCompound("Inventory");
        int slotCount = inventoryTag.getInt("Size");
        if(slotCount != this.slotsCount){
          ChemistryMod.LOGGER.debug("Common Bag Inventory size changed.");
          inventoryTag.putInt("Size", this.slotsCount);
        }
        handler.deserializeNBT(inventoryTag);
      }
      CompoundTag fullTag = stack.save(new CompoundTag());
      if(fullTag.contains("ForgeCaps")){
        CompoundTag forgeCaps = fullTag.getCompound("ForgeCaps");
        if(forgeCaps.contains("Parent")){
          CompoundTag oldInv = forgeCaps.getCompound("Parent");
          ChemistryMod.LOGGER.info("Migrating old bag inventory...");
          handler.deserializeNBT(oldInv);
          save();
        }
      }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return cap == ForgeCapabilities.ITEM_HANDLER ? lazyHandler.cast() : LazyOptional.empty();
    }
  }
}
