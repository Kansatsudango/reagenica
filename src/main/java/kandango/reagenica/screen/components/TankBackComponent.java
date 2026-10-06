package kandango.reagenica.screen.components;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TankBackComponent implements IScreenComponent{
  private final TankComponent maintank;
  private final List<ConcealedArea> concealedAreas;

  public TankBackComponent(FluidTank tank, int x, int y, BlockPos pos, List<ConcealedArea> concealed){
    this(tank, x, y, 16, 48, pos, concealed);
  }
  public TankBackComponent(FluidTank tank, int x, int y, int width, int height, BlockPos pos, List<ConcealedArea> concealed){
    this.maintank = new TankComponent(tank, x, y, width, height, pos);
    this.concealedAreas = concealed;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    maintank.render(graphics, partialTicks, mouseX, mouseY, topPos, leftPos);
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
    if (!concealedAreas.stream().anyMatch(a -> isMouseOver(leftPos+a.x, topPos+a.y, a.width, a.height, mouseX, mouseY))) {
      maintank.tooltip(graphics, mouseX, mouseY, topPos, leftPos, font);
    }
  }
  
  public static record ConcealedArea(int x, int y, int width, int height) {
    public static ConcealedArea of(int x, int y, int width, int height){
      return new ConcealedArea(x, y, width, height);
    }
  }
}
