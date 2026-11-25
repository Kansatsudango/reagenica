package kandango.reagenica.client.renderer;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import kandango.reagenica.ChemiGeometry;
import kandango.reagenica.ChemiGeometry.Space;
import kandango.reagenica.block.BlockUtil;
import kandango.reagenica.block.OnsenFiller;
import kandango.reagenica.block.entity.OnsenFillerBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class OnsenFillerFluidRenderer implements BlockEntityRenderer<OnsenFillerBlockEntity>{
  public OnsenFillerFluidRenderer(BlockEntityRendererProvider.Context context){
  }

  @Override
  public void render(@Nonnull OnsenFillerBlockEntity filler, float partialTick, @Nonnull PoseStack poseStack, 
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    FluidStack fluid = new FluidStack(filler.getRenderingFluid(), packedOverlay);
    if(fluid.isEmpty())return;
    Level level = filler.getLevel();
    BlockPos pos = filler.getBlockPos();

    BlockState state = filler.getBlockState();
    Direction rotation = BlockUtil.getStatus(state, OnsenFiller.FACING).orElse(Direction.NORTH);
    Block block = state.getBlock();
    poseStack.pushPose();
    if(block instanceof OnsenFiller fillerBlock){
      Collection<ChemiGeometry> waters = fillerBlock.waterFlows();
      for(ChemiGeometry cube : waters){
        ChemiGeometry place = cube.rotate(rotation).flush();
        Space start = place.getLeastPole();
        Space end = place.getMostPole();
        FluidBlockEntityRenderer.renderFlowingFluidCube(poseStack, bufferSource, fluid, start.fx(), start.fy(), start.fz(), end.fx(), end.fy(), end.fz(), level, pos, packedLight);
      }
    }
    poseStack.popPose();
  }
}
