package kandango.reagenica.block.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.screen.ComputerMenu;
import kandango.reagenica.world.ChemiPOIs;
import kandango.reagenica.world.retail.RetailCompanyData;
import kandango.reagenica.world.retail.SavedShopData;
import kandango.reagenica.world.retail.Transaction;
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

    if(rand.nextInt(20)==0){
      System.out.println(this.shelfs.size());
      getRandomShelf().ifPresent(pos -> {
        BlockEntity be = slv.getBlockEntity(pos);
        be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
          int size = handler.getSlots();
          ItemStack stack = handler.extractItem(rand.nextInt(size), 1, false);
          if(!stack.isEmpty()){
            Transaction transaction = new Transaction(shop_name, lv.getGameTime(), List.of(new Transaction.SingleItem(stack, 100, 80)));
            company.putTransaction(transaction);
          }
        });
      });
    }
  }
  private Optional<BlockPos> getRandomShelf(){
    int size = this.shelfs.size();
    if(size==0)return Optional.empty();
    else return Optional.of(this.shelfs.stream().toList().get(rand.nextInt(size)));
  }
  @Override
  public void invalidateCaps(){
    super.invalidateCaps();
  }
}
