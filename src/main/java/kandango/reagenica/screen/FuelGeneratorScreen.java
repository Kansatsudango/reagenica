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

public class FuelGeneratorScreen extends AbstractContainerScreen<FuelGeneratorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/fuel_generator.png");

    public FuelGeneratorScreen(FuelGeneratorMenu menu, Inventory inv, Component title) {
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
        int bt = menu.getBurnTime();
        int mt = menu.getMaxburntime();
        int arrowwidth = mt==0||bt==0 ? 0 : 24 - 24 * bt / mt;
        ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, bt, mt, 56, 25);
        graphics.blit(TEXTURE, leftPos+79, topPos+34, 176, 14, arrowwidth,16);
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
