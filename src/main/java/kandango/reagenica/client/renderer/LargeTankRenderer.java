package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.block.entity.LargeTankCoreBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class LargeTankRenderer implements BlockEntityRenderer<LargeTankCoreBlockEntity>{
  public LargeTankRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull LargeTankCoreBlockEntity be, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidTank tank = be.getFluidTank();
    if(tank == null || tank.isEmpty())return;
    FluidStack fluid = tank.getFluid();
    int amount = tank.getFluidAmount();
    int tankmax = tank.getCapacity();
    Level level = be.getLevel();
    BlockPos pos = be.getBlockPos();
    LargeTankCoreBlockEntity.TankSize size = be.getTankSize();
    if(size==null)return;
    int tankwidth = size.width();
    int tankheight = size.height();

    final float cube_min = -(tankwidth-1);
    final float cube_max = 1.0f+(tankwidth-1);
    final float offset = 0.01f;
    final float min = cube_min+offset;
    final float max = cube_max-offset;
    final float height = Math.min(1.0f + (tankheight-1)*(Math.min((float)amount/tankmax,1.0f)),tankheight-offset);

    poseStack.pushPose();
    FluidBlockEntityRenderer.renderFluidCube(poseStack, bufferSource, fluid, min, 1.01f, min, max, height, max, level, pos, 15728880);
    poseStack.popPose();
  }
  @Override
  public boolean shouldRenderOffScreen(@Nonnull LargeTankCoreBlockEntity be) {
    return true;
  }
}
