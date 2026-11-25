package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.block.entity.SimpleTankBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class SmallTankRenderer implements BlockEntityRenderer<SimpleTankBlockEntity>{
  public SmallTankRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull SimpleTankBlockEntity be, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidTank tank = be.getFluidTank();
    if(tank == null || tank.isEmpty())return;
    FluidStack fluid = tank.getFluid();
    int amount = tank.getFluidAmount();
    int tankmax = tank.getCapacity();
    Level level = be.getLevel();
    BlockPos pos = be.getBlockPos();

    final float cube_min = 0.0625f;
    final float cube_max = 0.9375f;
    final float offset = 0.01f;
    final float min = cube_min+offset;
    final float max = cube_max-offset;
    final float height = cube_min + offset + 0.875f*amount/tankmax;

    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, min, min, max, height, max, level, pos, 15728880);
    poseStack.popPose();
  }
}
