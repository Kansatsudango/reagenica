package kandango.reagenica.client.renderer;

import javax.annotation.Nonnull;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import kandango.reagenica.block.StackLamp;
import kandango.reagenica.block.entity.StackLampBlockEntity;
import kandango.reagenica.block.entity.lamp.LampState;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StackLampRenderer implements BlockEntityRenderer<StackLampBlockEntity>{
  public StackLampRenderer(BlockEntityRendererProvider.Context ctx) {}

  public void render(@Nonnull StackLampBlockEntity be, float partialTicks, @Nonnull PoseStack poseStack,
                       @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    Level level = be.getLevel();
    if(level==null)return;
    BlockState blockstate = level.getBlockState(be.getBlockPos());
    Direction facing;
    if(blockstate.hasProperty(StackLamp.FACING)){
      facing=blockstate.getValue(StackLamp.FACING);
    }else{
      return;
    }
    final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/block/stack_lamp_light.png");
    VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
    poseStack.pushPose();
    for(int stack=0;stack<=2;stack++){
      LampState lampstate = be.getLampStates().getLampState(stack);
      drawCube(poseStack, consumer, getSpaces(facing, stack), getColor(stack, lampstate, level), facing);
    }
    poseStack.popPose();
  }
  private void drawCube(PoseStack poseStack, VertexConsumer consumer, CubicFloat cube,
                          ColorFloats col, Direction facing) {

        Matrix4f mat = poseStack.last().pose();
        int fullBright = LightTexture.FULL_BRIGHT;
        
        final float offset=0.02f;
        float x1 = cube.x1()/16.0f, y1 = cube.y1()/16.0f, z1 = cube.z1()/16.0f;
        float x2 = cube.x2()/16.0f, y2 = cube.y2()/16.0f, z2 = cube.z2()/16.0f;
        if(facing!=Direction.EAST && facing!=Direction.WEST){
          x1+=offset;
          x2-=offset;
        }
        if(facing!=Direction.UP && facing!=Direction.DOWN){
          y1+=offset;
          y2-=offset;
        }
        if(facing!=Direction.NORTH && facing!=Direction.SOUTH){
          z1+=offset;
          z2-=offset;
        }
        float r=col.r();
        float g=col.g();
        float b=col.b();
        float a=col.a();

        // 上面
        consumer.vertex(mat, x1, y2, z1).color(r,g,b,a).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,1,0).endVertex();
        consumer.vertex(mat, x2, y2, z1).color(r,g,b,a).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,1,0).endVertex();
        consumer.vertex(mat, x2, y2, z2).color(r,g,b,a).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,1,0).endVertex();
        consumer.vertex(mat, x1, y2, z2).color(r,g,b,a).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,1,0).endVertex();

        // 底面
        consumer.vertex(mat, x1, y1, z2).color(r,g,b,a).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,-1,0).endVertex();
        consumer.vertex(mat, x2, y1, z2).color(r,g,b,a).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,-1,0).endVertex();
        consumer.vertex(mat, x2, y1, z1).color(r,g,b,a).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,-1,0).endVertex();
        consumer.vertex(mat, x1, y1, z1).color(r,g,b,a).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,-1,0).endVertex();

        // 北（手前）
        consumer.vertex(mat, x1, y1, z1).color(r,g,b,a).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,-1).endVertex();
        consumer.vertex(mat, x2, y1, z1).color(r,g,b,a).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,-1).endVertex();
        consumer.vertex(mat, x2, y2, z1).color(r,g,b,a).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,-1).endVertex();
        consumer.vertex(mat, x1, y2, z1).color(r,g,b,a).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,-1).endVertex();

        // 南（奥）
        consumer.vertex(mat, x2, y1, z2).color(r,g,b,a).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,1).endVertex();
        consumer.vertex(mat, x1, y1, z2).color(r,g,b,a).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,1).endVertex();
        consumer.vertex(mat, x1, y2, z2).color(r,g,b,a).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,1).endVertex();
        consumer.vertex(mat, x2, y2, z2).color(r,g,b,a).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(0,0,1).endVertex();

        // 西（左）
        consumer.vertex(mat, x1, y1, z2).color(r,g,b,a).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(-1,0,0).endVertex();
        consumer.vertex(mat, x1, y1, z1).color(r,g,b,a).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(-1,0,0).endVertex();
        consumer.vertex(mat, x1, y2, z1).color(r,g,b,a).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(-1,0,0).endVertex();
        consumer.vertex(mat, x1, y2, z2).color(r,g,b,a).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(-1,0,0).endVertex();

        // 東（右）
        consumer.vertex(mat, x2, y1, z1).color(r,g,b,a).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(1,0,0).endVertex();
        consumer.vertex(mat, x2, y1, z2).color(r,g,b,a).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(1,0,0).endVertex();
        consumer.vertex(mat, x2, y2, z2).color(r,g,b,a).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(1,0,0).endVertex();
        consumer.vertex(mat, x2, y2, z1).color(r,g,b,a).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fullBright).normal(1,0,0).endVertex();
    }

  private CubicFloat getSpaces(Direction facing, int stack){
    if(facing==Direction.UP)return new CubicFloat(6.0f, 3.0f+stack*4.0f, 6.0f, 10.0f, 6.0f+stack*4.0f, 10.0f);
    else if(facing==Direction.DOWN)return new CubicFloat(6.0f, 10.0f-stack*4.0f, 6.0f, 10.0f, 13.0f-stack*4.0f, 10.0f);
    else if(facing==Direction.NORTH)return new CubicFloat(6.0f, 6.0f, 10.0f-stack*4.0f, 10.0f, 10.0f, 13.0f-stack*4.0f);
    else if(facing==Direction.SOUTH)return new CubicFloat(6.0f, 6.0f, 3.0f+stack*4.0f, 10.0f, 10.0f, 6.0f+stack*4.0f);
    else if(facing==Direction.WEST)return new CubicFloat(10.0f-stack*4.0f, 6.0f, 6.0f, 13.0f-stack*4.0f, 10.0f, 10.0f);
    else if(facing==Direction.EAST)return new CubicFloat(3.0f+stack*4.0f, 6.0f, 6.0f, 6.0f+stack*4.0f, 10.0f, 10.0f);
    else throw new IllegalArgumentException();
  }
  private ColorFloats getColor(int stack, LampState state,Level lv){
    if(stack==0){
      if(state==LampState.ON)return ColorFloats.getColor(0x00ff7f);
      else if(state==LampState.OFF)return ColorFloats.getColor(0x113a00);
      else return litOnBlink(lv) ? ColorFloats.getColor(0x00ff7f) : ColorFloats.getColor(0x113a00);
    }else if(stack==1){
      if(state==LampState.ON)return ColorFloats.getColor(0xffc700);
      else if(state==LampState.OFF)return ColorFloats.getColor(0x422f00);
      else return litOnBlink(lv) ? ColorFloats.getColor(0xffc700) : ColorFloats.getColor(0x422f00);
    }else if(stack==2){
      if(state==LampState.ON)return ColorFloats.getColor(0xff0000);
      else if(state==LampState.OFF)return ColorFloats.getColor(0x3a0000);
      else return litOnBlink(lv) ? ColorFloats.getColor(0xff0000) : ColorFloats.getColor(0x3a0000);
    }
    return null;
  }
  private boolean litOnBlink(Level lv){
    long time=lv.getGameTime();
    return (time/10)%2==0;
  }
  private record CubicFloat(float x1,float y1,float z1,float x2,float y2,float z2) {
  }
  private record ColorFloats(float r,float g,float b,float a) {
    static ColorFloats getColor(int color){
      return new ColorFloats(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1.0f);
    }
  }
}
