package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.block.entity.FiltrationDeviceBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FiltrationDeviceRenderer implements BlockEntityRenderer<FiltrationDeviceBlockEntity>{
  public FiltrationDeviceRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull FiltrationDeviceBlockEntity be, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidTank in = be.getInputTank();
    FluidTank out = be.getOutputTank();
    if(in != null && !in.isEmpty()){
      FluidStack fluid = in.getFluid();
      int amount = in.getFluidAmount();
      int tankmax = in.getCapacity();
      Level level = be.getLevel();
      BlockPos pos = be.getBlockPos();

      final float cube_min = 0.125f;
      final float cube_max = 0.875f;
      final float offset = 0.01f;
      final float min = cube_min+offset;
      final float max = cube_max-offset;
      final float y_min = 0.625f + offset;
      final float y_max = 0.625f + 0.3125f * amount / tankmax;

      poseStack.pushPose();
      FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, y_min, min, max, y_max, max, level, pos, 15728880);
      poseStack.popPose();
    }
    if(out != null && !out.isEmpty()){
      FluidStack fluid = out.getFluid();
      int amount = out.getFluidAmount();
      int tankmax = out.getCapacity();
      Level level = be.getLevel();
      BlockPos pos = be.getBlockPos();

      final float cube_min = 0.125f;
      final float cube_max = 0.875f;
      final float offset = 0.001f;
      final float min = cube_min+offset;
      final float max = cube_max-offset;
      final float y_min = 0.0625f + offset;
      final float y_max = 0.0625f + 0.3125f * amount / tankmax;

      poseStack.pushPose();
      FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, y_min, min, max, y_max, max, level, pos, 15728880);
      poseStack.popPose();
    }
  }
}
