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

public class ElectroLysisScreen extends AbstractContainerScreen<ElectroLysisMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/electrolysis.png");

  public ElectroLysisScreen(ElectroLysisMenu menu, Inventory inv, Component title) {
    super(menu, inv, title);
    this.imageHeight = 191;
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
    int bubble = progress%20;
    ClientRenderUtil.renderEnergyInGui(TEXTURE, graphics, leftPos, topPos, bubble, 20, 67, 52, 176, 31, 18, 20);
    final int largetankcap = 8000;
    FluidStack fluid = getInflatedFluidstack(menu.getFluidInput(),largetankcap);
    ClientRenderUtil.renderFluidInGui(
      graphics,
      fluid,
      Minecraft.getInstance().level,
      menu.getBlockEntity().getBlockPos(),
      leftPos + 44, topPos + 51, 64, 48,
      largetankcap*2
    );
    fluid = menu.getFluidOutput();
    int capacity = menu.getTankCapacity();
    ClientRenderUtil.renderFluidInGui(
      graphics,
      fluid,
      Minecraft.getInstance().level,
      menu.getBlockEntity().getBlockPos(),
      leftPos + 134, topPos + 51, 16, 48,
      capacity
    );
    int energy = menu.getEnergy();
    capacity = menu.getMaxEnergy();
    ClientRenderUtil.renderEnergyInGui(TEXTURE, graphics, leftPos, topPos, energy, capacity, 69, 13, 194, 31, 3, 16);
    if(menu.isUsingEnergy()){
      graphics.blit(TEXTURE, leftPos+53, topPos+4, 0, 166, 64, 1);
      graphics.blit(TEXTURE, leftPos+105, topPos+11, 0, 166, 12, 1);
    }
  }

  private FluidStack getInflatedFluidstack(FluidStack original,int capacity){
    if(original.isEmpty())return original;
    FluidStack stack = original.copy();
    int amount = stack.getAmount();
    stack.setAmount(capacity+amount);
    return stack;
  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);

    if (isMouseOver(mouseX, mouseY, 44, 51, 64, 48) 
      && !isMouseOverSlot(mouseX, mouseY, 49, 57) && !isMouseOverSlot(mouseX, mouseY, 49, 81)
      && !isMouseOverSlot(mouseX, mouseY, 87, 57) && !isMouseOverSlot(mouseX, mouseY, 79, 79)) {
      FluidStack fluid = menu.getFluidInput();
      if (!fluid.isEmpty()) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(fluid.getTranslationKey()));
        tooltip.add(Component.literal(fluid.getAmount() + " mB"));

        graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
      } else {
        graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
      }
    }else if (isMouseOver(mouseX, mouseY, 134, 51, 16, 48)) {
      FluidStack fluid = menu.getFluidOutput();
      if (!fluid.isEmpty()) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(fluid.getTranslationKey()));
        tooltip.add(Component.literal(fluid.getAmount() + " mB"));

        graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
      } else {
        graphics.renderTooltip(font, List.of(Component.literal("Empty")), Optional.empty(), mouseX, mouseY);
      }
    }else if (isMouseOver(mouseX, mouseY, 69, 13, 3, 16)) {
      int energy = menu.getEnergy();
      List<Component> tooltip = new ArrayList<>();
      tooltip.add(Component.literal("Energy"));
      tooltip.add(Component.literal(energy + " EU"));

      graphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
    }
  }

  private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
    return mouseX >= leftPos+x && mouseX < leftPos+x + width && mouseY >= topPos+y && mouseY < topPos+y + height;
  }
  private boolean isMouseOverSlot(int mouseX, int mouseY, int slotx, int sloty){
    return isMouseOver(mouseX, mouseY, slotx-1, sloty-1, 18, 18);
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
