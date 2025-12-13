package kandango.reagenica.screen.components;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface IScreenComponent {
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos);
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font);

  default boolean isMouseOver(int x, int y, int width, int height, int mouseX, int mouseY){
    return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
  }
}