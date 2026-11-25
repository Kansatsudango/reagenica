package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import kandango.reagenica.block.TradingStall;
import kandango.reagenica.block.entity.TradingStallBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TradingStallRenderer implements BlockEntityRenderer<TradingStallBlockEntity>{
  private final ItemRenderer itemRenderer;

  public TradingStallRenderer(BlockEntityRendererProvider.Context context){
    this.itemRenderer = Minecraft.getInstance().getItemRenderer();
  }

  @Override
  public void render(@Nonnull TradingStallBlockEntity blockentity, float partialticks, @Nonnull PoseStack pose, 
                      @Nonnull MultiBufferSource mbs, int combinedLight, int combinedOverlay) {
    final ItemStackHandler handler = blockentity.getItemHandler();
    Direction facing = blockentity.getBlockState().getValue(TradingStall.FACING);
    float yRotation = -facing.getCounterClockWise().toYRot();
    for(int i=0;i<3;i++){
      for(int j=0;j<9;j++){
        ItemStack stack = handler.getStackInSlot(i*9+j);
        if(!stack.isEmpty()){
          pose.pushPose();
          SpaceFloat floats = new SpaceFloat((float)i/5.0f+0.3f, 0.1f, (float)j/10.0f+0.1f);
          SpaceFloat rotfloats = rotate(floats, torot(facing));
          pose.translate(rotfloats.x(),rotfloats.y(),rotfloats.z());
          pose.mulPose(Axis.YP.rotationDegrees(yRotation));
          pose.mulPose(Axis.YP.rotationDegrees(80));
          pose.mulPose(Axis.XP.rotationDegrees(15));
          pose.mulPose(Axis.ZP.rotationDegrees(15));
          pose.scale(0.3f, 0.3f, 0.3f);

          itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, pose, mbs, blockentity.getLevel(), 0);
          pose.popPose();
        }
      }
    }
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
    if(dir == Direction.WEST)return 0;
    else if(dir == Direction.NORTH)return 1;
    else if(dir == Direction.EAST)return 2;
    else return 3;
  }
  private record SpaceFloat(float x,float y,float z) {
  }

}
