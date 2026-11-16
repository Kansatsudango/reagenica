package kandango.reagenica.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import com.mojang.blaze3d.systems.RenderSystem;

import kandango.reagenica.block.entity.StoreManagerComputerBlockEntity;
import kandango.reagenica.packet.ModMessages;
import kandango.reagenica.packet.ShopNameChangePacket;
import kandango.reagenica.packet.TransactionListRequestPacket;
import kandango.reagenica.world.retail.Transaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StoreManagerComputerScreen extends AbstractContainerScreen<StoreManagerComputerMenu> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("reagenica", "textures/gui/storemanager_computer.png");

  private ScreenType currentScreen = ScreenType.MAINMENU;
  private final List<Supplier<? extends WrappedWidget<?>>> mainmenuWidgets;
  private final List<Supplier<? extends WrappedWidget<?>>> transactionsWidgets;
  private final List<Supplier<? extends WrappedWidget<?>>> shopWidgets;
  private final List<Supplier<? extends WrappedWidget<?>>> configWidgets;
  private final Map<ScreenType, List<Supplier<? extends WrappedWidget<?>>>> screenWidgetCreators = new EnumMap<>(ScreenType.class);
  private final Map<ScreenType, List<HideableWidget>> screenWidgets = new EnumMap<>(ScreenType.class);
  private TransactionsListWidget transactionList;
  private EditBox companyNameBox;
  private EditBox shopNameBox;

  private List<Transaction> transactionsCache = new ArrayList<>();

  public StoreManagerComputerScreen(StoreManagerComputerMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
    this.imageHeight = 203;
    this.imageWidth = 241;
    this.inventoryLabelY = 9999;
    this.mainmenuWidgets = List.of(
      pageSwitchButton("transactions", ScreenType.VIEWTRANSACTIONS, 10,10,150,20),
      pageSwitchButton("shop", ScreenType.SHOP,10,35,150,20),
      pageSwitchButton("config", ScreenType.CONFIG,10,60,150,20)
    );
    this.transactionsWidgets = List.of(
      pageSwitchButton("mainmenu", ScreenType.MAINMENU,10,170,70,20),
      packetButton("reflesh", b -> {
          ModMessages.CHANNEL.sendToServer(new TransactionListRequestPacket(menu.getBlockEntity().getCompanyName()));
        },100,170,70,20)
    );
    this.shopWidgets = List.of(
      pageSwitchButton("mainmenu", ScreenType.MAINMENU,10,170,70,20)
    );
    this.configWidgets = List.of(
      pageSwitchButton("mainmenu", ScreenType.MAINMENU,10,170,70,20),
      packetButton("setname", b -> {
        if(this.companyNameBox!=null && this.shopNameBox!=null){
          ModMessages.CHANNEL.sendToServer(new ShopNameChangePacket(this.shopNameBox.getValue(), this.companyNameBox.getValue()));
        }
      }, 10,150,120,20)
    );
    this.screenWidgetCreators.put(ScreenType.MAINMENU, mainmenuWidgets);
    this.screenWidgetCreators.put(ScreenType.VIEWTRANSACTIONS, transactionsWidgets);
    this.screenWidgetCreators.put(ScreenType.SHOP, shopWidgets);
    this.screenWidgetCreators.put(ScreenType.CONFIG, configWidgets);
    transactionList = new TransactionsListWidget(minecraft, this.imageWidth-16, this.height, topPos+40, topPos+160, 64);
  }

  private Supplier<WrappedWidget<Button>> pageSwitchButton(String name, ScreenType nextScreen, int x, int y, int width, int height){
    return () -> new WrappedWidget<Button>(Button.builder(Component.translatable("gui.reagenica.smc.button."+name), b -> this.currentScreen = nextScreen)
                  .pos(leftPos+x, topPos+y).size(width, height).build());
  }
  private Supplier<WrappedWidget<Button>> packetButton(String name, Button.OnPress action, int x, int y, int width, int height){
    return () -> new WrappedWidget<Button>(Button.builder(Component.translatable("gui.reagenica.smc.button."+name), 
                  action)
                  .pos(leftPos+x, topPos+y).size(width, height).build());
  }

  @Override
  protected void init() {
    super.init();
    screenWidgets.clear();
    screenWidgetCreators.keySet().forEach(key -> screenWidgets.put(key, new ArrayList<>()));
    screenWidgetCreators.forEach((type, list) -> {
      List<? extends WrappedWidget<?>> widgets = list.stream().map(Supplier::get).toList();
      screenWidgets.get(type).addAll(widgets);
      widgets.forEach(w -> addRenderableWidget(w.widget));
    });

    StoreManagerComputerBlockEntity be = menu.be;
    ModMessages.CHANNEL.sendToServer(new TransactionListRequestPacket((be.getCompanyName())));
    transactionList.setLeftPos(leftPos+8);
    addRenderableWidget(transactionList);
    screenWidgets.get(ScreenType.VIEWTRANSACTIONS).add(transactionList);
    transactionList.refleshTransactions(transactionsCache);

    companyNameBox = new EditBox(font, leftPos+10, topPos+40, 150, 20, Component.translatable("gui.reagenica.smc.company_name"));
    shopNameBox = new EditBox(font, leftPos+10, topPos+70, 150, 20, Component.translatable("gui.reagenica.smc.shop_name"));
    companyNameBox.setMaxLength(20);
    shopNameBox.setMaxLength(20);
    companyNameBox.setHint(Component.translatable("gui.reagenica.smc.company_name.hint"));
    shopNameBox.setHint(Component.translatable("gui.reagenica.smc.shop_name.hint"));
    addRenderableWidget(companyNameBox);
    addRenderableWidget(shopNameBox);
    screenWidgets.get(ScreenType.CONFIG).add(new WrappedWidget<EditBox>(companyNameBox));
    screenWidgets.get(ScreenType.CONFIG).add(new WrappedWidget<EditBox>(shopNameBox));
    companyNameBox.setValue(menu.getBlockEntity().getCompanyName());
    shopNameBox.setValue(menu.getBlockEntity().getShopName());
    updateWidgetsVisibility();
  }

  public void onListReflesh(List<Transaction> transactions){
    this.transactionsCache = transactions;
    transactionList.refleshTransactions(transactionsCache);
    transactionList.setScrollAmount(0.0);
  }

  private void updateWidgetsVisibility(){
    final ScreenType currentTypeCache = this.currentScreen;
    screenWidgets.forEach((type, list) -> {
      if(type.equals(currentTypeCache)){
        list.forEach(widget -> widget.setVisibility(true));
      }else{
        list.forEach(widget -> widget.setVisibility(false));
      }}
    );
  }

  @Override 
  protected void containerTick(){
    updateWidgetsVisibility();
  }

  @Override
  protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0, TEXTURE);
    graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
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

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if ((isBoxFocused(companyNameBox) || isBoxFocused(shopNameBox))
        && minecraft.options.keyInventory.matches(keyCode, scanCode)) {
        return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }
  private boolean isBoxFocused(EditBox box){
    return box!=null && box.isFocused();
  }

  private enum ScreenType{
    MAINMENU,
    VIEWTRANSACTIONS,
    SHOP,
    CONFIG
  }

  private static class TransactionsListWidget extends ObjectSelectionList<TransactionCard> implements HideableWidget{
    public boolean visible;
    public TransactionsListWidget(
      Minecraft minecraft,
      int width,
      int height,
      int top,
      int bottom,
      int itemHeight
    ) {
      super(minecraft, width, height, top, bottom, itemHeight);
      this.setRenderBackground(false);
      this.setRenderTopAndBottom(false);
    }

    @Override
    public int getRowWidth() {
      return this.width - 12;
    }

    @Override
    protected int getScrollbarPosition() {
      return this.getRowRight();
    }

    @Override
    public void setVisibility(boolean visibility) {
      this.visible = visibility;
    }
    
    @Override
    public void render(GuiGraphics g, int i1, int i2, float f) {
      if(this.visible) super.render(g, i1, i2, f);
    }

    public void clearTransactions(){
      this.clearEntries();
    }

    public void addTransaction(Transaction transaction){
      this.addEntry(new TransactionCard(transaction));
    }
    public void refleshTransactions(Collection<Transaction> transactions){
      clearTransactions();
      for(Transaction transaction : transactions){
        addTransaction(transaction);
      }
    }
  }
  private static class TransactionCard extends ObjectSelectionList.Entry<TransactionCard>{
    private final Transaction transaction;
    public TransactionCard(Transaction transaction){
      this.transaction = transaction;
    }

    @Override
    public Component getNarration() {
      return Component.literal("Transaction at "+transaction.getShopName());
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int index,
            int top,
            int left,
            int entryWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float partialTick
    ) {
      int backgroundColor = hovered ? 0xFF606060 : 0xFF404040;
      graphics.fill(left, top, left + entryWidth, top + entryHeight - 2, backgroundColor);

      int itemIndex = 0;
      Map<Item, Integer> itemAndCount = stackItems(transaction.getItems().stream().map(si -> si.stack()).toList());
      for (Map.Entry<Item, Integer> si : itemAndCount.entrySet()) {
        ItemStack stack = new ItemStack(si.getKey(), si.getValue());
        int itemX = left + 6 + 20*(itemIndex%5);
        int itemY = top + 6 + 20*(itemIndex/5);
        graphics.renderFakeItem(stack, itemX, itemY);
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, itemX, itemY);
        itemIndex++;
        if(itemIndex >= 15) break;
      }

      graphics.drawString(Minecraft.getInstance().font, 
                            Component.translatable("gui.reagenica.smc.shopname", transaction.getShopName()), 
                            left+100, top+6, 0xffffff, false);
      int sales = transaction.getItems().stream().mapToInt(si -> si.price()).sum();
      graphics.drawString(Minecraft.getInstance().font, 
                            Component.translatable("gui.reagenica.smc.sum", sales), 
                            left+100, top+16, 0xffffff, false);
      int cost = transaction.getItems().stream().mapToInt(si -> si.cost()).sum();
      int profit = sales - cost;
      graphics.drawString(Minecraft.getInstance().font, 
                            Component.translatable("gui.reagenica.smc.profit", profit), 
                            left+100, top+26, 0xffffff, false);
      long transactionTime = transaction.getTime();
      long currentTime = Optional.ofNullable(Minecraft.getInstance().level).map(lv -> lv.getGameTime()).orElse(-1L);
      graphics.drawString(Minecraft.getInstance().font, 
                            ticksAgo(currentTime - transactionTime), 
                            left+100, top+36, 0xffffff, false);
    }
    private Component ticksAgo(long tick){
      if(tick > Integer.MAX_VALUE){
        return Component.translatable("gui.reagenica.time.sofar");
      }else if(tick < 0){
        return Component.translatable("gui.reagenica.time.future");
      }else{
        int s = (int)tick/20;
        if(s<60){
          return Component.translatable("gui.reagenica.time.secs_ago", s);
        }else{
          int m = s/60;
          if(m<60){
            return Component.translatable("gui.reagenica.time.mins_ago", m);
          }else{
            int h = m/60;
            if(h<24){
              return Component.translatable("gui.reagenica.time.hours_ago", h);
            }else{
              return Component.translatable("gui.reagenica.time.sofar");
            }
          }
        }
      }
    }

    private Map<Item, Integer> stackItems(List<ItemStack> stacks){
      final Map<Item, Integer> map = new HashMap<>();
      for(ItemStack stack : stacks){
        Item item = stack.getItem();
        int count = stack.getCount();
        Integer x = map.get(item);
        if(x!=null){
          map.put(item, (x+count));
        }else{
          map.put(item, count);
        }
      }
      return map;
    }
  }

  private static interface HideableWidget extends Renderable{
    public void setVisibility(boolean visibility);
  }
  private static class WrappedWidget<T extends AbstractWidget> implements HideableWidget{
    final T widget;
    public WrappedWidget(T widget){
      this.widget = widget;
    }

    public void setVisibility(boolean visibility){
      this.widget.visible = visibility;
    }

    @Override
    public void render(GuiGraphics g, int i1, int i2, float f) {
      widget.render(g, i1, i2, f);
    }
    
  }
}
