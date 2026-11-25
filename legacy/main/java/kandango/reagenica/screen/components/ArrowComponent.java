package kandango.reagenica.screen.components;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ArrowComponent implements IScreenComponent{
  private final int x;
  private final int y;
  private final Supplier<Integer> progress;
  private final Supplier<Integer> max;
  private final ResourceLocation texture;

  public ArrowComponent(Supplier<Integer> progress, Supplier<Integer> maxProgress, int x, int y, ResourceLocation texture){
    this.progress=progress;
    this.max = maxProgress;
    this.x=x;
    this.y=y;
    this.texture = texture;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    ClientRenderUtil.renderArrowAtDefaultposInGui(texture, graphics, leftPos, topPos, progress.get(), max.get(), x, y);
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
  }
  
}
