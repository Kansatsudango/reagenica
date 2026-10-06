package kandango.reagenica.screen.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntSupplier;
import javax.annotation.Nonnull;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GaugeComponent implements IScreenComponent{
  private final IntSupplier value;
  private final int maxValue;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int refx;
  private final int refy;
  private final Function<Integer, Component> tooltip;
  private final ResourceLocation texture;

  public GaugeComponent(IntSupplier value, int maxValue, int x, int y, int width, int height, int refx, int refy, Function<Integer, Component> tooltip, ResourceLocation texture){
    if(maxValue<=0)throw new IllegalArgumentException("Gauge Max Value must be >0");
    this.value=value;
    this.maxValue=maxValue;
    this.x=x;
    this.y=y;
    this.width=width;
    this.height=height;
    this.refx=refx;
    this.refy=refy;
    this.tooltip=tooltip;
    this.texture = texture;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    int gaugeHeight = this.height * value.getAsInt() / this.maxValue;
    int dy = height-gaugeHeight;
    graphics.blit(texture, leftPos+x, topPos+y+dy, refx, refy+dy, width, height);
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
    if (isMouseOver(leftPos+x, topPos+y, width, height,mouseX, mouseY)) {
      List<Component> tooltip = new ArrayList<>();
      tooltip.add(this.tooltip.apply(this.value.getAsInt()));

      graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
    }
  }
  
}
