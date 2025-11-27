package kandango.reagenica.block.entity.fluidsyncer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SyncDualFluidTanksPacket;
import kandango.reagenica.packet.SyncFluidPacket;
import kandango.reagenica.packet.SyncQuadFluidTanksPacket;
import kandango.reagenica.screen.ChemistryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;

public class FluidSyncHelper {
  private final List<FluidTank> tanks;
  private List<FluidTank> lasttanks;
  private final BlockPos pos;
  private final SyncType type;
  private int tick=60;

  public FluidSyncHelper(BlockPos pos, SyncType type, FluidTank... tanks){
    this.tanks = Arrays.asList(tanks);
    save();
    this.pos = pos;
    this.type=type;
  }

  public void syncifneeded(ServerLevel lv){//call me every tick
    if(tick==60){//every 3 second force-sync
      tick=1;
      syncToChunk(lv);
    }else{
      tick++;
      if(shouldSync()){
        syncToChunk(lv);
      }else{
        for(ServerPlayer player : lv.players()){
          if(player.containerMenu instanceof ChemistryMenu menu){
            @Nullable BlockEntity be = menu.getBlockEntity();
            if(be!=null && be.getBlockPos().equals(pos)){
              syncToPlayer(lv, player);
            }
          }
        }
      } 
    }
  }
  private boolean shouldSync(){
    for(int i=0;i<tanks.size();i++){
      if(isSignificantChange(tanks.get(i), lasttanks.get(i))){
        return true;
      }
    }
    return false;
  }
  private boolean isSignificantChange(FluidTank tank1, FluidTank tank2){
    if(type==SyncType.GUIONLY)return false;
    FluidStack f1 = tank1.getFluid();
    FluidStack f2 = tank2.getFluid();
    if((f1.isEmpty()||f2.isEmpty()) && !(f1.isEmpty()&&f2.isEmpty()))return true;
    if(!f1.isFluidEqual(f2))return true;
    if(type==SyncType.ALLNOTHING)return false;
    int f1amount = f1.getAmount();
    int f2amount = f2.getAmount();
    int max = tank1.getCapacity();
    int diff = f1amount-f2amount;
    return (diff>(max/20) || diff<(-max/20));
  }
  private void save(){
    this.lasttanks = new ArrayList<>(this.tanks.size());
    for (FluidTank tank : this.tanks) {
      this.lasttanks.add(new FluidTank(tank.getCapacity()));
      this.lasttanks.get(this.lasttanks.size()-1).setFluid(tank.getFluid().copy());
    }
  }
  private void syncToChunk(ServerLevel lv){
    switch (tanks.size()) {
      case 1:
        ModMessages.CHANNEL.send(
          PacketDistributor.TRACKING_CHUNK.with(() -> lv.getChunkAt(pos)),
            new SyncFluidPacket(pos, tanks.get(0).getFluid().copy()));
        break;
      case 2:
        ModMessages.CHANNEL.send(
          PacketDistributor.TRACKING_CHUNK.with(() -> lv.getChunkAt(pos)),
            new SyncDualFluidTanksPacket(pos, tanks.get(0).getFluid().copy(), tanks.get(1).getFluid().copy()));
        break;
      case 3:
        throw new UnsupportedOperationException("Triple tank is not supported now.");
      case 4:
        ModMessages.CHANNEL.send(
          PacketDistributor.TRACKING_CHUNK.with(() -> lv.getChunkAt(pos)),
            new SyncQuadFluidTanksPacket(pos, tanks.get(0).getFluid().copy(), tanks.get(1).getFluid().copy(), tanks.get(2).getFluid().copy(), tanks.get(3).getFluid().copy()));
        break;
      default:
        throw new IllegalStateException("Tank count was invalid.");
    }
    save();
  }
  private void syncToPlayer(ServerLevel lv, ServerPlayer player){
    switch (tanks.size()) {
      case 1:
        ModMessages.CHANNEL.send(
          PacketDistributor.PLAYER.with(() -> player),
            new SyncFluidPacket(pos, tanks.get(0).getFluid().copy()));
        break;
      case 2:
        ModMessages.CHANNEL.send(
          PacketDistributor.PLAYER.with(() -> player),
            new SyncDualFluidTanksPacket(pos, tanks.get(0).getFluid().copy(), tanks.get(1).getFluid().copy()));
        break;
      case 3:
        throw new UnsupportedOperationException("Triple tank is not supported now.");
      case 4:
        ModMessages.CHANNEL.send(
          PacketDistributor.PLAYER.with(() -> player),
            new SyncQuadFluidTanksPacket(pos, tanks.get(0).getFluid().copy(), tanks.get(1).getFluid().copy(), tanks.get(2).getFluid().copy(), tanks.get(3).getFluid().copy()));
        break;
      default:
        throw new IllegalStateException("Tank count was invalid.");
    }
  }
  public enum SyncType{
    ALWAYS,
    GUIONLY,
    ALLNOTHING
  }
}
