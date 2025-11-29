package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class SolarPowerGeneratorScreen extends AbstractContainerScreen<SolarPowerGeneratorMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/solar_generator.png");

  public SolarPowerGeneratorScreen(SolarPowerGeneratorMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = 165;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

    int energy = menu.getEnergy();
    int capacity = menu.getMaxEnergy();
    ClientRenderUtil.renderEnergyInGui(TEXTURE, graphics, leftPos, topPos, energy, capacity, 133, 37, 176, 33, 3, 16);
    Level lv = menu.getLevel();
    if(lv!=null){
      boolean isClear = !lv.isRaining() && !lv.isThundering();
      int dayTime = (int)(lv.getDayTime() % 24000);
      boolean isBlocked = menu.isBlocked();
      if(dayTime<12000){//day
        if(isClear){
          graphics.blit(TEXTURE,leftPos+52,topPos+39,176,50,7,7);
        }else{
          graphics.blit(TEXTURE,leftPos+52,topPos+39,197,50,7,7);
        }
      }else if(13000<dayTime && dayTime<23000){//night
        graphics.blit(TEXTURE,leftPos+52,topPos+39,190,50,7,7);
      }else{//dawn or evening
        if(isClear){
          graphics.blit(TEXTURE,leftPos+52,topPos+39,183,50,7,7);
        }else{
          graphics.blit(TEXTURE,leftPos+52,topPos+39,197,50,7,7);
        }
      }
      if(isBlocked){
        graphics.blit(TEXTURE,leftPos+38,topPos+37,196,57,11,11);
      }
    }
  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);

    int tankX = leftPos + 132;
    int tankY = topPos + 36;
    int tankWidth = 5;
    int tankHeight = 18;

    if (isMouseOver(mouseX, mouseY, tankX, tankY, tankWidth, tankHeight)) {
      int energy = menu.getEnergy();
      List<Component> tooltip = new ArrayList<>();
      tooltip.add(Component.literal("Energy"));
      tooltip.add(Component.literal(energy + " EU"));
      graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
    }
    if(menu.isBlocked()){
      if(isMouseOver(mouseX, mouseY, leftPos+38, topPos+37, 11, 11)){
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("gui.reagenica.solar_panel_blocked"));
        graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
      }
    }
  }

  private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
    return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
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
