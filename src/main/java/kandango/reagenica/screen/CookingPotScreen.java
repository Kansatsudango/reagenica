package kandango.reagenica.screen;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CookingPotScreen extends AbstractContainerScreen<CookingPotMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/cooking_pot.png");

    public CookingPotScreen(CookingPotMenu menu, Inventory inv, Component title) {
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

        int progress = menu.getProgress();
        ClientRenderUtil.renderArrowAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, progress, 200, 89, 31);
        
        int bt = menu.getBurnTime();
        int mt = menu.getMaxBurnTime();
        ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, bt, mt, 45, 58);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
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
