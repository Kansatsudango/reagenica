package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.client.ClientRenderUtil;
import kandango.reagenica.client.MiniVerticalSlider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlastFurnaceScreen extends AbstractContainerScreen<BlastFurnaceMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/blast_furnace.png");

  public BlastFurnaceScreen(BlastFurnaceMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = 165;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void init() {
    super.init();
    double slidervalue = menu.getSliderValue();
    this.addRenderableWidget(new MiniVerticalSlider(this.leftPos + 161, this.topPos + 18, 4, 48, 4, slidervalue, menu.getBlockEntity().getBlockPos()));
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    int bt = menu.getBurnTime();
    int mt = menu.getMaxBurnTime();
    int pr = menu.getProgress();
    int tm = menu.getTemp();
    ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, bt, mt, 56, 42);
    ClientRenderUtil.renderArrowAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, pr, 2000000, 79, 23);
    ClientRenderUtil.renderEnergyInGui(TEXTURE, graphics, leftPos, topPos, tm, 16000, 148, 18, 176, 31, 4, 48);

  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);

    int tankX = leftPos + 148;
    int tankY = topPos + 18;
    int tankWidth = 6;
    int tankHeight = 48;

    if (isMouseOver(mouseX, mouseY, tankX, tankY, tankWidth, tankHeight)) {
      int temperature = menu.getTemp();
      List<Component> tooltip = new ArrayList<>();
      tooltip.add(Component.literal("Temperature"));
      tooltip.add(Component.literal(temperature/10 + " ℃"));

      graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
    }
  }

  private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
    return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
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
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy){
    for(GuiEventListener guieventlistener : this.children()) {
      if (guieventlistener.mouseDragged(mouseX, mouseY, button, dx, dy)) {
        return true;
      }
    }
    return super.mouseDragged(mouseX, mouseY, button, dx, dy);
  }
}
