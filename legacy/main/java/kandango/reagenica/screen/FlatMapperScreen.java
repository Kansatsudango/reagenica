package kandango.reagenica.screen;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.client.ClientRenderUtil;
import kandango.reagenica.packet.FlatMapperTogglePacket;
import kandango.reagenica.packet.ModMessages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FlatMapperScreen extends AbstractContainerScreen<FlatMapperMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/flatmapper.png");

  public FlatMapperScreen(FlatMapperMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = 165;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    boolean isToggleOn = menu.isToggleOn();
    graphics.blit(TEXTURE, leftPos+67, topPos+56, 176, isToggleOn?55:49, 11, 6);
    ClientRenderUtil.drawLongString(graphics, font, Component.translatable("gui.reagenica.flatmapper_stay_bag"), leftPos+80, topPos+55, 88, isToggleOn?0x404040:0x808080);
  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);
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

    @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    super.mouseClicked(mouseX, mouseY, button);
    double x = mouseX-leftPos;
    double y = mouseY-topPos;
    if(67<=x && x<=167 && 56<=y && y<=79){
        ModMessages.CHANNEL.sendToServer(new FlatMapperTogglePacket());
    }
    return true;
  }
}
