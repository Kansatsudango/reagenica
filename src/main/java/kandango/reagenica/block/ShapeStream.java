package kandango.reagenica.block;

import java.util.HashMap;
import java.util.Map;

import kandango.reagenica.ChemiGeometry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeStream {
  private final VoxelShape shape;
  private ShapeStream(VoxelShape vox){
    this.shape=vox;
  }

  public static ShapeStream createEmpty(){
    return new ShapeStream(Shapes.empty());
  }
  public static ShapeStream of(VoxelShape vox){
    return new ShapeStream(vox);
  }
  public static ShapeStream create(double x1,double y1,double z1,double x2,double y2,double z2){
    return new ShapeStream(Block.box(x1, y1, z1, x2, y2, z2));
  }
  public ShapeStream add(VoxelShape vox){
    return new ShapeStream(Shapes.or(this.shape,vox));
  }
  public ShapeStream add(double x1,double y1,double z1,double x2,double y2,double z2){
    return new ShapeStream(Shapes.or(this.shape,Block.box(x1, y1, z1, x2, y2, z2)));
  }
  public ShapeStream addif(boolean condition,VoxelShape vox){
    if(condition) return new ShapeStream(Shapes.or(this.shape,vox));
    else return this;
  }
  public ShapeStream addif(boolean condition,double x1,double y1,double z1,double x2,double y2,double z2){
    if(condition) return new ShapeStream(Shapes.or(this.shape,Block.box(x1, y1, z1, x2, y2, z2)));
    else return this;
  }
  public VoxelShape build(){
    return shape.optimize();
  }
  public ShapeStream rotate(Direction dir){
    ShapeStream buf = ShapeStream.createEmpty();
    for(AABB box : shape.toAabbs()){
      AABB rotated = ChemiGeometry.create(box.minX,box.minY,box.minZ,box.maxX,box.maxY,box.maxZ)
                            .rotate(dir).flush().toAABB();
      buf = buf.add(Shapes.create(rotated));
    }
    return buf;
  }
  public Map<Direction,VoxelShape> createRots(Direction[] dirs){
    Map<Direction,VoxelShape> map = new HashMap<>();
    for(Direction dir : dirs){
      map.put(dir, rotate(dir).build());
    }
    return map;
  }
}
