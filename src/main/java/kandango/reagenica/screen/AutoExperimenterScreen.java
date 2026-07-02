package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.screen.components.ArrowComponent;
import kandango.reagenica.screen.components.EnergyComponent;
import kandango.reagenica.screen.components.FireComponent;
import kandango.reagenica.screen.components.IScreenComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AutoExperimenterScreen extends AbstractContainerScreen<AutoExperimenterMenu>{
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/auto_experimenter.png");

  private final List<IScreenComponent> components = new ArrayList<>();

  public AutoExperimenterScreen(AutoExperimenterMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = 205;
    this.inventoryLabelY = this.imageHeight - 94;
    components.add(new ArrowComponent(() -> menu.getProgress(), () -> 200, 94, 73, TEXTURE));
    components.add(new EnergyComponent(menu.getEnergyStorage(), 156, 52, TEXTURE));
    components.add(new FireComponent(() -> menu.isRecipeBurnerOn()?1:0, ()->1, 69, 34, 176, 0, TEXTURE));
    components.add(new FireComponent(() -> menu.isHeating()?1:0, ()->1, 74, 75, 176, 49, TEXTURE));
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
