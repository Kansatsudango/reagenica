package kandango.reagenica.item;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class RoleItem extends Item{
  private final Consumer<List<Component>> role;

  public RoleItem(Item.Properties properties, Consumer<List<Component>> role) {
    super(properties);
    this.role = role;
  }
  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    role.accept(tooltipComponents);
  }
}