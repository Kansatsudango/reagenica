package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import kandango.reagenica.block.entity.electrical.PipeAbstractBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FluidPipeRenderer implements BlockEntityRenderer<PipeAbstractBlockEntity>{
  public FluidPipeRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull PipeAbstractBlockEntity pipe, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidStack fluid = pipe.getFluid();
    if(fluid == null || fluid.isEmpty())return;
    Level level = pipe.getLevel();
    BlockPos pos = pipe.getBlockPos();

    final float cube_min = 0.3125f;
    final float cube_max = 0.6875f;
    final float offset = 0.01f;
    final float min = cube_min+offset;
    final float max = cube_max-offset;

    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, min, min, max, max, max, level, pos, 15728880);
    poseStack.popPose();
  }
}
