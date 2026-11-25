package kandango.reagenica.item.reagent;

public record ReagentProperties (
  String formula,
  int color,
  int toxic,//0:non-toxic 1:toxic when drink 2:requires rubber grove 3:requires full armor 4:requires full scientist armor
  int flamable,//0:non-flammable 4:flameshower when turn on the burner
  int unstable//4:dropping has chance to explode
){}
