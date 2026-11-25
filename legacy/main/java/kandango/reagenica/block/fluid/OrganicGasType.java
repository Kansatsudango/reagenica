package kandango.reagenica.block.fluid;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

public class OrganicGasType extends FluidType{
  private final int color;
  public OrganicGasType(int color){
  super(FluidType.Properties.create()
    .canDrown(false)
    .canSwim(false)
    .density(1000)
    .viscosity(1000)
    .canPushEntity(false)
  );
  this.color = color;
  }

  @Override
  public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
  consumer.accept(new IClientFluidTypeExtensions() {
      private static final ResourceLocation STILL_TEXTURE =
        new ResourceLocation("reagenica", "block/gas");
      private static final ResourceLocation FLOWING_TEXTURE =
        new ResourceLocation("reagenica", "block/gas");
      private static final ResourceLocation OVERLAY_TEXTURE =
        new ResourceLocation("minecraft", "block/water_overlay"); // 任意

      @Override
      public ResourceLocation getStillTexture() {
        return STILL_TEXTURE;
      }

      @Override
      public ResourceLocation getFlowingTexture() {
        return FLOWING_TEXTURE;
      }

      @Override
      public ResourceLocation getOverlayTexture() {
        return OVERLAY_TEXTURE; // nullでもOK
      }

      @Override
      public int getTintColor() {
        return color;
      }
    });
  }
}
