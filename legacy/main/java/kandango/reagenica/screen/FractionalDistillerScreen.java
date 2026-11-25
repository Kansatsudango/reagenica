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

public class FractionalDistillerScreen extends AbstractContainerScreen<FractionalDistillerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/fractional_distiller.png");

    public FractionalDistillerScreen(FractionalDistillerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageHeight = 205;
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

        FluidStack fluid_in = menu.getFluidInput();
        FluidStack fluid_top = menu.getFluidTop();
        FluidStack fluid_bottom = menu.getFluidBottom();
        FluidStack fluid_water = menu.getFluidWater();
        int capacity = 8000;

        ClientRenderUtil.renderFluidInGui(
            graphics,
            fluid_in,
            Minecraft.getInstance().level,
            menu.getBlockEntity().getBlockPos(),
            leftPos + 8, topPos + 39, 16, 48,
            capacity
        );
        ClientRenderUtil.renderFluidInGui(
            graphics,
            fluid_top,
            Minecraft.getInstance().level,
            menu.getBlockEntity().getBlockPos(),
            leftPos + 129, topPos + 9, 16, 48,
            capacity
        );
        ClientRenderUtil.renderFluidInGui(
            graphics,
            fluid_bottom,
            Minecraft.getInstance().level,
            menu.getBlockEntity().getBlockPos(),
            leftPos + 129, topPos + 70, 16, 48,
            capacity
        );
        ClientRenderUtil.renderFluidInGui(
            graphics,
            fluid_water,
            Minecraft.getInstance().level,
            menu.getBlockEntity().getBlockPos(),
            leftPos + 81, topPos + 9, 16, 48,
            capacity
        );
        int progress = menu.getProgress();
        ClientRenderUtil.renderArrowAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, progress, 200, 29, 64);
        int fuel = menu.getBurnTime();
        int maxfuel = menu.getMaxBurnTime();
        ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, fuel, maxfuel, 8, 88);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);

        if (isMouseOver(mouseX, mouseY, leftPos + 8, topPos + 39, 16, 48)) {
            FluidStack fluid = menu.getFluidInput();
            if (!fluid.isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(fluid.getTranslationKey()));
                tooltip.add(Component.literal(fluid.getAmount() + " mB"));

                graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            } else {
                graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
            }
        }else if (isMouseOver(mouseX, mouseY, leftPos + 129, topPos + 9, 16, 48)) {
            FluidStack fluid = menu.getFluidTop();
            if (!fluid.isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(fluid.getTranslationKey()));
                tooltip.add(Component.literal(fluid.getAmount() + " mB"));

                graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            } else {
                graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
            }
        }else if (isMouseOver(mouseX, mouseY, leftPos + 129, topPos + 70, 16, 48)) {
            FluidStack fluid = menu.getFluidBottom();
            if (!fluid.isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(fluid.getTranslationKey()));
                tooltip.add(Component.literal(fluid.getAmount() + " mB"));

                graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            } else {
                graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
            }
        }else if (isMouseOver(mouseX, mouseY, leftPos + 81, topPos + 9, 16, 48)) {
            FluidStack fluid = menu.getFluidWater();
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
