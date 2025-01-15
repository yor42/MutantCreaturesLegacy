package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.client.model.CreeperMinionModel;
import chumbanotz.mutantbeasts.client.renderer.entity.layers.LayerCreeperCharge;
import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CreeperMinionRenderer extends RenderLiving<CreeperMinionEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/creeper/creeper.png");

    public CreeperMinionRenderer(RenderManager renderManagerIn) {
        super(renderManagerIn, new CreeperMinionModel(), 0.25F);
        addLayer(new LayerCreeperCharge(this, new CreeperMinionModel(2.0F)));
    }

    protected void preRenderCallback(CreeperMinionEntity entitylivingbaseIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1 * 0.5F;
        float f3 = (1.0F + f * 0.1F) / f1 * 0.5F;
        GlStateManager.scale(f2, f3, f2);
    }

    protected int getColorMultiplier(CreeperMinionEntity entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        if ((int) (f * 10.0F) % 2 == 0) return 0;
        int i = (int) (f * 0.2F * 255.0F);
        i = MathHelper.clamp(i, 0, 255);
        return i << 24 | 0x30FFFFFF;
    }

    protected ResourceLocation getEntityTexture(CreeperMinionEntity entity) {
        return TEXTURE;
    }
}
