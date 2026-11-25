package kandango.reagenica.screen.components;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class FireComponent implements IScreenComponent{
  private final int x;
  private final int y;
  private final int fx;
  private final int fy;
  private final Supplier<Integer> progress;
  private final Supplier<Integer> max;
  private final ResourceLocation texture;

  public FireComponent(Supplier<Integer> progress, Supplier<Integer> maxProgress, int x, int y, ResourceLocation texture){
    this.progress=progress;
    this.max = maxProgress;
    this.x=x;
    this.y=y;
    this.fx=176;
    this.fy=0;
    this.texture = texture;
  }
  public FireComponent(Supplier<Integer> progress, Supplier<Integer> maxProgress, int x, int y, int fx, int fy, ResourceLocation texture){
    this.progress=progress;
    this.max = maxProgress;
    this.x=x;
    this.y=y;
    this.fx=fx;
    this.fy=fy;
    this.texture = texture;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    ClientRenderUtil.renderFireInGui(texture, graphics, leftPos, topPos, progress.get(), max.get(), x, y, fx, fy);
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
  }
  
}
