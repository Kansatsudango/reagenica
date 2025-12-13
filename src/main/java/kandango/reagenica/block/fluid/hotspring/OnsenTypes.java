package kandango.reagenica.block.fluid.hotspring;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.material.Fluid;

public class OnsenTypes {
  public static final OnsenTypes SIMPLE = new OnsenTypes("simple", ChemiFluids.SIMPLE_HOTSPRING::getFluid,0);
  public static final OnsenTypes SULFUR = new OnsenTypes("sulfur", ChemiFluids.SULFUR_HOTSPRING::getFluid,1);
  public static final OnsenTypes CHLORIDE = new OnsenTypes("chloride", ChemiFluids.CHLORIDE_HOTSPRING::getFluid,2);
  public static final OnsenTypes IRON = new OnsenTypes("iron", ChemiFluids.IRON_HOTSPRING::getFluid,3);
  public static final OnsenTypes RADIOACTIVE = new OnsenTypes("radioactive", ChemiFluids.RADIOACTIVE_HOTSPRING::getFluid,4);
  private final String nameKey;
  private final Supplier<Fluid> fluidObject;
  private final int ordinal;
  private OnsenTypes(String name, Supplier<Fluid> fluid, int ordinal){
    this.nameKey = name;
    this.fluidObject = fluid;
    this.ordinal = ordinal;
  }
  public Component getNameComponent(){
    return Component.translatable(fluidObject.get().getFluidType().getDescriptionId());
  }
  public Component getLoreComponent(){
    return Component.translatable("gui.reagenica.hotspring_lore_"+nameKey);
  }
  public Fluid getFluid(){
    return fluidObject.get();
  }
  public int getOrdinal(){
    return ordinal;
  }

  public static OnsenTypes getOnsenTypeAt(@Nonnull ServerLevel lv, BlockPos pos){
    return getOnsenTypeFrom(getGeology(lv, pos));
  }
  public static Climate.TargetPoint getGeology(@Nonnull ServerLevel lv, BlockPos pos){
    RandomState randomState = lv.getChunkSource().randomState();
    Climate.Sampler sampler = randomState.sampler();
    int qx = QuartPos.fromBlock(pos.getX());
    int qy = QuartPos.fromBlock(pos.getY());
    int qz = QuartPos.fromBlock(pos.getZ());
    Climate.TargetPoint tp = sampler.sample(qx, qy, qz);
    return tp;
  }
  public static OnsenTypes getOnsenTypeFrom(Climate.TargetPoint tp){
    float continentalness = Climate.unquantizeCoord(tp.continentalness());
    float temperature = Climate.unquantizeCoord(tp.temperature());
    float humidity = Climate.unquantizeCoord(tp.humidity());
    float erosion = Climate.unquantizeCoord(tp.erosion());
    float weirdness = Climate.unquantizeCoord(tp.weirdness());

    if(continentalness < 0.0f && temperature < 0.65f){
      return OnsenTypes.CHLORIDE;
    }else if(erosion < -0.2 && weirdness>0){
      return OnsenTypes.SULFUR;
    }else if(continentalness>0.3 && erosion<-0.5 && weirdness<0){
      return OnsenTypes.RADIOACTIVE;
    }else if(continentalness>0.3 && humidity<0.3){
      return OnsenTypes.IRON;
    }else{
      return OnsenTypes.SIMPLE;
    }
  }
  public static OnsenTypes getOnsenTypeOf(int ordinal){
    return switch(ordinal){
      case 0 -> OnsenTypes.SIMPLE;
      case 1 -> OnsenTypes.SULFUR;
      case 2 -> OnsenTypes.CHLORIDE;
      case 3 -> OnsenTypes.IRON;
      case 4 -> OnsenTypes.RADIOACTIVE;
      default -> throw new IndexOutOfBoundsException();
    };
  }
}
