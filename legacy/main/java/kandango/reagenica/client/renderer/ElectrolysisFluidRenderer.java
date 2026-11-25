package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.block.entity.electrical.ElectroLysisBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class ElectrolysisFluidRenderer implements BlockEntityRenderer<ElectroLysisBlockEntity>{
  public ElectrolysisFluidRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull ElectroLysisBlockEntity eld, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidStack fluid = eld.getInputTank().getFluid();
    if(fluid == null || fluid.isEmpty())return;
//    FluidState fluidstate = fluid.getFluid().defaultFluidState();
    Level level = eld.getLevel();
    BlockPos pos = eld.getBlockPos();

    final float cube_min = 0.125f;
    final float cube_max = 0.875f;
    final float offset = 0.01f;
    final float min = cube_min+offset;
    final float max = cube_max-offset;
    final float height = ((float)fluid.getAmount() / (float)eld.getInputTank().getCapacity()) * 0.75f + 0.123f + offset;

    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, min, min, max, height, max, level, pos, 15728880);
    poseStack.popPose();
  }
}
