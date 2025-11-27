package kandango.reagenica;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

//This is utility class
public class ChemiGeometry {
  private final Space s1;
  private final Space s2;
  private ChemiGeometry(Space s1,Space s2){
    this.s1=s1;
    this.s2=s2;
  }
  private ChemiGeometry(double x1,double y1,double z1,double x2,double y2,double z2){
    this.s1=new Space(x1, y1, z1);
    this.s2=new Space(x2, y2, z2);
  }
  private ChemiGeometry(double x1,double y1,double z1){
    this.s1=new Space(x1, y1, z1);
    this.s2=null;
  }
  public static ChemiGeometry create(double x1,double y1,double z1,double x2,double y2,double z2){
    return new ChemiGeometry(x1, y1, z1, x2, y2, z2);
  }
  public static ChemiGeometry create(double x1,double y1,double z1){
    return new ChemiGeometry(x1, y1, z1);
  }

  public ChemiGeometry rotate(Direction dir){
    Space rs1 = rotatesp(this.s1, dir);
    Space rs2 = rotatesp(this.s2, dir);
    return new ChemiGeometry(rs1,rs2);
  }
  public ChemiGeometry flush(){
    if(s2==null) throw new IllegalStateException("Not cubic object!");
    double x1 = s1.x<s2.x ? s1.x : s2.x;
    double x2 = s1.x>s2.x ? s1.x : s2.x;
    double y1 = s1.y<s2.y ? s1.y : s2.y;
    double y2 = s1.y>s2.y ? s1.y : s2.y;
    double z1 = s1.z<s2.z ? s1.z : s2.z;
    double z2 = s1.z>s2.z ? s1.z : s2.z;
    return new ChemiGeometry(x1, y1, z1, x2, y2, z2);
  }
  public Space getLeastPole(){
    return s1;
  }
  public Space getMostPole(){
    if(s2==null)throw new IllegalStateException("Not cubic object!");
    return s2;
  }
  public AABB toAABB(){
    if(s2==null)throw new IllegalStateException("Not cubic object!");
    return new AABB(s1.x,s1.y,s1.z,s2.x,s2.y,s2.z);
  }
  private Space rotatesp(@Nullable Space space, Direction dir){
    if(space==null)return null;
    if(dir == Direction.NORTH)return new Space(space.x, space.y, space.z);
    else if(dir == Direction.SOUTH)return new Space(1-space.x, space.y, 1-space.z);
    else if(dir == Direction.EAST)return new Space(1-space.z, space.y, space.x);
    else if(dir == Direction.WEST)return new Space(space.z, space.y, 1-space.x);
    else if(dir == Direction.UP)return new Space(space.x, 1-space.z, space.y);
    else if(dir == Direction.DOWN)return new Space(space.x, space.z, 1-space.y);
    else throw new IllegalArgumentException("Unknown Direction "+dir.toString());
  }
  public record Space(double x,double y,double z) {
  }
}
