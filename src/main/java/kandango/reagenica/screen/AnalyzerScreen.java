package kandango.reagenica.screen;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.packet.AnalyzerButtonPacket;
import kandango.reagenica.packet.ModMessages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AnalyzerScreen extends AbstractContainerScreen<AnalyzerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/analyzer.png");

    public AnalyzerScreen(AnalyzerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageHeight = 165;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.translatable("gui.reagenica.analyze"), button -> {
            ModMessages.CHANNEL.sendToServer(new AnalyzerButtonPacket(menu.getBlockEntity().getBlockPos()));
        }).bounds(this.leftPos + 9, this.topPos + 41, 40, 20).build());
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
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
