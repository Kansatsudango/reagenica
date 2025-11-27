package kandango.reagenica.block.entity;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import kandango.reagenica.ChemiTags;
import kandango.reagenica.ChemistryMod;
import kandango.reagenica.packet.LargeTankPropPacket;
import kandango.reagenica.packet.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;

public class LargeTankCoreBlockEntity extends SimpleTankBlockEntity{
  @Nullable private TankSize size = null;
  private int tick = 20;
  public LargeTankCoreBlockEntity(BlockPos pos, BlockState state){
    super(ModBlockEntities.LARGE_TANK_CORE.get(),pos,state);
  }
  @Override
  public Component getDisplayName() {
    return Component.translatable("gui.reagenica.large_tank");
  }

  @Override
  protected int tankSize() {
    return 0;
  }
  
  @Override
  public void load(@Nonnull CompoundTag tag){
    super.load(tag);
    byte width=tag.getByte("Width");
    byte height=tag.getByte("Height");
    if(width>0 && height>0){
      this.size = new TankSize(width, height);
    }
    this.fluidTank.setCapacity(tag.getInt("Capacity"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag tag){
    super.saveAdditional(tag);
    TankSize size = this.size;
    tag.putByte("Width", size!=null ? (byte)size.width : 0);
    tag.putByte("Height", size!=null ? (byte)size.height : 0);
    tag.putInt("Capacity", this.fluidTank.getCapacity());
  }

  @Override
  public AABB getRenderBoundingBox() {
    TankSize size = this.size;
    if(size==null)return super.getRenderBoundingBox();
    return new AABB(
      worldPosition.getX()-(size.width-1),
      worldPosition.getY(),
      worldPosition.getZ()-(size.width-1),
      worldPosition.getX()+size.width,
      worldPosition.getY()+size.height,
      worldPosition.getZ()+size.width
      );
  }

  @Override
  public void serverTick(){
    Level lv = level;
    if(lv==null)return;
    super.serverTick();
    if(tick>=20){
      tick=0;
      TankSize newsize = testforTankStructure(lv);
      if(!Objects.equals(this.size, newsize)){
        this.size=newsize;
        if(newsize!=null){
          int width = newsize.width*2-1;
          int height = newsize.height;
          this.fluidTank.setCapacity((width*width*(height-1))*48000);
          this.notifyToInterfaces(lv, newsize);
        }else{
          this.fluidTank.setCapacity(0);
        }
        syncSizeToClient();
      }
    }else{
      tick++;
    }
  }

  @Nullable private TankSize testforTankStructure(@Nonnull Level lv){
    int temp_size=getFloorSize(lv);
    if(temp_size!=0){
      int height = getHeight(lv, temp_size);
      if(height!=0){
        return new TankSize(temp_size, height);
      }
    }
    return null;
  }

  private int getFloorSize(@Nonnull Level lv){
    final int MAX_FLOOR = 4;
    BooleanArraySquare hasValidWall = new BooleanArraySquare(MAX_FLOOR);
    for(int x=-MAX_FLOOR;x<=MAX_FLOOR;x++){
      for(int z=-MAX_FLOOR;z<=MAX_FLOOR;z++){
        hasValidWall.set(x, z, lv.getBlockState(worldPosition.offset(x, 0, z)).is(ChemiTags.Blocks.TANK_BLOCKS));
      }
    }
    hasValidWall.set(0, 0, true);//Center is always valid!
    int size;
    for(size=1;size<=MAX_FLOOR;size++){
      for(int x=-size;x<=size;x++){
        for(int z=-size;z<=size;z++){
          if(!hasValidWall.get(x,z)){
            return size-1; // Last trial was valid
          }
        }
      }
    }
    return MAX_FLOOR;
  }
  private int getHeight(@Nonnull Level lv, int floorsize){
    int tempheight = 0;
    for(int y=2;y<=10;y++){
      BlockState state = lv.getBlockState(worldPosition.offset(0, y, 0));
      if(state.isAir()){
        continue;
      }else if(state.is(ChemiTags.Blocks.TANK_BLOCKS)){
        tempheight=y;// The first height finding tank blocks
        break;
      }else{
        break;// Other Block is not allowed
      }
    }
    if(tempheight==0)return 0;
    boolean failed = false;
    wallscan:for(int y=1;y<tempheight;y++){
      for(int x=-floorsize;x<=floorsize;x++){
        for(int z=-floorsize;z<=floorsize;z++){
          if(x==-floorsize || x==floorsize || z==floorsize || z==-floorsize){// if hollowline
            if(!(lv.getBlockState(worldPosition.offset(x,y,z)).is(ChemiTags.Blocks.TANK_BLOCKS))){
              failed=true;
              break wallscan;
            }
          }else{ // if inside
            if(!(lv.getBlockState(worldPosition.offset(x,y,z)).isAir())){
              failed=true;
              break wallscan;
            }
          }
        }
      }
    }
    if(failed)return 0;
    topscan:for(int x=-floorsize;x<=floorsize;x++){
      for(int z=-floorsize;z<=floorsize;z++){
        if(!lv.getBlockState(worldPosition.offset(x,tempheight,z)).is(ChemiTags.Blocks.TANK_BLOCKS)){
          failed=true;
          break topscan;
        }
      }
    }
    if(failed)return 0;
    else return tempheight;
  }

  private void notifyToInterfaces(@Nonnull Level lv, @Nonnull TankSize size){
    BlockPos.MutableBlockPos mutablePos = worldPosition.mutable();
    for(int x=-size.width;x<=size.width;x++){
      for(int z=-size.width;z<=size.width;z++){
        for(int y=0;y<=size.height;y++){
          mutablePos.set(worldPosition.getX()+x, worldPosition.getY()+y, worldPosition.getZ()+z);
          if(!lv.isLoaded(mutablePos))return;
          BlockEntity be = lv.getBlockEntity(mutablePos);
          if(be instanceof LargeTankInterfaceBlockEntity lti){
            lti.setMaster(worldPosition);
          }
        }
      }
    }
  }

  @Nullable public TankSize getTankSize(){
    return this.size;
  }

  public void receiveSizePacket(int width, int height, int capacity){
    if(width>0 && height>0){
      this.size = new TankSize(width, height);
    }else{
      this.size = null;
    }
    this.fluidTank.setCapacity(capacity);
    ChemistryMod.LOGGER.info("Received Packet: {},{},{}",width,height,capacity);
  }

  private void syncSizeToClient(){
    Level lv = this.level;
    if(lv != null && !lv.isClientSide){
      ModMessages.CHANNEL.send(
        PacketDistributor.TRACKING_CHUNK.with(
          () -> lv.getChunkAt(worldPosition)
          ),
          new LargeTankPropPacket(worldPosition, this.size==null ? 0 : this.size.width, this.size==null ? 0 : this.size.height,this.fluidTank.getCapacity())
      );
    }
  }

  public static record TankSize(int width, int height) {
  }
  private static class BooleanArraySquare{
    private boolean[][] array;
    private final int radius;
    public BooleanArraySquare(int radius){
      this.radius = radius;
      int size = radius*2+1;
      this.array = new boolean[size][size];
    }
    public void set(int x, int z, boolean value){
      validate(x, z);
      array[x+radius][z+radius]=value;
    }
    public boolean get(int x, int z){
      validate(x, z);
      return array[x+radius][z+radius];
    }
    private void validate(int x, int z){
      if(!(-radius<=x && x<=radius && -radius<=z && z<=radius)){
        throw new IndexOutOfBoundsException("Index (" + x + "," + z + ") is invalid.");
      }
    }
  }
}
