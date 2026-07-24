package kandango.reagenica.item.bioreagent;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum BioReagentTypes implements StringRepresentable{
  CRUDE("crude"),
  YEAST("yeast"),
  ORYZAE("oryzae"),
  ACETOBACTER("acetobacter"),
  CONTAMINATED("contaminated"),
  NONE("none"),
  UNKNOWN("unknown");

  private final String name;

  private BioReagentTypes(String name){
    this.name=name;
  }

  @Override
  public String getSerializedName() {
    return name;
  }

  public static final Codec<BioReagentTypes> CODEC = StringRepresentable.fromEnum(BioReagentTypes::values);
  
}
