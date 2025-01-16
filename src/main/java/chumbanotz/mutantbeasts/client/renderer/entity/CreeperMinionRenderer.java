package chumbanotz.mutantbeasts.client.renderer.entity;

import chumbanotz.mutantbeasts.client.model.CreeperMinionModel;
import chumbanotz.mutantbeasts.client.renderer.entity.layers.LayerCreeperCharge;
import chumbanotz.mutantbeasts.entity.CreeperMinionEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CreeperMinionRenderer
extends RenderLiving<CreeperMinionEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/creeper/creeper.png");

    public CreeperMinionRenderer(RenderManager renderManagerIn) {
        super(renderManagerIn, new CreeperMinionModel(), 0.25f);
        this.addLayer(new LayerCreeperCharge<CreeperMinionEntity>(this, new CreeperMinionModel(2.0f)));
    }

    protected void preRenderCallback(CreeperMinionEntity entitylivingbaseIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0f + MathHelper.sin((float)(f * 100.0f)) * f * 0.01f;
        f = MathHelper.clamp((float)f, (float)0.0f, (float)1.0f);
        f *= f;
        f *= f;
        float f2 = (1.0f + f * 0.4f) * f1 * 0.5f;
        float f3 = (1.0f + f * 0.1f) / f1 * 0.5f;
        GlStateManager.scale((float)f2, (float)f3, (float)f2);
    }

    protected int getColorMultiplier(CreeperMinionEntity entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        if ((int)(f * 10.0f) % 2 == 0) {
            return 0;
        }
        int i = (int)(f * 0.2f * 255.0f);
        i = MathHelper.clamp((int)i, (int)0, (int)255);
        return i << 24 | 0x30FFFFFF;
    }

    protected ResourceLocation getEntityTexture(CreeperMinionEntity entity) {
        return TEXTURE;
    }
}
