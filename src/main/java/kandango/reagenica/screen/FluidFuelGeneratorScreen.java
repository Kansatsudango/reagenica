package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.client.ClientRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class FluidFuelGeneratorScreen extends AbstractContainerScreen<FluidFuelGeneratorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/fluid_fuel_generator.png");

    public FluidFuelGeneratorScreen(FluidFuelGeneratorMenu menu, Inventory inv, Component title) {
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
        ClientRenderUtil.renderEnergyInGui(TEXTURE, graphics, leftPos, topPos, energy, capacity, 147, 37, 176, 33, 3, 16);
        int tankcapacity = 8000;
        {
            FluidStack fluid = menu.getFuelFluid();
            ClientRenderUtil.renderFluidInGui(
                graphics,
                fluid,
                Minecraft.getInstance().level,
                menu.getBlockEntity().getBlockPos(),
                leftPos + 44, topPos + 23, 16, 48,
                tankcapacity
            );
        }
        boolean burning = menu.getBurning();
        if(burning){
            ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, 1, 1, 85, 65);
        }
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);

        if (isMouseOver(mouseX, mouseY, leftPos+142, topPos+37, 13, 16)) {
            int energy = menu.getEnergy();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Energy"));
            tooltip.add(Component.literal(energy + " EU"));

            graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
        }else if (isMouseOver(mouseX, mouseY, leftPos + 44, topPos + 23, 16, 48)) {
            FluidStack fluid = menu.getFuelFluid();
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
