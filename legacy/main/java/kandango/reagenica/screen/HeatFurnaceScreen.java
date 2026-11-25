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

public class HeatFurnaceScreen extends AbstractContainerScreen<HeatFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/heat_furnace.png");

    public HeatFurnaceScreen(HeatFurnaceMenu menu, Inventory inv, Component title) {
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

        FluidStack fluid = menu.getFluidInput();
        int capacity = menu.getTankCapacity();
        ClientRenderUtil.renderFluidInGui(
            graphics,
            fluid,
            Minecraft.getInstance().level,
            menu.getBlockEntity().getBlockPos(),
            leftPos + 26, topPos + 30, 16, 48,
            capacity
        );
        fluid = menu.getFluidOutput();
        ClientRenderUtil.renderFluidInGui(
            graphics,
            fluid,
            Minecraft.getInstance().level,
            menu.getBlockEntity().getBlockPos(),
            leftPos + 135, topPos + 30, 16, 48,
            capacity
        );
        int energy = menu.getEnergy();
        capacity = menu.getMaxEnergy();
        ClientRenderUtil.renderEnergyInGui(TEXTURE, graphics, leftPos, topPos, energy, capacity, 84, 59, 176, 33, 3, 16);
        int progress = menu.getProgress();
        int fuel = menu.getBurnTime();
        int maxfuel = menu.getMaxBurnTime();
        ClientRenderUtil.renderFireAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, fuel, maxfuel, 56, 42);
        ClientRenderUtil.renderArrowAtDefaultposInGui(TEXTURE, graphics, leftPos, topPos, progress, 200, 79, 23);
        if(menu.isUsingEnergy()){
            graphics.blit(TEXTURE, leftPos+73, topPos+63, 176, 31, 10, 2);
        }
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);

        if (isMouseOver(mouseX, mouseY, leftPos+26, topPos+30, 16, 48)) {
            FluidStack fluid = menu.getFluidInput();
            if (!fluid.isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(fluid.getTranslationKey()));
                tooltip.add(Component.literal(fluid.getAmount() + " mB"));

                graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            } else {
                graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
            }
        }else if (isMouseOver(mouseX, mouseY, leftPos+135, topPos+30, 16, 48)) {
            FluidStack fluid = menu.getFluidOutput();
            if (!fluid.isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(fluid.getTranslationKey()));
                tooltip.add(Component.literal(fluid.getAmount() + " mB"));

                graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            } else {
                graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
            }
        }else if (isMouseOver(mouseX, mouseY, leftPos+84, topPos+59, 13, 16)) {
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
