package kandango.reagenica.screen.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyComponent implements IScreenComponent{
  private final EnergyStorage storage;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final ResourceLocation texture;

  public EnergyComponent(EnergyStorage storage, int x, int y, ResourceLocation texture){
    this(storage, x, y, 3, 16, texture);
  }
  public EnergyComponent(EnergyStorage storage, int x, int y, int width, int height, ResourceLocation texture){
    this.storage=storage;
    this.x=x;
    this.y=y;
    this.width=width;
    this.height=height;
    this.texture = texture;
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY, int topPos, int leftPos) {
    ClientRenderUtil.renderEnergyInGui(texture, graphics, leftPos, topPos, storage.getEnergyStored(), storage.getMaxEnergyStored(), x, y, 176, 33, width, height);
  }

  @Override
  public void tooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, int topPos, int leftPos, Font font) {
    if (isMouseOver(leftPos+x, topPos+y, width, height,mouseX, mouseY)) {
      List<Component> tooltip = new ArrayList<>();
      tooltip.add(Component.literal("Energy"));
      tooltip.add(Component.literal(storage.getEnergyStored() + " EU"));

      graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
    }
  }
  
}
