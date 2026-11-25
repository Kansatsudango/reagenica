package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.screen.components.IScreenComponent;
import kandango.reagenica.screen.components.VirtualTankComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class OnsenDetecterScreen extends AbstractContainerScreen<OnsenDetecterMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/onsen_analyzer.png");

  private final List<IScreenComponent> components = new ArrayList<>();
  private final FluidTank virtualTank = new FluidTank(200);

  public OnsenDetecterScreen(OnsenDetecterMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    this.imageHeight = 165;
    this.inventoryLabelY = 9999;//No drawing
    components.add(new VirtualTankComponent(virtualTank, 24, 26, menu.getPos()));
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    virtualTank.setFluid(new FluidStack(menu.getOnsenType().getFluid(),120));
    for(IScreenComponent component : components){
      component.render(graphics, partialTicks, mouseX, mouseY, topPos, leftPos);
    }
    graphics.drawString(font, menu.getOnsenType().getNameComponent(), leftPos+53, topPos+26, 0xfefefe);
    drawLongString(graphics, font, menu.getOnsenType().getLoreComponent(), leftPos+53, topPos+40, 0xd0d0d0);
    graphics.drawString(font, Component.translatable("gui.reagenica.continentalness"), leftPos+25,  topPos+95, 0x404040, false);
    graphics.drawString(font, Component.translatable("gui.reagenica.erosion"        ), leftPos+25, topPos+107, 0x404040, false);
    graphics.drawString(font, Component.translatable("gui.reagenica.weirdness"      ), leftPos+25, topPos+119, 0x404040, false);
    graphics.drawString(font, Component.translatable("gui.reagenica.humidity"       ), leftPos+25, topPos+131, 0x404040, false);
    graphics.drawString(font, Component.translatable("gui.reagenica.temperature"    ), leftPos+25, topPos+143, 0x404040, false);
    graphics.drawString(font, Component.literal(simple(menu.getContinentalness())), leftPos+100,  topPos+95, 0xfefefe);
    graphics.drawString(font, Component.literal(simple(menu.getErosion())), leftPos+100, topPos+107, 0xfefefe);
    graphics.drawString(font, Component.literal(simple(menu.getWeirdness())), leftPos+100, topPos+119, 0xfefefe);
    graphics.drawString(font, Component.literal(simple(menu.getHumidity())), leftPos+100, topPos+131, 0xfefefe);
    graphics.drawString(font, Component.literal(simple(menu.getTemperature())), leftPos+100, topPos+143, 0xfefefe);
  }
  private String simple(float value){
    return String.format(Locale.US, "%.4f", value);
  }
  private void drawLongString(GuiGraphics graphics, Font font, Component longText, int x, int y, int color){
    final int maxWidth = 100;
    List<FormattedCharSequence> lines = font.split(longText, maxWidth);
    int yOffset = 0;
    for(FormattedCharSequence line : lines){
      graphics.drawString(font, line, x, y+yOffset, color);
      yOffset += font.lineHeight;
    }
  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);
    for(IScreenComponent component : components){
      component.tooltip(graphics, mouseX, mouseY, topPos, leftPos, font);
    }
  }

  @Override
  protected void renderLabels(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    this.renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, delta);
    this.renderTooltip(graphics, mouseX, mouseY);
  }
}
