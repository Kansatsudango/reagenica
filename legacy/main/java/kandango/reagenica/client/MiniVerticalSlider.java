package kandango.reagenica.client;

import javax.annotation.Nonnull;

import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.SliderValuePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class MiniVerticalSlider extends AbstractWidget {
  private double value; // 0.0〜1.0
  private boolean dragging = false;
  private final BlockPos pos;
  private final int handleHeight;

  public MiniVerticalSlider(int x, int y, int width, int height, int handleHeight, double initialValue, BlockPos pos) {
    super(x, y, width, height, Component.empty());
    this.handleHeight = handleHeight;
    this.value = Mth.clamp(initialValue, 0.0, 1.0);
    this.pos = pos;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.isHovered && button == 0) {
      this.dragging = true;
      this.setFocused(true);
      updateValueFromMouse(mouseY);
      return true;
    }
    return false;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (button == 0 && this.dragging) {
      this.dragging = false;
      this.setFocused(false);
      return true;
    }
    return false;
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
    if (this.dragging) {
      updateValueFromMouse(mouseY);
      return true;
    }
    return false;
  }

  private void updateValueFromMouse(double mouseY) {
    double relY = mouseY - this.getY();
    double usableHeight = this.height - this.handleHeight;
    this.value = Mth.clamp(1.0 - relY / usableHeight, 0.0, 1.0);
    this.onValueChanged();
  }

  private void onValueChanged(){
    ModMessages.CHANNEL.sendToServer(new SliderValuePacket(pos, value));
  }

  public double getValue() {
    return this.value;
  }

  public void setValue(double newValue) {
    this.value = Mth.clamp(newValue, 0.0, 1.0);
  }

  @Override
  protected void renderWidget(@Nonnull GuiGraphics graphics, int p_268034_, int p_268009_, float p_268085_) {
    // 背景（バー）
    graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF333333);
    // ハンドル（つまみ）
    int handleY = (int)(this.getY() + (1.0 - this.value) * (this.height - this.handleHeight));
    graphics.fill(this.getX(), handleY, this.getX() + this.width, handleY + this.handleHeight, 0xFF4444FF);
  }

  @Override
  protected void updateWidgetNarration(@Nonnull NarrationElementOutput builder) {
    builder.add(NarratedElementType.TITLE, this.getMessage());
    builder.add(NarratedElementType.USAGE, Component.literal("スライダーの値は " + (int)(value * 100) + "% です"));
  }
}
