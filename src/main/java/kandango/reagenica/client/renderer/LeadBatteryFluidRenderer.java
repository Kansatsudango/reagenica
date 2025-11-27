package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.ChemiFluids;
import kandango.reagenica.block.entity.electrical.LeadBatteryBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class LeadBatteryFluidRenderer implements BlockEntityRenderer<LeadBatteryBlockEntity>{
  public LeadBatteryFluidRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull LeadBatteryBlockEntity batt, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidStack fluid = new FluidStack(ChemiFluids.SULFURIC_ACID.getFluid(), packedOverlay);
    Level level = batt.getLevel();
    BlockPos pos = batt.getBlockPos();

    final float cube_min = 0.0625f;
    final float cube_max = 0.9375f;
    final float offset = 0.01f;
    final float min = cube_min+offset;
    final float max = cube_max-offset;
    final float height = 0.75f;

    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, min, min, max, height, max, level, pos, packedLight);
    poseStack.popPose();
  }
}
