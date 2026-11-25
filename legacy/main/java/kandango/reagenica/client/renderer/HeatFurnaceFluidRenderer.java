package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.block.HeatFurnace;
import kandango.reagenica.block.entity.electrical.HeatFurnaceBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class HeatFurnaceFluidRenderer implements BlockEntityRenderer<HeatFurnaceBlockEntity>{
  public HeatFurnaceFluidRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull HeatFurnaceBlockEntity furnace, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidStack fluidin = furnace.getInputTank().getFluid();
    FluidStack fluidout = furnace.getOutputTank().getFluid();
    if(fluidin == null || fluidout == null)return;
    if(fluidin.isEmpty() && fluidout.isEmpty())return;
    Level level = furnace.getLevel();
    BlockPos pos = furnace.getBlockPos();
    BlockState state = furnace.getBlockState();
    Direction dir;
    if(state.hasProperty(HeatFurnace.FACING)){
      dir=state.getValue(HeatFurnace.FACING);
    }else{
      return;
    }

    final float cube_pix = 0.0625f;
    final float offset = 0.001f;
    final float heightin = ((float)fluidin.getAmount() / (float)furnace.getInputTank().getCapacity()) * 0.9f + cube_pix + offset;
    final float heightout = ((float)fluidout.getAmount() / (float)furnace.getOutputTank().getCapacity()) * 0.9f + cube_pix + offset;
    
    final SpaceFloat inputstart = rotate(new SpaceFloat(cube_pix*11+offset, cube_pix+offset, cube_pix*7+offset),torot(dir));
    final SpaceFloat inputend = rotate(new SpaceFloat(cube_pix*15-offset, heightin, cube_pix*15-offset),torot(dir));
    final SpaceFloat outputstart = rotate(new SpaceFloat(cube_pix+offset, cube_pix+offset, cube_pix*7+offset),torot(dir));
    final SpaceFloat outputend = rotate(new SpaceFloat(cube_pix*5-offset, heightout, cube_pix*15-offset),torot(dir));
    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluidin, inputstart.x,inputstart.y,inputstart.z,inputend.x,inputend.y,inputend.z, level, pos, 15728880);
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluidout, outputstart.x,outputstart.y,outputstart.z,outputend.x,outputend.y,outputend.z, level, pos, 15728880);
    poseStack.popPose();
  }
  private SpaceFloat rotate(SpaceFloat coord, int rot){
    float x=coord.x();
    float y=coord.y();
    float z=coord.z();
    if(rot==0) return new SpaceFloat(x, y, z);
    else if(rot==1) return new SpaceFloat(z, y, 1f-x);
    else if(rot==2) return new SpaceFloat(1f-x, y, 1f-z);
    else if(rot==3) return new SpaceFloat(1f-z, y, x);
    else throw new IllegalArgumentException("Only 0-3 is allowed in rot");
  }
  private int torot(Direction dir){
    if(dir == Direction.NORTH)return 0;
    else if(dir == Direction.WEST)return 1;
    else if(dir == Direction.SOUTH)return 2;
    else return 3;
  }
  private record SpaceFloat(float x,float y,float z) {
  }
}
