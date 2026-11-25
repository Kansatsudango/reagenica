package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.screen.components.ArrowComponent;
import kandango.reagenica.screen.components.EnergyComponent;
import kandango.reagenica.screen.components.IScreenComponent;
import kandango.reagenica.screen.components.TankComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PEMDeviceScreen extends AbstractContainerScreen<PEMDeviceMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/pem_device.png");

  private final List<IScreenComponent> components = new ArrayList<>();
  
  public PEMDeviceScreen(PEMDeviceMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = 165;
    this.inventoryLabelY = this.imageHeight - 94;
    final BlockPos worldPosition = menu.getBlockEntity().getBlockPos();
    components.add(new TankComponent(menu.waterTank, 28, 30, worldPosition));
    components.add(new TankComponent(menu.hydrogenTank, 73, 30, worldPosition));
    components.add(new TankComponent(menu.oxygenTank, 116, 30, worldPosition));
    components.add(new EnergyComponent(menu.getEnergyStorage(), 163, 62, TEXTURE));
    components.add(new ArrowComponent(() -> menu.getProgress(), () -> 200, 46, 44, TEXTURE));
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    for(IScreenComponent component : components){
      component.render(graphics, partialTicks, mouseX, mouseY, topPos, leftPos);
    }
  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);
    for(IScreenComponent component : components){
      component.tooltip(graphics, mouseX, mouseY, topPos, leftPos, font);
    }
  }

  @Override
  protected void renderLabels(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
  }

  @Override
  public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    this.renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, delta);
    this.renderTooltip(graphics, mouseX, mouseY);
  }
}
