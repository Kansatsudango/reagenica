package kandango.reagenica.item;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import kandango.reagenica.ChemiComponents;
import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.screen.SimpleBagMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CommonBag<T extends AbstractContainerMenu> extends Item implements IBagItem{
  public static final String UUIDKey = "BagUUID";
  private static final Predicate<ItemStack> isAllowed = stack -> !stack.is(ChemiTags.Items.BAGS_DENY) && stack.getCapability(Capabilities.ItemHandler.ITEM)==null;
  private final int slotCount;
  private final int inv_start;
  private final DeferredHolder<MenuType<?>, MenuType<T>> menutype;
  private final Predicate<ItemStack> filter;
  private final boolean hasSpecialFilter;

  public CommonBag(int slots, int inv_start, DeferredHolder<MenuType<?>, MenuType<T>> type){
    super(new Item.Properties().stacksTo(1));
    this.slotCount = slots;
    this.inv_start = inv_start;
    this.menutype = type;
    this.filter = isAllowed;
    this.hasSpecialFilter = false;
  }
  public CommonBag(int slots, int inv_start, DeferredHolder<MenuType<?>, MenuType<T>> type, Predicate<ItemStack> filter){
    super(new Item.Properties().stacksTo(1));
    this.slotCount = slots;
    this.inv_start = inv_start;
    this.menutype = type;
    this.filter = isAllowed.and(filter);
    this.hasSpecialFilter = true;
  }

  public boolean isValidItem(ItemStack stack){
    return this.filter.test(stack);
  }
  public boolean canAutoStock(){
    return this.hasSpecialFilter;
  }
  public int getSlotCount(){
    return this.slotCount;
  }

  public IItemHandlerModifiable createItemHandler(ItemStack owner){
    return new ComponentItemHandler(owner, ChemiComponents.BAG_CONTENTS.get(), this.slotCount){
      @Override
      public boolean isItemValid(int slot, ItemStack inserted){
        return inserted.isEmpty() || CommonBag.this.isValidItem(inserted);
      }
    };
  }

  @Override
  public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slot, boolean selected) {
    super.inventoryTick(stack, level, entity, slot, selected);
    if (!level.isClientSide) {
      if(stack.get(ChemiComponents.BAG_UUID.get())==null){
        UUID id = UUID.randomUUID();
        stack.set(ChemiComponents.BAG_UUID.get(), id);
        ChemistryMod.LOGGER.debug( "Issued new UUID for a bag. UUID:{}, Holder:{}",id,entity);
      }
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
    if (player instanceof ServerPlayer sp) {
      ItemStack stack = player.getItemInHand(hand);
      UUID id = getOrCreateBagID(stack);
      OptionalInt mayslot = findSlot(player, stack, id);
      if (mayslot.isEmpty()) return InteractionResultHolder.fail(stack);
      int slot = mayslot.getAsInt();
      getBagID(stack).ifPresentOrElse(uuid -> {
        sp.openMenu(new SimpleMenuProvider(
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

  private OptionalInt findSlot(Player player, ItemStack target, UUID uuid) {
    Inventory inventory = player.getInventory();
    for (int i = 0; i < inventory.getContainerSize(); i++) {
        if (inventory.getItem(i) == target) {
            return OptionalInt.of(i);
        }
    }
    for (int i = 0; i < inventory.getContainerSize(); i++) {
        ItemStack candidate = inventory.getItem(i);

        if (getBagID(candidate)
                .filter(uuid::equals)
                .isPresent()) {
            return OptionalInt.of(i);
        }
    }

    return OptionalInt.empty();
  }

    public static Optional<UUID> getBagID(ItemStack stack) {
      return Optional.ofNullable(stack.get(ChemiComponents.BAG_UUID.get()));
    }

    private static UUID getOrCreateBagID(ItemStack stack) {
      UUID uuid = stack.get(ChemiComponents.BAG_UUID.get());
      if (uuid == null) {
        uuid = UUID.randomUUID();
        stack.set(ChemiComponents.BAG_UUID.get(), uuid);
      }
      return uuid;
    }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    if(stack.getItem() instanceof CommonBag<?> bag && bag.canAutoStock()){
      tooltipComponents.add(Component.translatable("tooltip.reagenica.autostore").withStyle(ChatFormatting.GREEN));
    }
  }
}
