package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.block.Frier;
import kandango.reagenica.block.entity.electrical.FrierBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class FrierFluidRenderer implements BlockEntityRenderer<FrierBlockEntity>{
  public FrierFluidRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull FrierBlockEntity frier, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidStack fluid = frier.getOilTank().getFluid();
    if(fluid == null)return;
    if(fluid.isEmpty())return;
    Level level = frier.getLevel();
    BlockPos pos = frier.getBlockPos();
    BlockState state = frier.getBlockState();
    Direction dir;
    if(state.hasProperty(Frier.FACING)){
      dir=state.getValue(Frier.FACING);
    }else{
      return;
    }

    final float cube_pix = 0.0625f;
    final float offset = 0.001f;
    final float height = ((float)fluid.getAmount() / (float)frier.getOilTank().getCapacity()) * 5 * cube_pix + 5*cube_pix + offset;
    
    final SpaceFloat start = rotate(new SpaceFloat(cube_pix*1+offset, cube_pix*5+offset, cube_pix*2+offset),torot(dir));
    final SpaceFloat end = rotate(new SpaceFloat(cube_pix*15-offset, height, cube_pix*12-offset),torot(dir));
    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, start.x,start.y,start.z,end.x,end.y,end.z, level, pos, 15728880);
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
