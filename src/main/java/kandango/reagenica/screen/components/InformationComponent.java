package kandango.reagenica.screen.components;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InformationComponent implements IScreenComponent{
  private final BooleanSupplier shouldShow;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int refx;
  private final int refy;
  private final Supplier<List<Component>> tooltip;
  private final ResourceLocation texture;

  public InformationComponent(BooleanSupplier shouldShow, int x, int y, int width, int height, int refx, int refy, Supplier<List<Component>> tooltip, ResourceLocation texture) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.refx = refx;
    this.refy = refy;
    this.texture = texture;
    this.tooltip = tooltip;
    this.shouldShow = shouldShow;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    if(this.shouldShow.getAsBoolean()){
      graphics.blit(texture, leftPos+x, topPos+y, refx, refy, width, height);
    }
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
    if (isMouseOver(leftPos+x, topPos+y, width, height, mouseX, mouseY)) {
      graphics.renderTooltip(font, this.tooltip.get(), Optional.empty(), mouseX, mouseY);
    }
  }
  
}
