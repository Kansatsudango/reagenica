package kandango.reagenica.block.entity.electrical;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.block.entity.electrical.Handlers.GeneratorEnergyHandler;
import kandango.reagenica.network.CableNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

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
    if(lv==null)return;
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
      customer.getCapability(ForgeCapabilities.ENERGY, term.getValue().side).filter(s -> s.canReceive()).ifPresent(s -> customers.add(new StorageAndCost(s, term.getValue().cost)));
    }
    int maxExtract = this.energyStorage.extractEnergy(Integer.MAX_VALUE, true);
    int demand = customers.stream().mapToInt(s -> demand(s, getOfferUnit())).sum();
    if(demand>0 && maxExtract >= demand){
      for(StorageAndCost customer : customers){
        IEnergyStorage storage = customer.storage;
        double cost = customer.cost;
        int inserted = storage.receiveEnergy((int)(demand(customer, getOfferUnit())-cost), false);
        if(inserted!=0){
          this.energyStorage.extractEnergy(inserted+(int)(0.91d+cost), false);
        }
      }
    }
  }
  private static int demand(StorageAndCost str, int offer){
    IEnergyStorage storage = str.storage;
    int inputtable = storage.receiveEnergy(offer, true);
    if(inputtable >= offer-str.cost)return offer;
    else if(inputtable >= offer/2)return inputtable;
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
    PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.cost()));
    Map<BlockPos, Double> costMap = new HashMap<>();
    for(Direction dir : Direction.values()){
      if(this.getCapability(ForgeCapabilities.ENERGY, dir).filter(s -> s.canExtract()).isPresent()){
        BlockPos rel = startpos.relative(dir);
        BlockEntity be = lv.getBlockEntity(rel);
        if(be instanceof ElectricCableAbstract){
          queue.add(new Node(rel, 0.1));
          costMap.put(rel, 0.1);
        }else if(be!=null){
          Direction inputtingSide = dir.getOpposite();
          if(be.getCapability(ForgeCapabilities.ENERGY, inputtingSide).filter(str -> str.canReceive()).isPresent()){
            costMap.put(rel, 0.1);
            this.customerCostMap.put(rel,new ElectroTerminal(0.1,inputtingSide));
          }
        }
      }
    }
    costMap.put(startpos, 0.0);
    int watchdog = 0;
    while (!queue.isEmpty()) {
      watchdog++;
      if(watchdog>1000){
        ChemistryMod.LOGGER.warn("Cable tracing exceeded safety limit (1000). Origin: {}",startpos.toShortString());
        return;
      }
      Node node = queue.poll();
      if(costMap.get(node.pos) < node.cost)continue;
      for(Direction dir : Direction.values()){
        BlockPos neighbor = node.pos.relative(dir);
        BlockEntity be = lv.getBlockEntity(neighbor);
        if(be instanceof ElectricCableAbstract cable){
          double resistance = cable.getResistance();
          double newcost = node.cost + resistance;
          if(newcost < costMap.getOrDefault(neighbor, Double.POSITIVE_INFINITY)){
            costMap.put(neighbor, newcost);
            queue.add(new Node(neighbor, newcost));
          }
        }else if(be!=null){
          Direction inputtingSide = dir.getOpposite();
          if(be.getCapability(ForgeCapabilities.ENERGY, inputtingSide).filter(str -> str.canReceive()).isPresent()){
            Double previous = costMap.getOrDefault(neighbor,Double.POSITIVE_INFINITY);
            if(previous > node.cost){
              costMap.put(neighbor, node.cost);
              this.customerCostMap.put(neighbor,new ElectroTerminal(node.cost,inputtingSide));
            }
          }
        }
      }
    }
  }
  public void tellCustomers(CommandSourceStack source){
    source.sendSystemMessage(Component.literal("This Block has customers below:"));
    customerCostMap.entrySet().stream().forEach(entry -> {
      source.sendSystemMessage(Component.literal(entry.getKey().toShortString() + " costs " + entry.getValue().cost).withStyle(ChatFormatting.AQUA));
    });
  }
  private record Node(BlockPos pos, double cost) {
  }
  private record ElectroTerminal(double cost, Direction side) {
  }
  private record StorageAndCost(IEnergyStorage storage, double cost) {
  }
}
