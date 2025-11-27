package kandango.reagenica.entity.renderer;

import javax.annotation.Nonnull;

import kandango.reagenica.ChemistryMod;
import kandango.reagenica.entity.SilverArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SilverArrowRenderer extends ArrowRenderer<SilverArrowEntity>{
  private static final ResourceLocation TEXTURE = new ResourceLocation(ChemistryMod.MODID, "textures/entity/silver_arrow.png");

  public SilverArrowRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public ResourceLocation getTextureLocation(@Nonnull SilverArrowEntity entity){
    return TEXTURE;
  }
}
