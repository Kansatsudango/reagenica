package kandango.reagenica.block.entity.electrical;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector3f;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.electrical.Handlers.GeneratorEnergyHandler;
import kandango.reagenica.network.CableNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ForgeCapabilities;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class ElectricGeneratorAbstract extends ElectricMachineAbstract{
  private Map<BlockPos,ElectroTerminal> customerCostMap = new HashMap<>();
  private boolean networkChanged = true;
  public void notifyChange(){
    this.networkChanged=true;
  }
  private final LazyOptional<IEnergyStorage> energyOutLazyOptional = LazyOptional.of(() -> new GeneratorEnergyHandler(energyStorage));

  public ElectricGeneratorAbstract(BlockEntityType<? extends ElectricGeneratorAbstract> type,BlockPos pos, BlockState state){
    super(type,pos,state);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return energyOutLazyOptional.cast();
    }
    return super.getCapability(cap, side);
  }
  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    energyOutLazyOptional.invalidate();
  }

  @Override
  public void serverTick(){
  }

  @Override
  public void onLoad(){
    if(level instanceof ServerLevel slv){
      CableNetworkManager.requestUpdate(slv, worldPosition);
    }
    super.onLoad();
  }

  @Override
  public void setRemoved(){
    if(level instanceof ServerLevel slv){
      CableNetworkManager.requestUpdate(slv, worldPosition);
    }
    super.setRemoved();
  }

  protected void provideEnergy(){
    Level lv = this.getLevel();
    ServerLevel slv = lv instanceof ServerLevel s ? s : null;
    if(slv==null)return;
    if(networkChanged){
      refleshCustomers();
    }
    List<StorageAndCost> customers = new ArrayList<>();
    for(Map.Entry<BlockPos,ElectroTerminal> term : customerCostMap.entrySet()){
      BlockPos customerpos = term.getKey();
      if(!lv.isLoaded(customerpos))continue;
      BlockEntity customer = lv.getBlockEntity(customerpos);
      if(customer==null){
        this.networkChanged=true;
        continue;
      }
      customer.getCapability(ForgeCapabilities.ENERGY, term.getValue().side).filter(s -> s.canReceive())
          .ifPresent(s -> customers.add(new StorageAndCost(s, term.getValue().info)));
    }
    int maxExtract = this.energyStorage.extractEnergy(Integer.MAX_VALUE, true);
    int demand = customers.stream().mapToInt(s -> demand(s, getOfferUnit())).sum();
    int energyStored = this.energyStorage.getEnergyStored();
    if(demand>0 && maxExtract >= demand){
      for(StorageAndCost customer : customers){
        IEnergyStorage storage = customer.storage;
        double cost = customer.info.cost;
        int inserted = storage.receiveEnergy(Math.max(0,(int)(demand(customer, getOfferUnit())-cost)), false);
        if(inserted!=0){
          this.energyStorage.extractEnergy(inserted+(int)(0.91d+cost), false);
        }
      }
    } 
    if(energyStored > maxExtract){ // Stored Energy was enough for extract
      int loadRate = maxExtract!=0 ? demand*1000/maxExtract : 0;
      if(loadRate>1000){
        DustParticleOptions dust = new DustParticleOptions(new Vector3f(0.9f, 0.2f, 0.0f), 1.0f);
        slv.sendParticles(dust, 
            worldPosition.getX() + 0.5,
            worldPosition.getY() + 0.5,
            worldPosition.getZ() + 0.5,
            3,
            0.5, 0.5, 0.5,0.02);
      }else if(loadRate>750){
        DustParticleOptions dust = new DustParticleOptions(new Vector3f(0.9f, 0.7f, 0.0f), 1.0f);
        slv.sendParticles(dust, 
            worldPosition.getX() + 0.5,
            worldPosition.getY() + 0.5,
            worldPosition.getZ() + 0.5,
            1,
            0.5, 0.5, 0.5,0.02);
      }
    }
  }
  private static int demand(StorageAndCost str, int offer){
    IEnergyStorage storage = str.storage;
    final int space = storage.receiveEnergy(Integer.MAX_VALUE, true);
    final int restriction = str.info.restriction;
    final int importUnit = Math.min(offer, restriction);
    if(space >= importUnit)return importUnit;
    else return 0;
  }
  private void refleshCustomers(){
    networkChanged=false;
    customerCostMap.clear();
    reofferEnergy();
  }
  protected int getOfferUnit(){//Overrideable
    return 40;
  }

  public void reofferEnergy(){
    BlockPos pos = this.getBlockPos();
    Level level = this.getLevel();
    if(level==null){
      networkChanged=true;
      return;
    }
    this.traceCable(pos, level);
  }
  private void traceCable(BlockPos startpos, @Nonnull Level lv){
    ChemistryMod.LOGGER.debug("Cable trace started from {}", startpos.toShortString());
    PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.info.cost()));
    Map<BlockPos, CostAndRestriction> costMap = new HashMap<>();
    for(Direction dir : Direction.values()){
      if(this.getCapability(ForgeCapabilities.ENERGY, dir).filter(s -> s.canExtract()).isPresent()){
        BlockPos rel = startpos.relative(dir);
        BlockEntity be = lv.getBlockEntity(rel);
        if(be instanceof ElectricCableAbstract cable){
          queue.add(new Node(rel, new CostAndRestriction(cable.getResistance(), cable.getRestriction())));
          costMap.put(rel, new CostAndRestriction(cable.getResistance(), cable.getRestriction()));
        }else if(be!=null){
          Direction inputtingSide = dir.getOpposite();
          if(be.getCapability(ForgeCapabilities.ENERGY, inputtingSide).filter(str -> str.canReceive()).isPresent()){
            costMap.put(rel, new CostAndRestriction(0, getOfferUnit()));
            this.customerCostMap.put(rel, new ElectroTerminal(new CostAndRestriction(0, getOfferUnit()), inputtingSide));
          }
        }
      }
    }
    costMap.put(startpos, new CostAndRestriction(0, getOfferUnit()));
    int watchdog = 0;
    while (!queue.isEmpty()) {
      watchdog++;
      if(watchdog>1000){
        ChemistryMod.LOGGER.warn("Cable tracing exceeded safety limit (1000). Origin: {}",startpos.toShortString());
        return;
      }
      Node node = queue.poll();
      if(costMap.get(node.pos).isBetterThan(node.info))continue;
      for(Direction dir : Direction.values()){
        BlockPos neighbor = node.pos.relative(dir);
        BlockEntity be = lv.getBlockEntity(neighbor);
        if(be instanceof ElectricCableAbstract cable){
          double resistance = cable.getResistance();
          double newcost = node.info.cost + resistance;
          int newRestriction = Math.min(node.info.restriction, cable.getRestriction());
          CostAndRestriction newCR = new CostAndRestriction(newcost, newRestriction);
          if(newCR.isBetterThan(costMap.get(neighbor))){
            costMap.put(neighbor, newCR);
            queue.add(new Node(neighbor, newCR));
          }
        }else if(be!=null){
          Direction inputtingSide = dir.getOpposite();
          if(be.getCapability(ForgeCapabilities.ENERGY, inputtingSide).filter(str -> str.canReceive()).isPresent()){
            CostAndRestriction previous = costMap.get(neighbor);
            if(node.info.isBetterThan(previous)){
              costMap.put(neighbor, node.info);
              this.customerCostMap.put(neighbor,new ElectroTerminal(node.info, inputtingSide));
            }
          }
        }
      }
    }
  }
  public void tellCustomers(CommandSourceStack source){
    source.sendSystemMessage(Component.literal("This Block has customers below:"));
    customerCostMap.entrySet().stream().forEach(entry -> {
      source.sendSystemMessage(Component.literal(entry.getKey().toShortString() + " costs " + entry.getValue().info.cost + " restricted to " + entry.getValue().info.restriction).withStyle(ChatFormatting.AQUA));
    });
  }
  private record Node(BlockPos pos, CostAndRestriction info) {
  }
  private record ElectroTerminal(CostAndRestriction info, Direction side) {
  }
  private record StorageAndCost(IEnergyStorage storage, CostAndRestriction info) {
  }
  private record CostAndRestriction(double cost, int restriction) {
    @SuppressWarnings("unused")
    public CostAndRestriction better(@Nullable CostAndRestriction another){
      return this.isBetterThan(another) ? this : another;
    }
    public boolean isBetterThan(@Nullable CostAndRestriction another){
      if(another==null) return true;
      if(another.restriction < this.restriction){
        return true;
      }else if(another.restriction > this.restriction){
        return false;
      }else{
        if(this.cost<another.cost)return true;
        else return false;
      }
    }
  }
}
