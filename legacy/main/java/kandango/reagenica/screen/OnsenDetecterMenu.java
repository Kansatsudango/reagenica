package kandango.reagenica.screen;

import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.block.fluid.hotspring.OnsenTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Climate;

public class OnsenDetecterMenu extends AbstractContainerMenu{
  private static final float ENCODE_SCALE = 1000f;
  private final Level level;
  private final BlockPos pos;
  private final ContainerData datas;

  public OnsenDetecterMenu(int id, Level level, BlockPos pos){
    super(ModMenus.ONSEN_DETECTER_MENU.get(), id);
    this.level = level;
    this.pos = pos;
    this.datas = new SimpleContainerData(6);
    this.addDataSlots(datas);
    if(level instanceof ServerLevel slv){
      Climate.TargetPoint tp = OnsenTypes.getGeology(slv, this.pos);
      datas.set(0, encode(tp.continentalness()));
      datas.set(1, encode(tp.temperature()));
      datas.set(2, encode(tp.humidity()));
      datas.set(3, encode(tp.erosion()));
      datas.set(4, encode(tp.weirdness()));
      datas.set(5, OnsenTypes.getOnsenTypeFrom(tp).getOrdinal());
    }
  }
  private int encode(long raw){
    return (int)(Climate.unquantizeCoord(raw)*ENCODE_SCALE);
  }
  public float getContinentalness(){
    return (float)datas.get(0)/ENCODE_SCALE;
  }
  public float getTemperature(){
    return (float)datas.get(1)/ENCODE_SCALE;
  }
  public float getHumidity(){
    return (float)datas.get(2)/ENCODE_SCALE;
  }
  public float getErosion(){
    return (float)datas.get(3)/ENCODE_SCALE;
  }
  public float getWeirdness(){
    return (float)datas.get(4)/ENCODE_SCALE;
  }
  public OnsenTypes getOnsenType(){
    return OnsenTypes.getOnsenTypeOf(datas.get(5));
  }

  public Optional<Level> getLevel(){
    return Optional.ofNullable(level);
  }
  public BlockPos getPos(){
    return pos;
  }

  @Override
  public ItemStack quickMoveStack(@Nonnull Player player, int slot) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(@Nonnull Player player) {
    return true;
  }
}
