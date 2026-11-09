package kandango.reagenica.block.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.screen.ComputerMenu;
import kandango.reagenica.world.ChemiPOIs;
import kandango.reagenica.world.retail.RetailCompanyData;
import kandango.reagenica.world.retail.SKU;
import kandango.reagenica.world.retail.SavedShopData;
import kandango.reagenica.world.retail.SingleItem;
import kandango.reagenica.world.retail.Transaction;
import kandango.reagenica.world.retail.customer.Customer;
import kandango.reagenica.world.retail.customer.MerchandiseProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class StoreManagerComputerBlockEntity extends BlockEntity implements MenuProvider,ITickableBlockEntity{
  private final Set<BlockPos> shelfs = new HashSet<>();
  private String company_name = "Sample";
  private String shop_name = "Sample";
  private final Random rand = new Random();

  public StoreManagerComputerBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.STOREMANAGER_COMPUTER.get(),pos,state);
  }

  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    this.company_name = tag.getString("CompanyName");
    this.shop_name = tag.getString("ShopName");
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    tag.putString("CompanyName", company_name);
    tag.putString("ShopName", shop_name);
    super.saveAdditional(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    return super.getCapability(cap, side);
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
    return new ComputerMenu(id, inv);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("block.reagenica.storemanager_computer");
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
  
  @Override
  public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
  }

  @Override
  public void serverTick(){
    Level lv = this.level;
    final ServerLevel slv = lv instanceof ServerLevel s ? s : null;
    if(slv==null)return;

    ServerPlayer sp = slv.getRandomPlayer();
    if(sp==null)return;
    final RetailCompanyData company = SavedShopData.get(slv.getServer()).getOrCreateCompany(company_name, sp.getUUID());
    
    if(lv.getGameTime()%20==11){
      this.shelfs.clear();
      final PoiManager poiManager = slv.getPoiManager();
      poiManager.findAll(
          holder -> holder.is(ChemiPOIs.STALL_POI.getKey()), 
          pos -> true, 
          worldPosition, 64, PoiManager.Occupancy.ANY
        ).forEach(pos -> this.shelfs.add(pos));
    }

    if(rand.nextInt(2)==0){
      List<IItemHandler> handlers = this.shelfs.stream().map(pos -> slv.getBlockEntity(pos)).filter(Objects::nonNull)
                  .map(be -> be.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null)).filter(Objects::nonNull)
                  .toList();
      MergedItemHandler handler = new MergedItemHandler(handlers);
      Customer customer = Customer.Builder.of().createWith(SKU.getAllKnownSKU(slv));
      List<MerchandiseProperty> bought = customer.buyFrom(handler, sku -> sku.getBasePrice(), slv);
      List<SingleItem> items = bought.stream().map(mp -> SingleItem.of(mp, this::baseCost)).toList();
      if(!items.isEmpty()){
        Transaction transaction = new Transaction(shop_name, slv.getGameTime(), items);
        company.putTransaction(transaction);
      }
    }
  }
  private int baseCost(SKU sku){
    return sku.getBaseCost();
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
  }

  public static class MergedItemHandler implements IItemHandler {
    private final List<? extends IItemHandler> handlers;

    public MergedItemHandler(List<? extends IItemHandler> handlers){
      this.handlers = List.copyOf(handlers);
    }

    @Override
    public int getSlots() {
      return this.handlers.stream().mapToInt(IItemHandler::getSlots).sum();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
      long x = find(slot);
      return handlers.get(decodeHandlerIndex(x)).getStackInSlot(decodeSlotIndex(x));
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      long x = find(slot);
      return handlers.get(decodeHandlerIndex(x)).insertItem(decodeSlotIndex(x), stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      long x = find(slot);
      return handlers.get(decodeHandlerIndex(x)).extractItem(decodeSlotIndex(x), amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
      long x = find(slot);
      return handlers.get(decodeHandlerIndex(x)).getSlotLimit(decodeSlotIndex(x));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      long x = find(slot);
      return handlers.get(decodeHandlerIndex(x)).isItemValid(decodeSlotIndex(x), stack);
    }

    private long find(int index){
      if(index < 0)throw new IndexOutOfBoundsException();
      int offset = index;
      for(int i=0; i<handlers.size(); i++){
        IItemHandler handler = handlers.get(i);
        int slots = handler.getSlots();
        if(offset < slots){
          return encode(i, offset);
        }else{
          offset -= slots;
        }
      }
      throw new IndexOutOfBoundsException();
    }

    private int decodeHandlerIndex(long x){
      return (int)(x>>32 & 0xffffffff);
    }
    private int decodeSlotIndex(long x){
      return (int)(x & 0xffffffff);
    }
    private long encode(int handlerIndex, int slotIndex){
      return (long)handlerIndex<<32 | slotIndex;
    }
  }
}
