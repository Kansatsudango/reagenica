package kandango.reagenica.item;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemiBlocks;
import kandango.reagenica.block.PaleoPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags.Blocks;

public class GeologistRod extends Item{
  public GeologistRod(){
    super(new Item.Properties().rarity(Rarity.RARE).stacksTo(1));
  }

  @Override
  public InteractionResult useOn(@Nonnull UseOnContext context){
    Level level = context.getLevel();
    if(level instanceof ServerLevel serverLevel){
      Player player=context.getPlayer();
      if(player==null)return InteractionResult.FAIL;
      BlockPos pos=context.getClickedPos();
      Direction clickSide = context.getClickedFace();
      BlockPos origin = pos.relative(clickSide);
      if(hasXaxPortalFrame(serverLevel, origin.offset(-1, -1, 0))){
        fillXPortal(serverLevel, origin.offset(-1, -1, 0));
      }else if(hasXaxPortalFrame(serverLevel, origin.offset(-2, -1, 0))){
        fillXPortal(serverLevel, origin.offset(-2, -1, 0));
      }else if(hasZaxPortalFrame(serverLevel, origin.offset(0,-1,-1))){
        fillZPortal(serverLevel, origin.offset(0, -1, -1));
      }else if(hasZaxPortalFrame(serverLevel, origin.offset(0,-1,-2))){
        fillZPortal(serverLevel, origin.offset(0, -1, -2));
      }else{
        for(int i=0;i<12;i++){
          double x = origin.getX() + serverLevel.getRandom().nextDouble();
          double y = origin.getY() + serverLevel.getRandom().nextDouble();
          double z = origin.getZ() + serverLevel.getRandom().nextDouble();
          serverLevel.addParticle(ParticleTypes.ENCHANT, x,y,z, 0.0D, 0.01D, 0.0D);
        }
      }
    }
    return InteractionResult.SUCCESS;
  }
  private boolean hasXaxPortalFrame(ServerLevel lv, BlockPos pos){
    for(int x=0;x<4;x++){
      for(int y=0;y<5;y++){
        if(y==0 || y==4){
          if(1<=x && x<=2){
            if(!isFrame(lv.getBlockState(pos.offset(x, y, 0)))){
              return false;
            }
          }
        }else{
          if(x==0 || x==3){
            if(!isFrame(lv.getBlockState(pos.offset(x, y, 0)))){
              return false;
            }
          }else{
            if(!lv.getBlockState(pos.offset(x, y, 0)).isAir()){
              return false;
            }
          }
        }
      }
    }
    return true;
  }
  private boolean hasZaxPortalFrame(ServerLevel lv, BlockPos pos){
    for(int z=0;z<4;z++){
      for(int y=0;y<5;y++){
        if(y==0 || y==4){
          if(1<=z && z<=2){
            if(!isFrame(lv.getBlockState(pos.offset(0, y, z)))){
              return false;
            }
          }
        }else{
          if(z==0 || z==3){
            if(!isFrame(lv.getBlockState(pos.offset(0, y, z)))){
              return false;
            }
          }else{
            if(!lv.getBlockState(pos.offset(0, y, z)).isAir()){
              return false;
            }
          }
        }
      }
    }
    return true;
  }
  private void fillXPortal(ServerLevel lv,BlockPos portalorigin){
    BlockState state = ChemiBlocks.PALEO_PORTAL.get().defaultBlockState().setValue(PaleoPortalBlock.AXIS, Direction.Axis.X);
    for(int x=1;x<=2;x++){
      for(int y=1;y<=3;y++){
        lv.setBlock(portalorigin.offset(x, y, 0), state, 18);
      }
    }
  }
  private void fillZPortal(ServerLevel lv,BlockPos portalorigin){
    BlockState state = ChemiBlocks.PALEO_PORTAL.get().defaultBlockState().setValue(PaleoPortalBlock.AXIS, Direction.Axis.Z);
    for(int z=1;z<=2;z++){
      for(int y=1;y<=3;y++){
        lv.setBlock(portalorigin.offset(0, y, z), state, 18);
      }
    }
  }
  private boolean isFrame(BlockState state){
    return state.is(Blocks.OBSIDIAN);
  }
}
