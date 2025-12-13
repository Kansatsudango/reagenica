package kandango.reagenica.screen.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TankComponent implements IScreenComponent{
  private final FluidTank tank;
  private final BlockPos pos;
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  public TankComponent(FluidTank tank, int x, int y, BlockPos pos){
    this(tank, x, y, 16, 48, pos);
  }
  public TankComponent(FluidTank tank, int x, int y, int width, int height, BlockPos pos){
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
    if (isMouseOver(leftPos+x, topPos+y, width, height,mouseX, mouseY)) {
      FluidStack fluid = tank.getFluid();
      if (!fluid.isEmpty()) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(fluid.getTranslationKey()));
        tooltip.add(Component.literal(fluid.getAmount() + " mB"));

        graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
      } else {
        graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
      }
    }
  }
  
}
