package kandango.reagenica.client.renderer;

public record SquareRecord(
  float minX,
  float minY,
  float minZ,
  float maxX,
  float maxY,
  float maxZ,
  Hvector vec
) {
  public static SquareRecord createUpside(float y, float minx, float minz, float maxx, float maxz){
    return new SquareRecord(minx, y, minz, maxx, y, maxz, Hvector.UP);
  }
  public static SquareRecord createDownside(float y, float minx, float minz, float maxx, float maxz){
    return new SquareRecord(minx, y, minz, maxx, y, maxz, Hvector.DOWN);
  }
  public static SquareRecord createXpside(float x, float miny, float minz, float maxy, float maxz){
    return new SquareRecord(x, miny, minz, x, maxy, maxz, Hvector.XP);
  }
  public static SquareRecord createXnside(float x, float miny, float minz, float maxy, float maxz){
    return new SquareRecord(x, miny, minz, x, maxy, maxz, Hvector.XN);
  }
  public static SquareRecord createZpside(float z, float minx, float miny, float maxx, float maxy){
    return new SquareRecord(minx, miny, z, maxx, miny, z, Hvector.ZP);
  }
  public static SquareRecord createZnside(float z, float minx, float miny, float maxx, float maxy){
    return new SquareRecord(minx, miny, z, maxx, maxy, z, Hvector.ZN);
  }

  public enum Hvector{
    UP,
    DOWN,
    XP,
    XN,
    ZP,
    ZN
  }
}
