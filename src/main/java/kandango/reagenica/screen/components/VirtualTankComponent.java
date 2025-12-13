package kandango.reagenica.screen.components;

import javax.annotation.Nonnull;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class VirtualTankComponent implements IScreenComponent{
  private final FluidTank tank;
  private final BlockPos pos;
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public VirtualTankComponent(FluidTank tank, int x, int y, BlockPos pos){
    this(tank, x, y, 16, 48, pos);
  }
  public VirtualTankComponent(FluidTank tank, int x, int y, int width, int height, BlockPos pos){
    this.tank=tank;
    this.pos=pos;
    this.x=x;
    this.y=y;
    this.width=width;
    this.height=height;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    ClientRenderUtil.renderFluidInGui(
      graphics,
      tank.getFluid(),
      Minecraft.getInstance().level,
      pos,
      leftPos + x, topPos + y, width, height,
      tank.getCapacity()
    );
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
  }
  
}
