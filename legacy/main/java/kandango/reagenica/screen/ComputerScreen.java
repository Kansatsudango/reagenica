package kandango.reagenica.screen;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.ChemiItems;
import kandango.reagenica.packet.ComputerOrderPacket;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.recipes.ReagenimartRecipe;
import kandango.reagenica.recipes.ReagenimartRecipe.ReagenimartCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ComputerScreen extends AbstractContainerScreen<ComputerMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/container/computer.png");

  private ReagenimartCategory currentcat = ReagenimartCategory.BUILDING;
  private final Map<ReagenimartCategory,ItemStack> catItemMap = Map.of(
    ReagenimartCategory.BUILDING, new ItemStack(Blocks.BRICKS),
    ReagenimartCategory.COMMODITY, new ItemStack(ChemiItems.CHLORINE.get()),
    ReagenimartCategory.MINERALS, new ItemStack(Items.RAW_GOLD),
    ReagenimartCategory.NATURALS, new ItemStack(Items.BLUE_ORCHID)
  );
  private int currentselectedIndex = -1;
  private int currentScroll = 0;

  public ComputerScreen(ComputerMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    this.imageHeight = 191;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    graphics.renderFakeItem(catItemMap.get(ReagenimartCategory.BUILDING), leftPos+6, topPos+18);
    graphics.renderFakeItem(catItemMap.get(ReagenimartCategory.COMMODITY), leftPos+6, topPos+40);
    graphics.renderFakeItem(catItemMap.get(ReagenimartCategory.MINERALS), leftPos+6, topPos+62);
    graphics.renderFakeItem(catItemMap.get(ReagenimartCategory.NATURALS), leftPos+6, topPos+84);
    List<ReagenimartRecipe> trades = menu.recipes(currentcat);
    for(int i=0; i<15; i++){
      int itemIndex = currentScroll*3+i;
      if(itemIndex >= trades.size()) break;
      graphics.renderFakeItem(trades.get(itemIndex).getMerchandise(), leftPos + 26 + (i%3)*18, topPos + 15 + (i/3)*18);
    }
    @Nullable ReagenimartRecipe selectedRecipe = getRecipeSelected();
    if(selectedRecipe!=null){
      renderFakeItemandCounts(graphics, selectedRecipe.getPrice(), leftPos+105, topPos+45);
      renderFakeItemandCounts(graphics, selectedRecipe.getMerchandise(), leftPos+144, topPos+45);
    }
  }
  private void renderFakeItemandCounts(GuiGraphics graphics, ItemStack stack, int x, int y){
    graphics.renderFakeItem(stack, x, y);
    graphics.renderItemDecorations(font, stack, x, y);
  }
  @Nullable private ReagenimartRecipe getRecipeSelected(){
    if(currentselectedIndex==-1)return null;
    if(currentselectedIndex >= menu.recipes(currentcat).size())return null;
    return menu.recipes(currentcat).get(currentselectedIndex);
  }

  @Override
  protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderTooltip(graphics, mouseX, mouseY);
    int mouseXinGui = (int)mouseX-leftPos;
    int mouseYinGui = (int)mouseY-topPos;
    if(mouseXinGui>4 && mouseXinGui<23){
      if(mouseYinGui>17 && mouseYinGui<35){
        graphics.renderTooltip(font, Component.translatable("gui.reagenica.building"), mouseX, mouseY);
      }else if(mouseYinGui>39 && mouseYinGui<57){
        graphics.renderTooltip(font, Component.translatable("gui.reagenica.commodity"), mouseX, mouseY);
      }else if(mouseYinGui>61 && mouseYinGui<79){
        graphics.renderTooltip(font, Component.translatable("gui.reagenica.minerals"), mouseX, mouseY);
      }else if(mouseYinGui>83 && mouseYinGui<101){
        graphics.renderTooltip(font, Component.translatable("gui.reagenica.naturals"), mouseX, mouseY);
      }
    }else if(mouseXinGui>25 && mouseXinGui<79){
      if(mouseYinGui>14 && mouseYinGui<104){
        int x = (int)(mouseXinGui-25)/18;
        int y = (int)(mouseYinGui-14)/18;
        int index = x+y*3+currentScroll*3;
        if(index<menu.recipes(currentcat).size()){
          ItemStack merchandise = menu.recipes(currentcat).get(index).getMerchandise();
          graphics.renderTooltip(font, merchandise, mouseX, mouseY);
        }
      }
    }else if(mouseXinGui>105 && mouseXinGui<123 && mouseYinGui>45 && mouseYinGui<63){
      ReagenimartRecipe recipe = getRecipeSelected();
      if(recipe!=null){
        ItemStack stack = recipe.getPrice();
        graphics.renderTooltip(font, stack, mouseX, mouseY);
      }
    }else if(mouseXinGui>144 && mouseXinGui<162 && mouseYinGui>45 && mouseYinGui<63){
      ReagenimartRecipe recipe = getRecipeSelected();
      if(recipe!=null){
        ItemStack stack = recipe.getMerchandise();
        graphics.renderTooltip(font, stack, mouseX, mouseY);
      }
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

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    boolean direction = delta < 0; //true:down false:up
    int listsize = menu.recipes(currentcat).size();
    int scrollable = (listsize-13)/3;
    if(scrollable<0)scrollable=0;
    currentScroll = Mth.clamp(currentScroll+(direction?1:-1),0,scrollable);
    return true;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    super.mouseClicked(mouseX, mouseY, button);
    int mouseXinGui = (int)mouseX-leftPos;
    int mouseYinGui = (int)mouseY-topPos;
    if(mouseXinGui>4 && mouseXinGui<23){
      if(mouseYinGui>17 && mouseYinGui<35){
        this.currentcat = ReagenimartCategory.BUILDING;
        currentselectedIndex=-1;
        currentScroll=0;
      }else if(mouseYinGui>39 && mouseYinGui<57){
        this.currentcat = ReagenimartCategory.COMMODITY;
        currentselectedIndex=-1;
        currentScroll=0;
      }else if(mouseYinGui>61 && mouseYinGui<79){
        this.currentcat = ReagenimartCategory.MINERALS;
        currentselectedIndex=-1;
        currentScroll=0;
      }else if(mouseYinGui>83 && mouseYinGui<101){
        this.currentcat = ReagenimartCategory.NATURALS;
        currentselectedIndex=-1;
        currentScroll=0;
      }
      sendSelectedRecipe();
    }else if(mouseXinGui>25 && mouseXinGui<79){
      if(mouseYinGui>14 && mouseYinGui<104){
        int x = (int)(mouseXinGui-25)/18;
        int y = (int)(mouseYinGui-14)/18;
        currentselectedIndex = x + y*3 + currentScroll*3;
        sendSelectedRecipe();
      }
    }
    return true;
  }

  private void sendSelectedRecipe(){
    if(getRecipeSelected()==null){
      ModMessages.CHANNEL.sendToServer(ComputerOrderPacket.createNullRecipePacket());
    }else{
      ModMessages.CHANNEL.sendToServer(new ComputerOrderPacket(currentcat, currentselectedIndex));
    }
  }
}
