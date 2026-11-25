package kandango.reagenica.block.fluid;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

public class OrganicFluidType extends FluidType{
  private final int color;
  public OrganicFluidType(int color){
  super(FluidType.Properties.create()
    .canDrown(true)
    .canSwim(true)
    .density(1000)
    .viscosity(1000)
    .supportsBoating(true)
    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
  );
  this.color = color;
  }

  @Override
  public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
  consumer.accept(new IClientFluidTypeExtensions() {
      private static final ResourceLocation STILL_TEXTURE =
        new ResourceLocation("minecraft", "block/water_still");
      private static final ResourceLocation FLOWING_TEXTURE =
        new ResourceLocation("minecraft", "block/water_flow");
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
