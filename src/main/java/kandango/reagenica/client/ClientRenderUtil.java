package kandango.reagenica.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class ClientRenderUtil {

  public static void renderFluidInGui(GuiGraphics graphics, FluidStack fluid, BlockAndTintGetter level, BlockPos pos, int x, int y, int width, int height, int capacity) {
    if (fluid.isEmpty() || capacity <= 0) return;

    int amount = fluid.getAmount();
    int scaledHeight = (int)((amount / (float)capacity) * height);
    if (scaledHeight <= 0) return;

    FluidState fluidState = fluid.getFluid().defaultFluidState();
    IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid.getFluid());
    TextureAtlasSprite sprite = Minecraft.getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
        .apply(clientFluid.getStillTexture(fluid));

    int color = Minecraft.getInstance().getBlockColors().getColor(fluidState.createLegacyBlock(), level, pos, 0);
    if (color == -1) {
      IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(fluid.getFluid());
      color = ext.getTintColor(fluid);
    }
    if (((color >> 24) & 0xFF) == 0) {
      color |= 0xFF000000;
    }

    float r = ((color >> 16) & 0xFF) / 255f;
    float g = ((color >> 8) & 0xFF) / 255f;
    float b = (color & 0xFF) / 255f;
    float a = ((color >> 24) & 0xFF) / 255f;

    RenderSystem.enableBlend();
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
    RenderSystem.setShaderColor(r, g, b, a);

    int yBottom = y + height;
    PoseStack poseStack = graphics.pose();
    poseStack.pushPose();
    for (int i = 0; i < scaledHeight; i += 16) {
      for(int j = 0; j < width; j += 16){
        int drawHeight = Math.min(16, scaledHeight - i);
        int drawWidth = Math.min(16, width-j);
        int drawY = yBottom - i - drawHeight;
        int drawX = x + j;

        float u0 = sprite.getU0();
        float u1 = sprite.getU0() + (sprite.getU1() - sprite.getU0()) * (drawWidth / 16.0f);;
        float v0 = sprite.getV0();
        float v1 = sprite.getV0() + (sprite.getV1() - sprite.getV0()) * (drawHeight / 16.0f);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = poseStack.last().pose();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, drawX, drawY + drawHeight, 0).uv(u0, v1).endVertex();
        buffer.vertex(matrix, drawX + drawWidth, drawY + drawHeight, 0).uv(u1, v1).endVertex();
        buffer.vertex(matrix, drawX + drawWidth, drawY, 0).uv(u1, v0).endVertex();
        buffer.vertex(matrix, drawX, drawY, 0).uv(u0, v0).endVertex();
        tesselator.end();
      }
    }
    poseStack.popPose();
    RenderSystem.disableBlend();
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f); // 色リセット

    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    RenderSystem.disableBlend();
  }

  public static void renderEnergyInGui(GuiGraphics graphics, int energy, BlockAndTintGetter level, BlockPos pos, int x, int y, int width, int height, int capacity) {
    ClientRenderUtil.renderFluidInGui(graphics, new FluidStack(Fluids.LAVA,energy), level, pos, x, y, width, height, capacity);
  }
  public static void renderEnergyInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int energy, int maxenergy, int x, int y, int ex, int ey, int gaugewidth, int gaugeheight) {
    int barheight = gaugeheight * energy / (maxenergy!=0 ? maxenergy : Integer.MAX_VALUE);
    graphics.blit(texture, leftPos+x, topPos+y+gaugeheight-barheight, ex, ey+gaugeheight-barheight, gaugewidth, barheight);
  }
  public static void renderHorizontalInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int amount, int max, int x, int y, int ex, int ey, int gaugewidth, int gaugeheight) {
    int barwidth = gaugewidth * amount / (max!=0 ? max : Integer.MAX_VALUE);
    graphics.blit(texture, leftPos+x, topPos+y, ex, ey, barwidth, gaugeheight);
  }

  public static void renderFireInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int fireheight, int x, int y, int fx, int fy){
    graphics.blit(texture, leftPos+x, topPos+y+14-fireheight, fx, fy+14-fireheight, 14,fireheight);
  }
  public static void renderFireInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int fuel, int maxfuel, int x, int y, int fx, int fy){
    int fireheight = fireHeight(fuel, maxfuel);
    graphics.blit(texture, leftPos+x, topPos+y+14-fireheight, fx, fy+14-fireheight, 14,fireheight);
  }
  public static void renderFireAtDefaultposInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int fuel, int maxfuel, int x, int y){
    int fireheight = fireHeight(fuel, maxfuel);
    graphics.blit(texture, leftPos+x, topPos+y+14-fireheight, 176, 14-fireheight, 14,fireheight);
  }
  private static int fireHeight(int remain, int max){
    if(remain==0){
      return 0;
    }else if(max<=0 || remain>max){// I don't want ArithmeticException or something strange just for cosmetic
      return 0;
    }else{
      int h = ceilDiv(12*remain,max);
      return h+1;
    }
  }
  private static int ceilDiv(int a, int b) {
    return (a + b - 1) / b;
  }

  public static void renderArrowInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int arrowlength, int x, int y, int ax, int ay){
    graphics.blit(texture, leftPos+x, topPos+y, ax, ay, arrowlength,16);
  }
  public static void renderArrowInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int progress, int maxprogress, int x, int y, int ax, int ay){
    int arrowlength = 24*progress/maxprogress;
    graphics.blit(texture, leftPos+x, topPos+y, ax, ay, arrowlength,16);
  }
  public static void renderArrowAtDefaultposInGui(ResourceLocation texture, GuiGraphics graphics, int leftPos, int topPos, int progress, int maxprogress, int x, int y){
    int arrowlength = 24*progress/maxprogress;
    graphics.blit(texture, leftPos+x, topPos+y, 176, 14, arrowlength,16);
  }
}
