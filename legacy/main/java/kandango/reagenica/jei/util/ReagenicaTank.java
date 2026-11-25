package kandango.reagenica.jei.util;

import kandango.reagenica.item.reagent.ReagentFluidMap;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ReagenicaTank {
  final int x;
  final int y;
  final int itemx;
  final int itemy;
  int width=16;
  int height=48;
  int capacity=400;
  boolean showCapacity = false;
  FluidStack fluid = FluidStack.EMPTY;
  private ReagenicaTank(int x, int y, int ix, int iy){
    this.x=x;
    this.y=y;
    this.itemx=ix;
    this.itemy=iy;
  }
  public static ReagenicaTank create(int tankX, int tankY, int itemX, int itemY){
    return new ReagenicaTank(tankX, tankY, itemX, itemY);
  }
  public ReagenicaTank renderSize(int width, int height){
    this.width=width;
    this.height=height;
    return this;
  }
  public ReagenicaTank capacity(int capacity){
    this.capacity=capacity;
    return this;
  }
  public ReagenicaTank setFluid(FluidStack stack){
    this.fluid = stack;
    return this;
  }
  public ReagenicaTank showCapacity(){
    this.showCapacity=true;
    return this;
  }
  public void consumeAsInputTank(IRecipeLayoutBuilder builder){
    consume(builder, RecipeIngredientRole.INPUT);
  }
  public void consumeAsOutputTank(IRecipeLayoutBuilder builder){
    consume(builder, RecipeIngredientRole.OUTPUT);
  }
  private void consume(IRecipeLayoutBuilder builder, RecipeIngredientRole role){
    if(this.fluid.isEmpty())return;
    builder.addSlot(role,x,y)
      .addFluidStack(this.fluid.getFluid(), this.fluid.getAmount())
      .setFluidRenderer(this.capacity, this.showCapacity, this.width, this.height);
    ReagentFluidMap.getItemfromFluid(fluid.getFluid()).map(item -> new ItemStack(item)).ifPresent(reagentItem -> {
      builder.addSlot(role, itemx, itemy).addItemStack(reagentItem);
    });
  }
}
