package kandango.reagenica.client.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class FluidBlockEntityRenderer {
  public static void renderFluidCube(PoseStack poseStack, MultiBufferSource buffersource,
                                   FluidStack fluidStack, float minX, float minY, float minZ,
                                   float maxX, float maxY, float maxZ,
                                   BlockAndTintGetter level, BlockPos pos, int light) {

    if (fluidStack.isEmpty()) return;
    Fluid fluid = fluidStack.getFluid();
    FluidState fluidState = fluid.defaultFluidState();

    // テクスチャ取得
    TextureAtlasSprite[] sprites = ForgeHooksClient.getFluidSprites(level, pos, fluidState);
    TextureAtlasSprite sprite = sprites[0];
    VertexConsumer buffer = buffersource.getBuffer(RenderType.entityTranslucent(sprite.atlasLocation()));

    // 色取得
    int color = Minecraft.getInstance().getBlockColors().getColor(fluidState.createLegacyBlock(), level, pos, 0);
    if (color == -1) {
        color = IClientFluidTypeExtensions.of(fluid).getTintColor(fluidStack);
    }

    // アルファが0の場合は255をセット
    if (((color >> 24) & 0xFF) == 0) {
        color |= 0xFF000000;
    }
    float a = ((color >> 24) & 0xFF) / 255f;
    float r = ((color >> 16) & 0xFF) / 255f;
    float g = ((color >> 8) & 0xFF) / 255f;
    float b = (color & 0xFF) / 255f;

    PoseStack.Pose pose = poseStack.last();
    Matrix4f matrix = pose.pose();
    Matrix3f normal = pose.normal();

    // 6面に頂点を送る（各面でUV座標は同じ、Z順序も同じでよい）
    // ↓簡略化のため、テクスチャのUVは0-1の範囲固定（液面のアニメを無視）
    float u1 = sprite.getU0();
    float u2 = sprite.getU1();
    float v1 = sprite.getV0();
    float v2 = sprite.getV1();

    RenderingHappySet renderingkit = new RenderingHappySet(buffer, matrix, r, g, b, a, u1, v1, u2, v2, light, normal);

    renderToUpper(renderingkit, minX, minZ, maxX, maxZ, maxY);
    renderToLower(renderingkit, minX, minZ, maxX, maxZ, minY);
    renderToNorthern(renderingkit, minX, minY, maxX, maxY, minZ);
    renderToSouthern(renderingkit, minX, minY, maxX, maxY, maxZ);
    renderToWestern(renderingkit, minZ, minY, maxZ, maxY, minX);
    renderToWestern(renderingkit, minZ, minY, maxZ, maxY, maxX);
  }
  public static void renderToUpper(RenderingHappySet kit, float minX, float minZ, float maxX, float maxZ, float Y){
    final VertexConsumer buffer = kit.buffer;
    final Matrix4f matrix = kit.matrix;
    final float r = kit.r;
    final float g = kit.g;
    final float b = kit.b;
    final float a = kit.a;
    final float u1 = kit.u1;
    final float v1 = kit.v1;
    final float u2 = kit.u2;
    final float v2 = kit.v2;
    final int light = kit.light;
    final Matrix3f normal = kit.normal;
    float ptr_X = minX;
    while(ptr_X<maxX){
      float ptr_Z = minZ;
      while (ptr_Z<maxZ) {
        final float ptr_mX = Math.min(ptr_X+1.0f, maxX);
        final float ptr_mZ = Math.min(ptr_Z+1.0f, maxZ);
        final float xu2 = u1+(ptr_mX-ptr_X)*(u2-u1);
        final float xv2 = v1+(ptr_mZ-ptr_Z)*(v2-v1);
        buffer.vertex(matrix, ptr_X , Y, ptr_mZ).color(r, g, b, a).uv(u1 , v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
        buffer.vertex(matrix, ptr_mX, Y, ptr_mZ).color(r, g, b, a).uv(xu2, v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
        buffer.vertex(matrix, ptr_mX, Y, ptr_Z ).color(r, g, b, a).uv(xu2, xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
        buffer.vertex(matrix, ptr_X , Y, ptr_Z ).color(r, g, b, a).uv(u1 , xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 1, 0).endVertex();
        ptr_Z+=1.0f;
      }
      ptr_X+=1.0f;
    }
  }
  public static void renderToLower(RenderingHappySet kit, float minX, float minZ, float maxX, float maxZ, float Y){
    final VertexConsumer buffer = kit.buffer;
    final Matrix4f matrix = kit.matrix;
    final float r = kit.r;
    final float g = kit.g;
    final float b = kit.b;
    final float a = kit.a;
    final float u1 = kit.u1;
    final float v1 = kit.v1;
    final float u2 = kit.u2;
    final float v2 = kit.v2;
    final int light = kit.light;
    final Matrix3f normal = kit.normal;
    float ptr_X = minX;
    while(ptr_X<maxX){
      float ptr_Z = minZ;
      while (ptr_Z<maxZ) {
        final float ptr_mX = Math.min(ptr_X+1.0f, maxX);
        final float ptr_mZ = Math.min(ptr_Z+1.0f, maxZ);
        final float xu2 = u1+(ptr_mX-ptr_X)*(u2-u1);
        final float xv2 = v1+(ptr_mZ-ptr_Z)*(v2-v1);
        buffer.vertex(matrix, ptr_X , Y, ptr_mZ).color(r, g, b, a).uv(u1 , v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, -1, 0).endVertex();
        buffer.vertex(matrix, ptr_mX, Y, ptr_mZ).color(r, g, b, a).uv(xu2, v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, -1, 0).endVertex();
        buffer.vertex(matrix, ptr_mX, Y, ptr_Z ).color(r, g, b, a).uv(xu2, xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, -1, 0).endVertex();
        buffer.vertex(matrix, ptr_X , Y, ptr_Z ).color(r, g, b, a).uv(u1 , xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, -1, 0).endVertex();
        ptr_Z+=1.0f;
      }
      ptr_X+=1.0f;
    }
  }
  public static void renderToNorthern(RenderingHappySet kit, float minX, float minY, float maxX, float maxY, float Z){
    final VertexConsumer buffer = kit.buffer;
    final Matrix4f matrix = kit.matrix;
    final float r = kit.r;
    final float g = kit.g;
    final float b = kit.b;
    final float a = kit.a;
    final float u1 = kit.u1;
    final float v1 = kit.v1;
    final float u2 = kit.u2;
    final float v2 = kit.v2;
    final int light = kit.light;
    final Matrix3f normal = kit.normal;
    float ptr_X = minX;
    while(ptr_X<maxX){
      float ptr_Y = minY;
      while (ptr_Y<maxY) {
        final float ptr_mX = Math.min(ptr_X+1.0f, maxX);
        final float ptr_mY = Math.min(ptr_Y+1.0f, maxY);
        final float xu2 = u1+(ptr_mX-ptr_X)*(u2-u1);
        final float xv2 = v1+(ptr_mY-ptr_Y)*(v2-v1);
        buffer.vertex(matrix, ptr_X , ptr_Y , Z).color(r, g, b, a).uv(u1 , v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        buffer.vertex(matrix, ptr_mX, ptr_Y , Z).color(r, g, b, a).uv(xu2, v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        buffer.vertex(matrix, ptr_mX, ptr_mY, Z).color(r, g, b, a).uv(xu2, xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        buffer.vertex(matrix, ptr_X , ptr_mY, Z).color(r, g, b, a).uv(u1 , xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, -1).endVertex();
        ptr_Y+=1.0f;
      }
      ptr_X+=1.0f;
    }
  }
  public static void renderToSouthern(RenderingHappySet kit, float minX, float minY, float maxX, float maxY, float Z){
    final VertexConsumer buffer = kit.buffer;
    final Matrix4f matrix = kit.matrix;
    final float r = kit.r;
    final float g = kit.g;
    final float b = kit.b;
    final float a = kit.a;
    final float u1 = kit.u1;
    final float v1 = kit.v1;
    final float u2 = kit.u2;
    final float v2 = kit.v2;
    final int light = kit.light;
    final Matrix3f normal = kit.normal;
    float ptr_X = minX;
    while(ptr_X<maxX){
      float ptr_Y = minY;
      while (ptr_Y<maxY) {
        final float ptr_mX = Math.min(ptr_X+1.0f, maxX);
        final float ptr_mY = Math.min(ptr_Y+1.0f, maxY);
        final float xu2 = u1+(ptr_mX-ptr_X)*(u2-u1);
        final float xv2 = v1+(ptr_mY-ptr_Y)*(v2-v1);
        buffer.vertex(matrix, ptr_mX, ptr_Y , Z).color(r, g, b, a).uv(u1 , v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(matrix, ptr_X , ptr_Y , Z).color(r, g, b, a).uv(xu2, v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(matrix, ptr_X , ptr_mY, Z).color(r, g, b, a).uv(xu2, xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(matrix, ptr_mX, ptr_mY, Z).color(r, g, b, a).uv(u1 , xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0, 0, 1).endVertex();
        ptr_Y+=1.0f;
      }
      ptr_X+=1.0f;
    }
  }
  public static void renderToWestern(RenderingHappySet kit, float minZ, float minY, float maxZ, float maxY, float X){
    final VertexConsumer buffer = kit.buffer;
    final Matrix4f matrix = kit.matrix;
    final float r = kit.r;
    final float g = kit.g;
    final float b = kit.b;
    final float a = kit.a;
    final float u1 = kit.u1;
    final float v1 = kit.v1;
    final float u2 = kit.u2;
    final float v2 = kit.v2;
    final int light = kit.light;
    final Matrix3f normal = kit.normal;
    float ptr_Z = minZ;
    while(ptr_Z<maxZ){
      float ptr_Y = minY;
      while (ptr_Y<maxY) {
        final float ptr_mZ = Math.min(ptr_Z+1.0f, maxZ);
        final float ptr_mY = Math.min(ptr_Y+1.0f, maxY);
        final float xu2 = u1+(ptr_mZ-ptr_Z)*(u2-u1);
        final float xv2 = v1+(ptr_mY-ptr_Y)*(v2-v1);
        buffer.vertex(matrix, X, ptr_Y , ptr_mZ).color(r, g, b, a).uv(u1 , v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, -1, 0, 0).endVertex();
        buffer.vertex(matrix, X, ptr_Y , ptr_Z ).color(r, g, b, a).uv(xu2, v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, -1, 0, 0).endVertex();
        buffer.vertex(matrix, X, ptr_mY, ptr_Z ).color(r, g, b, a).uv(xu2, xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, -1, 0, 0).endVertex();
        buffer.vertex(matrix, X, ptr_mY, ptr_mZ).color(r, g, b, a).uv(u1 , xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, -1, 0, 0).endVertex();
        ptr_Y+=1.0f;
      }
      ptr_Z+=1.0f;
    }
  }
  public static void renderToEastern(RenderingHappySet kit, float minZ, float minY, float maxZ, float maxY, float X){
    final VertexConsumer buffer = kit.buffer;
    final Matrix4f matrix = kit.matrix;
    final float r = kit.r;
    final float g = kit.g;
    final float b = kit.b;
    final float a = kit.a;
    final float u1 = kit.u1;
    final float v1 = kit.v1;
    final float u2 = kit.u2;
    final float v2 = kit.v2;
    final int light = kit.light;
    final Matrix3f normal = kit.normal;
    float ptr_Z = minZ;
    while(ptr_Z<maxZ){
      float ptr_Y = minY;
      while (ptr_Y<maxY) {
        final float ptr_mZ = Math.min(ptr_Z+1.0f, maxZ);
        final float ptr_mY = Math.min(ptr_Y+1.0f, maxY);
        final float xu2 = u1+(ptr_mZ-ptr_Z)*(u2-u1);
        final float xv2 = v1+(ptr_mY-ptr_Y)*(v2-v1);
        buffer.vertex(matrix, X, ptr_Y , ptr_Z ).color(r, g, b, a).uv(u1 , v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(matrix, X, ptr_Y , ptr_mZ).color(r, g, b, a).uv(xu2, v1 ).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(matrix, X, ptr_mY, ptr_mZ).color(r, g, b, a).uv(xu2, xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(matrix, X, ptr_mY, ptr_Z ).color(r, g, b, a).uv(u1 , xv2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 1, 0, 0).endVertex();
        ptr_Y+=1.0f;
      }
      ptr_Z+=1.0f;
    }
  }
  private static record RenderingHappySet(VertexConsumer buffer, Matrix4f matrix, float r, float g, float b, float a, float u1, float v1, float u2, float v2, int light, Matrix3f normal) {
  }
}
